package io.xdd.blackscience.socksserver.proxy.handler.codec;

public class ShadowSocksMessage {

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


    private byte[] data;


    public byte getAddressType() {
        return addressType;
    }

    private void setAddressType(byte addressType) {
        this.addressType = addressType;
    }

    public byte[] getAddress() {
        return address;
    }

    public void setIpv4Address(byte[] ipv4Address) throws IllegalArgumentException {
        if (ipv4Address.length==4){
            setAddressType(ShadowSocksAddressTypeScheme.IPv4.byteValue());
            this.address=ipv4Address;
        }else {
            throw new IllegalArgumentException("ipv4Address.length must 4");
        }
    }

    public void setIpv6Address(byte[] ipv6Address){
        if (ipv6Address.length==16){
            setAddressType(ShadowSocksAddressTypeScheme.IPv6.byteValue());
            this.address=ipv6Address;
        }else {
            throw new IllegalArgumentException("ipv6Address.length must 16");
        }
    }

    public void setHostnameAddress(byte[] hostnameAddress){
        if (hostnameAddress.length>0){
            setAddressType(ShadowSocksAddressTypeScheme.hostname.byteValue());
            this.address=hostnameAddress;
        }else {
            throw new IllegalArgumentException("hostnameAddress.length must >0");
        }
    }

    public short getDestinationPort() {
        return destinationPort;
    }

    public void setDestinationPort(short destinationPort) {
        this.destinationPort = destinationPort;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
