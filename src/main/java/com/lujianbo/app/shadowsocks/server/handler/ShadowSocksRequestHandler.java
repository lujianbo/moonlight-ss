package com.lujianbo.app.shadowsocks.server.handler;

import com.lujianbo.app.shadowsocks.common.codec.ShadowSocksRequest;
import com.lujianbo.app.shadowsocks.common.handler.RelayHandler;
import com.lujianbo.app.shadowsocks.common.utils.NetUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;

public final class ShadowSocksRequestHandler extends ChannelInboundHandlerAdapter {

    private static EventLoopGroup executors = new NioEventLoopGroup();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof ShadowSocksRequest) {
            ShadowSocksRequest request = (ShadowSocksRequest) msg;
            Bootstrap b = new Bootstrap();
            b.group(executors)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new RelayHandler(ctx.channel()));
            Channel outboundChannel = b.connect(request.host(), request.port()).sync().channel();
            ctx.pipeline().remove(ShadowSocksRequestHandler.this);
            outboundChannel.pipeline().addFirst(new LoggingHandler());
            ctx.pipeline().addLast(new RelayHandler(outboundChannel));
        } else {
            // no in  here
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        NetUtils.closeOnFlush(ctx.channel());
    }
}
