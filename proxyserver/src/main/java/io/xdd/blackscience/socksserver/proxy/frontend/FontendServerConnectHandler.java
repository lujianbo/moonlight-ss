package io.xdd.blackscience.socksserver.proxy.frontend;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.socks.SocksAddressType;
import io.netty.handler.codec.socks.SocksCmdRequest;
import io.netty.handler.codec.socks.SocksCmdResponse;
import io.netty.handler.codec.socks.SocksCmdStatus;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Promise;
import io.xdd.blackscience.socksserver.proxy.handler.PromiseHandler;
import io.xdd.blackscience.socksserver.proxy.handler.RelayHandler;
import io.xdd.blackscience.socksserver.proxy.handler.codec.ShadowSocksAddressType;
import io.xdd.blackscience.socksserver.proxy.handler.codec.ShadowSocksServerConnectInitializer;
import io.xdd.blackscience.socksserver.proxy.utils.SocksServerUtils;

import javax.crypto.NoSuchPaddingException;
import java.net.SocketAddress;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class FontendServerConnectHandler extends SimpleChannelInboundHandler<SocksCmdRequest> {

    private final Bootstrap b = new Bootstrap();

    private SocketAddress socketAddress;

    private String password;

    public FontendServerConnectHandler(SocketAddress socketAddress, String password){
        this.socketAddress=socketAddress;
        this.password = password;
    }

    public FontendServerConnectHandler(){}

    @Override
    public void channelRead0(final ChannelHandlerContext ctx, final SocksCmdRequest request) throws Exception {
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
                                        ctx.pipeline().remove(FontendServerConnectHandler.this);
                                        /**
                                         * 设置 ctx数据的处理方式
                                         * */
                                        ctx.pipeline().addLast(new RelayHandler(outboundChannel));

                                        /**
                                         * 设置 outboundChannel
                                         * */
                                        outboundChannel.pipeline().addLast(new RelayHandler(ctx.channel()));


                                        outboundChannel.pipeline().addLast(getHandler(request));

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
                //把promise设置到Handler中来触发promise
                .handler(new PromiseHandler(promise));

        /**
         * 连接目标服务器
         * */
        b.connect(socketAddress).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
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


    private ShadowSocksServerConnectInitializer getHandler(SocksCmdRequest request) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException {
        SocksAddressType addressType=request.addressType();
        String host=request.host();
        short port= (short) request.port();
        ShadowSocksServerConnectInitializer serverConnectInitializer = null;
        switch (addressType) {
            case IPv4: {
                serverConnectInitializer=new ShadowSocksServerConnectInitializer(password, ShadowSocksAddressType.IPv4,host,port);
                break;
            }
            case DOMAIN: {
                serverConnectInitializer=new ShadowSocksServerConnectInitializer(password,ShadowSocksAddressType.hostname,host,port);
                break;
            }
            case IPv6: {
                serverConnectInitializer=new ShadowSocksServerConnectInitializer(password,ShadowSocksAddressType.IPv6,host,port);
                break;
            }
        }
        return serverConnectInitializer;
    }
}
