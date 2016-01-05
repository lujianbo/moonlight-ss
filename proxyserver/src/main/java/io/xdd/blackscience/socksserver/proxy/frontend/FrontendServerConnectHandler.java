package io.xdd.blackscience.socksserver.proxy.frontend;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.socks.SocksAddressType;
import io.netty.handler.codec.socks.SocksCmdRequest;
import io.netty.handler.codec.socks.SocksCmdResponse;
import io.netty.handler.codec.socks.SocksCmdStatus;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Promise;
import io.xdd.blackscience.socksserver.common.manager.SSServerInstance;
import io.xdd.blackscience.socksserver.common.manager.SSServerManager;
import io.xdd.blackscience.socksserver.proxy.handler.PromiseHandler;
import io.xdd.blackscience.socksserver.proxy.handler.RelayHandler;
import io.xdd.blackscience.socksserver.proxy.handler.codec.*;
import io.xdd.blackscience.socksserver.proxy.utils.SocksServerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * 连接 shadowsocks 服务器
 * */
@ChannelHandler.Sharable
public class FrontendServerConnectHandler extends SimpleChannelInboundHandler<SocksCmdRequest> {

    private Logger logger= LoggerFactory.getLogger(getClass());

    private final Bootstrap b = new Bootstrap();

    private SSServerManager shadowSocksServerManager;

    public FrontendServerConnectHandler(){
        this.shadowSocksServerManager= SSServerManager.getInstance();
    }

    @Override
    public void channelRead0(final ChannelHandlerContext ctx, final SocksCmdRequest request) throws Exception {
        SSServerInstance instance=shadowSocksServerManager.getOne();
        Promise<Channel> promise = ctx.executor().newPromise();
        promise.addListener(
                new GenericFutureListener<Future<Channel>>() {
                    @Override
                    public void operationComplete(final Future<Channel> future) throws Exception {
                        final Channel outboundChannel = future.getNow();
                        if (future.isSuccess()) {
                            ctx.channel()
                                    .writeAndFlush(new SocksCmdResponse(SocksCmdStatus.SUCCESS, request.addressType()))
                                    .addListener(channelFuture -> {
                                        /**
                                         * 移除当前的处理数据
                                         * */
                                        ctx.pipeline().remove(FrontendServerConnectHandler.this);
                                        outboundChannel.pipeline().addLast(new SSEncoder(instance.getMethod(),instance.getPassword()));
                                        outboundChannel.pipeline().addLast(new ShadowSocksRequestEncoder());
                                        outboundChannel.write(transform(request));
                                        ctx.pipeline().addLast(new RelayHandler(outboundChannel));
                                        outboundChannel.pipeline().addLast(new SSDecoder(instance.getMethod(),instance.getPassword()));
                                        outboundChannel.pipeline().addLast(new RelayHandler(ctx.channel()));
                                    });
                        } else {
                            ctx.channel().writeAndFlush(new SocksCmdResponse(SocksCmdStatus.FAILURE, request.addressType()));
                            SocksServerUtils.closeOnFlush(ctx.channel());
                        }
                    }
                });

        /**
         * 配置Handler
         * */
        final Channel inboundChannel = ctx.channel();
        b.group(inboundChannel.eventLoop())
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new PromiseHandler(promise));//把promise设置到Handler中来触发promise

        /**
         * 获取目标服务器的相关信息
         * */

        /**
         * 连接目标服务器
         * */
        b.connect(instance.getAddress(),instance.getPort()).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    //ctx.channel().writeAndFlush(new SocksCmdResponse(SocksCmdStatus.SUCCESS, request.addressType()));
                    // Connection established use handler provided results
                } else {
                    // Close the connection if the connection attempt has failed.
                    ctx.channel().writeAndFlush(new SocksCmdResponse(SocksCmdStatus.FAILURE, request.addressType()));
                    SocksServerUtils.closeOnFlush(ctx.channel());
                }
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        SocksServerUtils.closeOnFlush(ctx.channel());
    }

    private ShadowSocksRequest transform(SocksCmdRequest request) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException {
        SocksAddressType addressType=request.addressType();
        String host=request.host();
        int port= request.port();
        ShadowSocksRequest shadowSocksRequest = null;
        switch (addressType) {
            case IPv4: {
                shadowSocksRequest=new ShadowSocksRequest(ShadowSocksAddressType.IPv4,host,port);
                break;
            }
            case DOMAIN: {
                shadowSocksRequest=new ShadowSocksRequest(ShadowSocksAddressType.hostname,host,port);
                break;
            }
            case IPv6: {
                shadowSocksRequest=new ShadowSocksRequest(ShadowSocksAddressType.IPv6,host,port);
                break;
            }
        }
        return shadowSocksRequest;
    }
}
