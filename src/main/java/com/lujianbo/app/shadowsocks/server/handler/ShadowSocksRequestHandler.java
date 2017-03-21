package com.lujianbo.app.shadowsocks.server.handler;

import com.lujianbo.app.shadowsocks.common.codec.ShadowSocksRequest;
import com.lujianbo.app.shadowsocks.common.handler.RelayHandler;
import com.lujianbo.app.shadowsocks.common.utils.NetUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Promise;

public final class ShadowSocksRequestHandler extends SimpleChannelInboundHandler<ShadowSocksRequest> {

    private final Bootstrap b = new Bootstrap();

    @Override
    public void channelRead0(final ChannelHandlerContext ctx, final ShadowSocksRequest request) throws Exception {

        final Channel inboundChannel = ctx.channel();

        b.group(inboundChannel.eventLoop())
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                .option(ChannelOption.SO_KEEPALIVE, true);
        //向目标端口发起连接
        b.connect(request.host(), request.port()).addListener((ChannelFutureListener) future -> {
            final Channel outboundChannel = future.channel();
            if (future.isSuccess()) {
                ctx.pipeline().remove(ShadowSocksRequestHandler.this);
                //两个 channel上的数据进行交换
                outboundChannel.pipeline().addLast(new RelayHandler(ctx.channel()));
                ctx.pipeline().addLast(new RelayHandler(outboundChannel));
            } else {
                NetUtils.closeOnFlush(ctx.channel());
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        NetUtils.closeOnFlush(ctx.channel());
    }
}
