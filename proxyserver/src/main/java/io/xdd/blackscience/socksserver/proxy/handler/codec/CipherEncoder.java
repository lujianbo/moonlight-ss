package io.xdd.blackscience.socksserver.proxy.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.xdd.blackscience.socksserver.crypto.AESCrypto;
import io.xdd.blackscience.socksserver.crypto.CryptoUtil;
import io.xdd.blackscience.socksserver.proxy.utils.BytebufCipherUtil;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * 对流过的数据都进行cipher 的 update操作
 * */
public class CipherEncoder extends ChannelOutboundHandlerAdapter {

    private transient  Cipher encryptCipher;

    private boolean iv_sent=false;

    private final int ivLength=16;
    private final int keyLength=32;

    private ShadowSocksRequest request;

    public CipherEncoder(String password,ShadowSocksRequest request){
        this.request=request;
        byte[] iv=CryptoUtil.generatorIvParameter(ivLength);
        try {
            this.encryptCipher = AESCrypto.getEncryptCipher(CryptoUtil.EVP_BytesToKey(password,keyLength),iv);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ByteBuf out=ctx.alloc().buffer();
        ByteBuf in=ctx.alloc().buffer();
        if (!iv_sent){
            iv_sent=true;
            out.writeBytes(encryptCipher.getIV());
            request.encodeAsByteBuf(in);
        }
        in.writeBytes((ByteBuf)msg);
        ByteBuf temp=ctx.alloc().buffer(in.capacity());
        BytebufCipherUtil.update(encryptCipher,in,temp);//update 解密
        out.writeBytes(temp);
        ctx.write(out);
    }

}
