package io.xdd.blackscience.socksserver.core.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class ShadowSocksEncoder extends MessageToByteEncoder<ShadowSocksMessage> {


    @Override
    protected void encode(ChannelHandlerContext ctx, ShadowSocksMessage msg, ByteBuf out) throws Exception {

    }
}
