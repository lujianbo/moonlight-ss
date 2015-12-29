package io.xdd.blackscience.socksserver.core.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class ShadowSocksEncoder extends MessageToByteEncoder<ShadowSocksMessage> {

    /**
     * 地址类型
     * */
    private byte addressType;

    /**
     * 地址,IPv4  4byte
     * ipV6 16byte
     * hostname 第一个 byte 是长度
     * */
    private byte[] address;

    /**
     * 目标端口
     * */
    private short destinationPort;



    public ShadowSocksEncoder(byte addressType, byte[] address, short destinationPort) {
        this.addressType = addressType;
        this.address = address;
        this.destinationPort = destinationPort;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ShadowSocksMessage msg, ByteBuf out) throws Exception {

    }
}
