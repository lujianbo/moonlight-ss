package com.lujianbo.app.shadowsocks.common.codec;

import com.lujianbo.app.shadowsocks.common.utils.ShadowUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.util.CharsetUtil;

import java.io.IOException;

/**
 * Created by jianbo on 2017/3/25.
 */
public class ShadowSocksRequestDecoder extends ChannelInboundHandlerAdapter {

    private ShadowSocksAddressType addressType;
    private int addressLength=1;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
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
        ctx.fireChannelRead(new ShadowSocksRequest(addressType, address, port));
        ctx.pipeline().remove(ShadowSocksRequestDecoder.this);
        ctx.fireChannelRead(in);//fire last data
    }



    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
