package io.xdd.blackscience.socksserver.proxy.httpadapter;

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
            HttpRequest request=(HttpRequest)object;
            /**
             * 处理 Https代理
             * */
            if (request.getMethod().equals(HttpMethod.CONNECT)){
                ctx.pipeline().remove(HttpProxyHandler.this);//移除
                ctx.pipeline().addLast(new HttpsProxyConnectHandler());
                return;
            }

            /**
             * 替换
             * */
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
