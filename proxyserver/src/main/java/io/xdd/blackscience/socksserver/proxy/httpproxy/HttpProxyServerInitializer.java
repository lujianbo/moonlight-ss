package io.xdd.blackscience.socksserver.proxy.httpproxy;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpRequestEncoder;

public class HttpProxyServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        /**
         * Request 解码
         * */
        ch.pipeline().addLast(new HttpRequestDecoder());

        /**
         * Response 编码
         * */
        ch.pipeline().addLast(new HttpRequestEncoder());



        ch.pipeline().addLast(new HttpProxyHandler());
    }
}
