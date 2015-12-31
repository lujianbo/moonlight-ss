package io.xdd.blackscience.socksserver.proxy.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.ReplayingDecoder;
import io.xdd.blackscience.socksserver.crypto.AESCrypto;
import io.xdd.blackscience.socksserver.crypto.CryptoUtil;
import io.xdd.blackscience.socksserver.proxy.utils.BytebufCipherUtil;

import javax.crypto.Cipher;
import java.util.List;


/**
 * IV 作为数据开头的加密处理
 * */
public class CipherDecoder extends ChannelInboundHandlerAdapter {

    private transient Cipher decryptCipher;

    private String password;

    private boolean readIV=false;

    private final int ivLength=16;

    private final int keyLength=32;

    public CipherDecoder(String password) {
        this.password=password;
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in=(ByteBuf)msg;
        if (!readIV){
           if (in.readableBytes()<ivLength){
               return;
           }else{
               readIV=true;
               ByteBuf iv=in.readBytes(ivLength);
               byte[] ivbytes=new byte[ivLength];
               iv.getBytes(0,ivbytes);
               this.decryptCipher=AESCrypto.getDecryptCipher(CryptoUtil.EVP_BytesToKey(password,keyLength),ivbytes);
           }
        }
        int length=in.readableBytes();
        if (length>0){
            ByteBuf en= in.readBytes(length);
            ByteBuf real=ctx.alloc().buffer();
            ByteBuf temp=ctx.alloc().buffer(en.capacity());
            BytebufCipherUtil.update(decryptCipher,en,temp);//update 解密
            real.writeBytes(temp);
            ctx.fireChannelRead(real);
        }
    }

}
