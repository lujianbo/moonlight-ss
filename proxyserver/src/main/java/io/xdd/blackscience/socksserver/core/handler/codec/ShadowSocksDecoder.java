package io.xdd.blackscience.socksserver.core.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import io.xdd.blackscience.socksserver.core.handler.codec.ShadowSocksDecoder.State;

import java.util.List;

public class ShadowSocksDecoder extends ReplayingDecoder<State> {


    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

    }

    enum State {

    }
}
