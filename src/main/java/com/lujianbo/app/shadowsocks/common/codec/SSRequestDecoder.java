package com.lujianbo.app.shadowsocks.common.codec;

import com.lujianbo.app.shadowsocks.common.codec.ShadowSocksAddressType;
import com.lujianbo.app.shadowsocks.common.codec.ShadowSocksRequest;
import com.lujianbo.app.shadowsocks.common.handler.RelayHandler;
import com.lujianbo.app.shadowsocks.common.utils.ShadowUtils;
import com.lujianbo.app.shadowsocks.server.handler.ShadowSocksRequestHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

import java.io.IOException;

/**
 * Created by jianbo on 2017/3/25.
 */
public class SSRequestDecoder extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        handler(ctx,in);
    }

    private ShadowSocksAddressType addressType;
    private int addressLength=1;
    private void handler(ChannelHandlerContext ctx, ByteBuf in) throws IOException {
        if (addressType==null){
            int needToRead=1+addressLength+2;
            if (in.readableBytes()<needToRead){
                return;
            }
            byte type = in.readByte();
            addressType = ShadowSocksAddressType.valueOf(type);
            switch (addressType) {
                case IPv4:
                    addressLength=4;
                    break;
                case IPv6:
                    addressLength=16;
                    break;
                case hostname:
                    addressLength=in.readByte();
                    break;
                default:
                    throw new IOException("unknown addressType "+type);
            }
        }

        int needToRead=addressLength+2;
        if (in.readableBytes()<needToRead){
            return;
        }
        String address="";
        switch (addressType){
            case IPv4:
                address=ShadowUtils.intToIp(in.readInt());
                break;
            case IPv6:
                byte[] ipv6bytes=new byte[16];
                in.readBytes(ipv6bytes);
                address=ShadowUtils.ipv6toStr(ipv6bytes);
                break;
            case hostname:
                address = in.readBytes(addressLength).toString(CharsetUtil.US_ASCII);
                break;
            case UNKNOWN:
                break;
        }
        int port=in.readUnsignedShort();
        in.markReaderIndex();
        ctx.fireChannelRead(new ShadowSocksRequest(addressType, address, port));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
