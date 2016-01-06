package io.xdd.blackscience.socksserver.proxy.httpproxy;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpObject;

public class HttpProxyConnectHandler extends SimpleChannelInboundHandler<HttpObject> {

    /**
     * 目标 bound
     * */
    private Channel outboundChannel;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {

    }
}
