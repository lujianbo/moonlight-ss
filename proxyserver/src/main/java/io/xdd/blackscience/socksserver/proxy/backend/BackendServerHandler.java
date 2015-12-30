package io.xdd.blackscience.socksserver.proxy.backend;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import io.xdd.blackscience.socksserver.crypto.AESCrypto;
import io.xdd.blackscience.socksserver.proxy.handler.codec.CipherDecoder;
import io.xdd.blackscience.socksserver.proxy.handler.codec.CipherEncoder;
import io.xdd.blackscience.socksserver.proxy.handler.codec.ShadowSocksRequestDecoder;

import java.util.List;

@ChannelHandler.Sharable
public class BackendServerHandler extends ReplayingDecoder<Void> {

    private String password;
    private int keyLength;
    private int ivLength;

    public BackendServerHandler(String password,int keyLength,int ivLength){
        this.ivLength=ivLength;
        this.keyLength=keyLength;
        this.password=password;
    }
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        byte[] iv=in.readBytes(ivLength).array();
        ctx.pipeline().remove(this);
        //加解密
        AESCrypto aesCrypto=new AESCrypto(password,keyLength,iv);
        ctx.pipeline().addLast(new CipherDecoder(aesCrypto.getDecryptCipher()));
        ctx.pipeline().addLast(new CipherEncoder(aesCrypto.getEncryptCipher()));
    }
}
