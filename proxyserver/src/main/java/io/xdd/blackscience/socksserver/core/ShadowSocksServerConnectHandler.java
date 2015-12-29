package io.xdd.blackscience.socksserver.core;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.socks.SocksCmdRequest;

public class ShadowSocksServerConnectHandler extends SimpleChannelInboundHandler<SocksCmdRequest> {



    @Override
    protected void channelRead0(ChannelHandlerContext ctx, SocksCmdRequest msg) throws Exception {

    }
}
