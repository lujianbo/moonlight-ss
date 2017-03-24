package com.lujianbo.app.shadowsocks;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * socks 服务器的启动类
 */
public final class ProxyServer {

    private EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    private ChannelInitializer<SocketChannel> initializer;

    private int port;

    public ProxyServer(int port,ChannelInitializer<SocketChannel> initializer) {
        this.initializer = initializer;
        this.port=port;
    }

    public void start() {
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(initializer);
            b.bind(port).sync().addListener(future -> {
                if (future.isSuccess()){
                    System.out.println("启动成功");
                }else {
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
