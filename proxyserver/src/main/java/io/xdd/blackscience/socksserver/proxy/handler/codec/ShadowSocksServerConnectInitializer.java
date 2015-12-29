package io.xdd.blackscience.socksserver.proxy.handler.codec;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.xdd.blackscience.socksserver.crypto.AESCrypto;

import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class ShadowSocksServerConnectInitializer extends ChannelInitializer<SocketChannel> {

    private AESCrypto aesCrypto;

    private ShadowSocksRequest request;

    public ShadowSocksServerConnectInitializer(String password,ShadowSocksAddressType addressType, String host, short port) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException {
        this.aesCrypto=new AESCrypto(password);
        this.request=new ShadowSocksRequest(addressType,host,port);
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline p = socketChannel.pipeline();
        /**
         * 配置解密密钥
         * */
        p.addLast(new ShadowSocksDecoder(aesCrypto.getDecryptCipher()));

        /**
         * 配置加密密钥
         * */
        p.addLast(new ShadowSocksEncoder(request,aesCrypto.getEncryptCipher()));
    }
}
