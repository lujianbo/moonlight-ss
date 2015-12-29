package io.xdd.blackscience.socksserver.proxy;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;


/**
 *  socks 服务器的启动类
 * */
public final class SocksServer {

    static final int PORT = Integer.parseInt(System.getProperty("port", "1080"));

    private EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    public void start() {
       try {
           ServerBootstrap b = new ServerBootstrap();
           b.group(bossGroup, workerGroup)
                   .channel(NioServerSocketChannel.class)
                   .handler(new LoggingHandler(LogLevel.INFO))
                   .childHandler(new SocksServerInitializer());
           b.bind(PORT).sync().channel().closeFuture().sync();
       } catch (Exception e) {
           e.printStackTrace();
       } finally {
           bossGroup.shutdownGracefully();
           workerGroup.shutdownGracefully();
       }
    }

    public void stop(){
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

}
