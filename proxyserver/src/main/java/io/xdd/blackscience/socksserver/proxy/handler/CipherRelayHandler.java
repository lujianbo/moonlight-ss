package io.xdd.blackscience.socksserver.proxy.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import io.xdd.blackscience.socksserver.proxy.utils.BytebufCipherUtil;
import io.xdd.blackscience.socksserver.proxy.utils.SocksServerUtils;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;

public final class CipherRelayHandler extends ChannelInboundHandlerAdapter {

    private final Channel relayChannel;

    private final Cipher cipher;

    public CipherRelayHandler(Channel relayChannel, Cipher cipher) {
        this.relayChannel = relayChannel;
        this.cipher=cipher;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (relayChannel.isActive()) {
            ByteBuf in= (ByteBuf) msg;
            ByteBuf out=ctx.alloc().buffer(in.capacity());
            BytebufCipherUtil.update(cipher,in,out);//update 解密
            relayChannel.writeAndFlush(out);//想relayChanel写入处理过的数据
        } else {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        if (relayChannel.isActive()) {
            SocksServerUtils.closeOnFlush(relayChannel);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
