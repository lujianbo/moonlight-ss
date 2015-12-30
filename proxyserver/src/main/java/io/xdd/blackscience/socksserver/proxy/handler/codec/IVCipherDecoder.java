package io.xdd.blackscience.socksserver.proxy.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import javax.crypto.Cipher;
import java.util.List;


/**
 * IV 作为数据开头的加密处理
 * */
public class IVCipherDecoder extends ReplayingDecoder<Void> {

    private boolean isIV=true;

    private int ivLength=1;

    private Cipher decryptCipher;

    public IVCipherDecoder(int ivLength){

    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (isIV){
            isIV=false;
            ByteBuf iv=in.readBytes(ivLength);
            //构建解密器
        }else{
            //解密数据发往下一个位置
            //decryptCipher.update()
        }
    }
}
