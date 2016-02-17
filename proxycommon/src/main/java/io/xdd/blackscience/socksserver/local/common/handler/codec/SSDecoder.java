package io.xdd.blackscience.socksserver.local.common.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.xdd.blackscience.socksserver.local.common.crypto.SSCipher;
import io.xdd.blackscience.socksserver.local.common.crypto.SSCiphersConstant;
import io.xdd.blackscience.socksserver.local.common.utils.BytebufCipherUtil;

/**
 * IV 作为数据开头的加密处理
 * */
public class SSDecoder extends ChannelInboundHandlerAdapter {

    private transient SSCipher decryptCipher;

    private String password;

    private String method;

    private boolean readIV=false;

    private int ivLength=16;

    public SSDecoder(String method,String password) {
        this.password=password;
        this.method=method;
        this.ivLength= SSCiphersConstant.getSSCrypto(method).getIvLength();
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
               this.decryptCipher=new SSCipher(ivbytes,method,password,false);
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
