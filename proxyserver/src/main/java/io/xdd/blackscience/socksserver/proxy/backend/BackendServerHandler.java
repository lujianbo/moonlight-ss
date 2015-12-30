package io.xdd.blackscience.socksserver.proxy.backend;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import io.xdd.blackscience.socksserver.proxy.handler.codec.CipherDecoder;
import io.xdd.blackscience.socksserver.proxy.handler.codec.CipherEncoder;
import io.xdd.blackscience.socksserver.proxy.handler.codec.ShadowSocksRequestDecoder;

import java.util.List;

@ChannelHandler.Sharable
public class BackendServerHandler extends ReplayingDecoder<Void> {

    private int ivLength;

    public BackendServerHandler(int ivLength){
        this.ivLength=ivLength;
    }
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (ivLength>0){
            byte[] iv=in.readBytes(ivLength).array();
        }
        ctx.pipeline().remove(this);
        //加解密
        ctx.pipeline().addLast(new CipherDecoder(null));
        ctx.pipeline().addLast(new CipherEncoder(null));
    }
}
