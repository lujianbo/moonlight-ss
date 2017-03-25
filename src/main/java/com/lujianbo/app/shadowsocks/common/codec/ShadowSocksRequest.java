package com.lujianbo.app.shadowsocks.common.codec;

import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;
import io.netty.util.NetUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ShadowSocksRequest {

    /**
     * 地址类型
     */
    private ShadowSocksAddressType addressType;

    /**
     * 地址,IPv4  4byte
     * ipV6 16byte
     * hostname 第一个 byte 是长度
     */
    private String host;

    /**
     * 目标端口
     */
    private int port;

    public ShadowSocksRequest(ShadowSocksAddressType addressType, String host, int port) {
        this.addressType = addressType;
        this.host = host;
        this.port = port;
    }

    public byte[] encodeToBytes() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(addressType.byteValue());
        switch (addressType) {
            case IPv4:
                byteArrayOutputStream.write(NetUtil.createByteArrayFromIpAddressString(host));
                break;
            case hostname:
                byteArrayOutputStream.write((byte) host.length());
                byteArrayOutputStream.write(host.getBytes(CharsetUtil.US_ASCII));
                break;
            case IPv6:
                byteArrayOutputStream.write(NetUtil.createByteArrayFromIpAddressString(host));
                break;
            default:
                break;
        }
        byteArrayOutputStream.write((short) port);
        return byteArrayOutputStream.toByteArray();
    }

    public void encodeAsByteBuf(ByteBuf byteBuf) {
        byteBuf.writeByte(addressType.byteValue());
        switch (addressType) {
            case IPv4:
                byteBuf.writeBytes(NetUtil.createByteArrayFromIpAddressString(host));
                byteBuf.writeShort(port);
                break;
            case hostname:
                byteBuf.writeByte(host.length());
                byteBuf.writeBytes(host.getBytes(CharsetUtil.US_ASCII));
                byteBuf.writeShort(port);
                break;
            case IPv6:
                byteBuf.writeBytes(NetUtil.createByteArrayFromIpAddressString(host));
                byteBuf.writeShort(port);
                break;
            default:
                break;
        }
    }

    public String host() {
        return host;
    }

    public int port() {
        return port;
    }
}
