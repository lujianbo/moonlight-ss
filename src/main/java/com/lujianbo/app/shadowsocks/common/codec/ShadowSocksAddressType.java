package com.lujianbo.app.shadowsocks.common.codec;


/**
 * 地址类型的协议标准规范
 */
public enum ShadowSocksAddressType {

    IPv4((byte) 0x01),
    IPv6((byte) 0x02),
    hostname((byte) 0x03),
    UNKNOWN((byte) 0xff);

    private final byte b;

    ShadowSocksAddressType(byte b) {
        this.b = b;
    }

    public static ShadowSocksAddressType valueOf(byte b) {
        for (ShadowSocksAddressType code : values()) {
            if (code.b == b) {
                return code;
            }
        }
        return UNKNOWN;
    }

    public byte byteValue() {
        return b;
    }

}
