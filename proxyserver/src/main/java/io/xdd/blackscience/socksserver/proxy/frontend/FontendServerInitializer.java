package io.xdd.blackscience.socksserver.proxy.frontend;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.socks.SocksInitRequestDecoder;
import io.netty.handler.codec.socks.SocksMessageEncoder;
import io.xdd.blackscience.socksserver.proxy.SocksServerHandler;

public class FontendServerInitializer extends ChannelInitializer<SocketChannel> {

    private final SocksMessageEncoder socksMessageEncoder = new SocksMessageEncoder();
    private final SocksServerHandler socksServerHandler = new SocksServerHandler();


    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline p = socketChannel.pipeline();
        /**
         * Socks request 解码
         * */
        p.addLast(new SocksInitRequestDecoder());

        /**
         * socks 编码
         * */
        p.addLast(socksMessageEncoder);

        /**
         * 数据处理
         * */
        p.addLast(socksServerHandler);
    }
}
