package io.xdd.blackscience.socksserver.local.server.shadowsocks;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.xdd.blackscience.socksserver.local.common.handler.codec.SSDecoder;
import io.xdd.blackscience.socksserver.local.common.handler.codec.SSEncoder;
import io.xdd.blackscience.socksserver.local.common.handler.codec.ShadowSocksRequestDecoder;

@ChannelHandler.Sharable
public class BackendServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        ch.pipeline().addLast(new SSDecoder("method","password"));
        //解码地址部分
        ch.pipeline().addLast(new ShadowSocksRequestDecoder());

        ch.pipeline().addLast(new BackendServerConnectHandler());

        ch.pipeline().addLast(new SSEncoder("method","password"));
    }
}
