package com.lujianbo.app.shadowsocks.common.crypto;

import org.bouncycastle.crypto.engines.*;
import org.bouncycastle.crypto.modes.CFBBlockCipher;
import org.bouncycastle.crypto.modes.OFBBlockCipher;
import org.bouncycastle.crypto.modes.SICBlockCipher;

import java.util.HashMap;
import java.util.Map;


/**
 * shadow socks 支持的加解密算法
 */
public class ShadowSocksCiphersConstant {
    /**
     * 定义加密的类型方法
     */
    private static ShadowSocksCrypto[] ShadowSocksCryptos = {
            new ShadowSocksCrypto("aes-128-cfb", 16, 16, () -> new CFBBlockCipher(new AESEngine(), 128)),
            new ShadowSocksCrypto("aes-192-cfb", 24, 16, () -> new CFBBlockCipher(new AESEngine(), 128)),
            new ShadowSocksCrypto("aes-256-cfb", 32, 16, () -> new CFBBlockCipher(new AESEngine(), 128)),
            new ShadowSocksCrypto("aes-128-cfb8", 16, 16, () -> new CFBBlockCipher(new AESEngine(), 8)),
            new ShadowSocksCrypto("aes-192-cfb8", 24, 16, () -> new CFBBlockCipher(new AESEngine(), 8)),
            new ShadowSocksCrypto("aes-256-cfb8", 32, 16, () -> new CFBBlockCipher(new AESEngine(), 8)),
            new ShadowSocksCrypto("aes-128-cfb1", 16, 16, () -> new CFBBlockCipher(new AESEngine(), 1)),
            new ShadowSocksCrypto("aes-192-cfb1", 24, 16, () -> new CFBBlockCipher(new AESEngine(), 1)),
            new ShadowSocksCrypto("aes-256-cfb1", 32, 16, () -> new CFBBlockCipher(new AESEngine(), 1)),
            new ShadowSocksCrypto("aes-128-ofb", 16, 16, () -> new OFBBlockCipher(new AESEngine(), 128)),
            new ShadowSocksCrypto("aes-192-ofb", 24, 16, () -> new OFBBlockCipher(new AESEngine(), 128)),
            new ShadowSocksCrypto("aes-256-ofb", 32, 16, () -> new OFBBlockCipher(new AESEngine(), 128)),
            new ShadowSocksCrypto("aes-128-ctr", 16, 16, () -> new SICBlockCipher(new AESEngine())),
            new ShadowSocksCrypto("aes-192-ctr", 24, 16, () -> new SICBlockCipher(new AESEngine())),
            new ShadowSocksCrypto("aes-256-ctr", 32, 16, () -> new SICBlockCipher(new AESEngine())),
            new ShadowSocksCrypto("bf-cfb", 16, 8, () -> new CFBBlockCipher(new BlowfishEngine(), 128)),
            new ShadowSocksCrypto("camellia-128-cfb", 16, 16, () -> new CFBBlockCipher(new CamelliaEngine(), 128)),
            new ShadowSocksCrypto("camellia-192-cfb", 24, 16, () -> new CFBBlockCipher(new CamelliaEngine(), 128)),
            new ShadowSocksCrypto("camellia-256-cfb", 32, 16, () -> new CFBBlockCipher(new CamelliaEngine(), 128)),
            new ShadowSocksCrypto("cast5-cfb", 16, 8, () -> new CFBBlockCipher(new CAST5Engine(), 128)),
            new ShadowSocksCrypto("des-cfb", 8, 8, () -> new CFBBlockCipher(new DESEngine(), 128)),
            new ShadowSocksCrypto("idea-cfb", 16, 8, () -> new CFBBlockCipher(new IDEAEngine(), 128)),
            new ShadowSocksCrypto("rc2-cfb", 16, 8, () -> new CFBBlockCipher(new RC2Engine(), 128)),
            new ShadowSocksCrypto("rc4", 16, 0, RC4Engine::new),
            new ShadowSocksCrypto("seed-cfb", 16, 16, () -> new CFBBlockCipher(new SEEDEngine(), 128)),
    };

    /**
     * 用Map来作为快速查找的方式
     */
    private static Map<String, ShadowSocksCrypto> CIPHERS = new HashMap<>();

    static {
        for (ShadowSocksCrypto ShadowSocksCrypto : ShadowSocksCryptos) {
            CIPHERS.put(ShadowSocksCrypto.getName(), ShadowSocksCrypto);
        }
    }

    /**
     * 判断是否存在该加密方法
     */
    public static boolean containsMethod(String method) {
        return CIPHERS.containsKey(method);
    }

    public static ShadowSocksCrypto getShadowSocksCrypto(String method) {
        return CIPHERS.get(method);
    }


}
