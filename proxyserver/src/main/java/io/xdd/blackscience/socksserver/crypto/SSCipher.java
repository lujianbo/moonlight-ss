package io.xdd.blackscience.socksserver.crypto;

import org.apache.commons.codec.digest.DigestUtils;
import org.bouncycastle.crypto.StreamCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

import java.security.SecureRandom;

public class SSCipher {

    private StreamCipher streamCipher;

    private byte[] iv;

    private String method;

    private String password;

    private boolean forEncryption;

    public SSCipher(byte[] iv, String method, String password, boolean forEncryption) {
        this.iv = iv;
        this.method = method;
        this.password = password;
        this.forEncryption = forEncryption;
        SSCiphersConstant.SSCrypto ssCrypto= SSCiphersConstant.getSSCrypto(method);
        this.streamCipher=ssCrypto.getSupplier().get();
        streamCipher.init(forEncryption,new ParametersWithIV(new KeyParameter(EVP_BytesToKey(password,ssCrypto.getKeyLength())),iv));
    }

    public SSCipher(String method, String password, boolean forEncryption) {
        this(generatorIvParameter(SSCiphersConstant.getSSCrypto(method).getIvLength()),method,password,forEncryption);
    }

    public byte[] update(byte[] data){
        byte[] result=new byte[data.length];
        streamCipher.processBytes(data,0,data.length,result,0);
        return result;
    }

    public void update(byte[] in,byte[] out){
        streamCipher.processBytes(in,0,in.length,out,0);
    }

    public void update(byte[] in,int inOff,int len,byte[] out,int outOff){
        streamCipher.processBytes(in,inOff,len,out,outOff);
    }

    public StreamCipher getStreamCipher() {
        return streamCipher;
    }

    public byte[] getIv() {
        return iv;
    }

    public String getMethod() {
        return method;
    }

    public String getPassword() {
        return password;
    }


    /**
     * 返回随机IV
     * */
    public static byte[] generatorIvParameter(int length){
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[length];
        random.nextBytes(bytes);
        return bytes;
    }

    /**
     * MD5 迭代 password 直到 长度满足 key_len
     * */
    public static byte[] EVP_BytesToKey(String password,int length){
        byte[] passwordBytes=password.getBytes();
        byte[] temp = new byte[passwordBytes.length + 16];
        byte[] result=new byte[length];//32byte 的key
        int i=0;
        byte[] md5_sum=null;
        while(i<result.length){
            if (i==0){
                md5_sum= DigestUtils.md5(passwordBytes);//计算passwordBytes的md5
            }else{
                System.arraycopy(md5_sum,0,temp,0,md5_sum.length);//把md5_sum拷贝到temp中
                System.arraycopy(passwordBytes,0,temp,md5_sum.length,passwordBytes.length);//把passwordBytes追加到temp中
                md5_sum=DigestUtils.md5(temp);//对temp进行md5
            }
            //把md5_sum 拷贝到result中
            System.arraycopy(md5_sum,0,result,i,md5_sum.length);
            i+=md5_sum.length;
        }
        return result;
    }
}
