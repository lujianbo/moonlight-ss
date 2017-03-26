package com.lujianbo.app.shadowsocks.common.handler;

import com.lujianbo.app.shadowsocks.common.utils.NetUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.ReferenceCountUtil;

import java.util.function.Supplier;

/**
 * connect to given host and port and relay it's data
 */
public class ConnectRelayHandler extends ChannelInboundHandlerAdapter {

    private static EventLoopGroup executors = new NioEventLoopGroup();
    private Channel outboundChannel;
    private String host;
    private int port;
    private Supplier<ChannelHandler> initSupplier;

    public ConnectRelayHandler(String host, int port, Supplier<ChannelHandler> initSupplier) {
        this.host = host;
        this.port = port;
        this.initSupplier = initSupplier;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Bootstrap b = new Bootstrap();
        b.group(executors)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(initSupplier.get());
        outboundChannel = b.connect(host, port).sync().channel();
        //add client relay to ctx
        outboundChannel.pipeline().addLast(new RelayHandler(ctx.channel()));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (outboundChannel.isActive()) {
            outboundChannel.writeAndFlush(msg);
        } else {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        if (outboundChannel.isActive()) {
            NetUtils.closeOnFlush(outboundChannel);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }
}
