package io.xdd.blackscience.socksserver.proxy.socksadapter;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.socks.SocksInitRequestDecoder;
import io.netty.handler.codec.socks.SocksMessageEncoder;

@ChannelHandler.Sharable
public class FrontendServerInitializer extends ChannelInitializer<SocketChannel> {

    private final SocksMessageEncoder socksMessageEncoder = new SocksMessageEncoder();
    private final FrontendServerHandler frontendServerHandler = new FrontendServerHandler();


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
        p.addLast(frontendServerHandler);

    }
}
