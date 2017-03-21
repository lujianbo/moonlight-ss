package com.lujianbo.app.shadowsocks.local.handler;

import com.lujianbo.app.shadowsocks.common.handler.RelayHandler;
import com.lujianbo.app.shadowsocks.common.manager.SSServerInstance;
import com.lujianbo.app.shadowsocks.common.manager.SSServerManager;
import com.lujianbo.app.shadowsocks.common.utils.NetUtils;
import com.lujianbo.app.shadowsocks.common.utils.ShadowUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.socks.SocksCmdRequest;
import io.netty.handler.codec.socks.SocksCmdResponse;
import io.netty.handler.codec.socks.SocksCmdStatus;
import io.netty.handler.codec.socks.SocksMessageEncoder;


/**
 * 连接 shadowsocks 服务器
 */
public class ShadowSocksConnectHandler extends SimpleChannelInboundHandler<SocksCmdRequest> {

    private final Bootstrap b = new Bootstrap();

    private SSServerManager shadowSocksServerManager;

    public ShadowSocksConnectHandler() {
        this.shadowSocksServerManager = SSServerManager.getInstance();
    }

    @Override
    public void channelRead0(final ChannelHandlerContext ctx, final SocksCmdRequest request) throws Exception {
        SSServerInstance instance = shadowSocksServerManager.getOne();

        final Channel inboundChannel = ctx.channel();
        b.group(inboundChannel.eventLoop())
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                .option(ChannelOption.SO_KEEPALIVE, true);

        //连接目标服务器
        b.connect(instance.getAddress(), instance.getPort())
                .addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                ctx.channel().writeAndFlush(new SocksCmdResponse(SocksCmdStatus.SUCCESS, request.addressType()))
                        .addListener(channelFuture -> {
                            if (channelFuture.isSuccess()){
                                //移除当前的处理数据
                                ctx.pipeline().remove(ShadowSocksConnectHandler.this);
                                ctx.pipeline().remove(SocksMessageEncoder.class);
                                future.channel().pipeline().addLast(new ShadowSocksProxyHandler(ShadowUtils.transform(request), instance));
                                ctx.pipeline().addLast(new RelayHandler(future.channel()));
                                future.channel().pipeline().addLast(new RelayHandler(ctx.channel()));
                            }else {
                                NetUtils.closeOnFlush(future.channel());
                                NetUtils.closeOnFlush(ctx.channel());
                            }
                        });
            } else {
                // Close the connection if the connection attempt has failed.
                ctx.channel().writeAndFlush(new SocksCmdResponse(SocksCmdStatus.FAILURE, request.addressType()));
                NetUtils.closeOnFlush(ctx.channel());
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        NetUtils.closeOnFlush(ctx.channel());
    }

}
