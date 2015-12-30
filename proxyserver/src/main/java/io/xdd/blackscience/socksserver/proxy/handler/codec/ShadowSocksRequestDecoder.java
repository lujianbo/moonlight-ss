package io.xdd.blackscience.socksserver.proxy.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import io.netty.util.CharsetUtil;
import io.xdd.blackscience.socksserver.proxy.handler.codec.ShadowSocksRequestDecoder.State;
import io.xdd.blackscience.socksserver.proxy.utils.ShadowUtils;
import java.util.List;

/**
 * 解码 request 数据
 * */
public class ShadowSocksRequestDecoder extends ReplayingDecoder<State> {

    private ShadowSocksAddressType addressType;

    private String address;

    private int port;

    public ShadowSocksRequestDecoder() {
        super(State.READ_ADDRESSTYPE);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        switch (state()) {
            case READ_ADDRESSTYPE:
                byte type=in.readByte();
                addressType=ShadowSocksAddressType.valueOf(type);
                checkpoint(State.READ_ADDRESS);
                break;
            case READ_ADDRESS:{
                switch (addressType){
                    case IPv4:
                        address= ShadowUtils.intToIp(in.readInt());
                        break;
                    case IPv6:
                        address=ShadowUtils.ipv6toStr(in.readBytes(16).array());
                        break;
                    case hostname:
                        byte length=in.readByte();
                        address=in.readBytes(length).toString(CharsetUtil.US_ASCII);
                        break;
                    case UNKNOWN:
                        break;
                }
                checkpoint(State.READ_PORT);
                break;
            }
            case READ_PORT:
                port=in.readUnsignedShort();
                break;
            default:
                break;
        }
        //该解码只执行一次，因此执行后将会移除自身
        ctx.pipeline().remove(this);
        out.add(new ShadowSocksRequest(addressType,address,port));
    }

    enum State {
        READ_ADDRESSTYPE,
        READ_ADDRESS,
        READ_PORT,
    }
}
