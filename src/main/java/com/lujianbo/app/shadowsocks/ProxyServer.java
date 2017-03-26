package com.lujianbo.app.shadowsocks;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import net.openhft.affinity.AffinityStrategies;
import net.openhft.affinity.AffinityThreadFactory;

import java.util.concurrent.ThreadFactory;

/**
 * socks 服务器的启动类
 */
public final class ProxyServer {

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    private ChannelInitializer<SocketChannel> initializer;

    private int port;

    public ProxyServer(int port, ChannelInitializer<SocketChannel> initializer) {
        this.initializer = initializer;
        this.port = port;
        bossGroup = new NioEventLoopGroup(1);
        //ThreadFactory threadFactory = new AffinityThreadFactory("atf_wrk", AffinityStrategies.DIFFERENT_CORE);
        int workerSize=Runtime.getRuntime().availableProcessors();
        //workerGroup = new NioEventLoopGroup(workerSize,threadFactory);
        workerGroup = new NioEventLoopGroup(workerSize);
    }

    public void start() {
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .childOption(ChannelOption.ALLOCATOR, new PooledByteBufAllocator(true))
                    .childOption(ChannelOption.SO_REUSEADDR, true)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(initializer);
            b.bind(port).sync().addListener(future -> {
                if (future.isSuccess()) {
                    System.out.println("启动成功");
                } else {
                    System.out.println("启动失败");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

}
