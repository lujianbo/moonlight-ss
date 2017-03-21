package com.lujianbo.app.shadowsocks.server.handler;

import com.lujianbo.app.shadowsocks.common.codec.SSDecoder;
import com.lujianbo.app.shadowsocks.common.codec.SSEncoder;
import com.lujianbo.app.shadowsocks.common.codec.ShadowSocksRequestDecoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class ShadowSocksServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        //解码被加密的流量
        ch.pipeline().addLast(new SSDecoder("method", "password"));
        //解码地址部分
        ch.pipeline().addLast(new ShadowSocksRequestDecoder());
        //处理解密后的数据流量
        ch.pipeline().addLast(new ShadowSocksRequestHandler());
        //对返回的数据进行加密
        ch.pipeline().addLast(new SSEncoder("method", "password"));
    }
}
