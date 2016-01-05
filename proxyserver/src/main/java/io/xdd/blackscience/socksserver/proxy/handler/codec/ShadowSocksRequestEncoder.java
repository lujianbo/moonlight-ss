package io.xdd.blackscience.socksserver.proxy.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class ShadowSocksRequestEncoder extends MessageToByteEncoder<ShadowSocksRequest> {

    @Override
    protected void encode(ChannelHandlerContext ctx, ShadowSocksRequest request, ByteBuf out) throws Exception {
        request.encodeAsByteBuf(out);
    }
}
