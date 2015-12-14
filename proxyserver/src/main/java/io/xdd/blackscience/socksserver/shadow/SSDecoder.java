package io.xdd.blackscience.socksserver.shadow;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import io.xdd.blackscience.socksserver.domain.SSInitPackage;

import java.util.List;

public class SSDecoder extends ReplayingDecoder<SSInitPackage> {


    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

    }
}
