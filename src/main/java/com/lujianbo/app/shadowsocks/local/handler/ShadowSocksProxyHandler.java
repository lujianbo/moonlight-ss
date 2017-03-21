package com.lujianbo.app.shadowsocks.local.handler;

import com.lujianbo.app.shadowsocks.common.codec.SSDecoder;
import com.lujianbo.app.shadowsocks.common.codec.SSEncoder;
import com.lujianbo.app.shadowsocks.common.codec.ShadowSocksRequest;
import com.lujianbo.app.shadowsocks.common.codec.ShadowSocksRequestEncoder;
import com.lujianbo.app.shadowsocks.common.manager.SSServerInstance;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ShadowSocksProxyHandler extends ChannelInboundHandlerAdapter {

    /**
     * 目标信息
     */
    private SSServerInstance instance;

    private ShadowSocksRequest request;

    public ShadowSocksProxyHandler(ShadowSocksRequest request, SSServerInstance instance) {
        this.instance = instance;
        this.request = request;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext outboundChannel) throws Exception {

        outboundChannel.pipeline().addLast(new SSEncoder(instance.getMethod(), instance.getPassword()));

        outboundChannel.pipeline().addLast(new ShadowSocksRequestEncoder());

        outboundChannel.channel().write(request);

        outboundChannel.pipeline().addLast(new SSDecoder(instance.getMethod(), instance.getPassword()));

    }

}
