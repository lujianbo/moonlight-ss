package io.xdd.blackscience.socksserver.proxy.httpproxy;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

public class HttpProxyHandler extends SimpleChannelInboundHandler<HttpObject>{

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject object) throws Exception {

        /**
         * 正常的request
         * */
        if (object instanceof HttpRequest){

        }

        /**
         * 含有 chunked 的request
         * */
        if (object instanceof HttpContent){

        }

        if (object instanceof LastHttpContent){

        }
    }
}
