package com.lujianbo.app.shadowsocks.common.codec;

import com.lujianbo.app.shadowsocks.common.codec.ShadowSocksRequestDecoder.State;
import com.lujianbo.app.shadowsocks.common.utils.ShadowUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import io.netty.util.CharsetUtil;

import java.util.List;

/**
 * 解码 request 数据
 */
public class ShadowSocksRequestDecoder extends ReplayingDecoder<State> {

    private ShadowSocksAddressType addressType;

    private String address;

    public ShadowSocksRequestDecoder() {
        super(State.READ_ADDRESSTYPE);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        switch (state()) {
            case READ_ADDRESSTYPE:
                byte type = in.readByte();
                addressType = ShadowSocksAddressType.valueOf(type);
                checkpoint(State.READ_ADDRESS);
            case READ_ADDRESS: {
                switch (addressType) {
                    case IPv4:
                        address = ShadowUtils.intToIp(in.readInt());
                        break;
                    case IPv6:
                        address = ShadowUtils.ipv6toStr(in.readBytes(16).array());
                        break;
                    case hostname:
                        byte length = in.readByte();
                        address = in.readBytes(length).toString(CharsetUtil.US_ASCII);
                        break;
                    case UNKNOWN:
                        break;
                }
                checkpoint(State.READ_PORT);
            }
            case READ_PORT:
                int port = in.readUnsignedShort();
                out.add(new ShadowSocksRequest(addressType, address, port));
                checkpoint(State.READ_DATA);
                break;
            case READ_DATA:
                if (super.actualReadableBytes()>0){
                    ByteBuf msg=in.readBytes(super.actualReadableBytes());
                    out.add(msg);
                }
                checkpoint(State.READ_DATA);
                break;
            default:
                break;
        }
    }

    enum State {
        READ_ADDRESSTYPE,
        READ_ADDRESS,
        READ_PORT,
        READ_DATA
    }
}
