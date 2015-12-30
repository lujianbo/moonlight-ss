package io.xdd.blackscience.socksserver.proxy.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public class ShadowSocksRequestEncoder extends ChannelOutboundHandlerAdapter {

    private ShadowSocksRequest request;

    public ShadowSocksRequestEncoder(ShadowSocksRequest request){
        this.request=request;
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ByteBuf temp = ctx.alloc().buffer(4);
        request.encodeAsByteBuf(temp);
        //该编码只执行一次，执行后将自身移除
        ctx.pipeline().remove(this);
        ctx.write(temp,promise);
    }
}
