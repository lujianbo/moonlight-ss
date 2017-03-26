package com.lujianbo.app.shadowsocks.local.connector.socks;

import com.lujianbo.app.shadowsocks.common.crypto.ShadowSocksContext;
import com.lujianbo.app.shadowsocks.common.handler.ConnectRelayHandler;
import com.lujianbo.app.shadowsocks.common.handler.ShadowSocksClientInitializer;
import com.lujianbo.app.shadowsocks.local.manager.ShadowSocksServerInfo;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.socks.SocksInitRequestDecoder;
import io.netty.handler.codec.socks.SocksMessageEncoder;

import java.security.NoSuchAlgorithmException;

public class SocksServerInitializer extends ChannelInitializer<SocketChannel> {

    private ShadowSocksServerInfo instance;

    private ShadowSocksContext shadowSocksContext;

    public SocksServerInitializer(ShadowSocksServerInfo instance) throws NoSuchAlgorithmException {
        this.instance = instance;
        shadowSocksContext = new ShadowSocksContext(instance.getMethod(), instance.getPassword());
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline p = socketChannel.pipeline();
        //Socks request decode it will remove itself
        p.addLast(new SocksInitRequestDecoder());
        //socks encode
        p.addLast(new SocksMessageEncoder());
        //process SocksRequest
        p.addLast(new SocksRequestHandler());
        // connect to Shadow Socks
        p.addLast(new ConnectRelayHandler(instance.getAddress(), instance.getPort(), () -> new ShadowSocksClientInitializer(shadowSocksContext)));

    }
}
