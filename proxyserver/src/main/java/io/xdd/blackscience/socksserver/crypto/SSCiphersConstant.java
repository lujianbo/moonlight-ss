package io.xdd.blackscience.socksserver.crypto;

import org.bouncycastle.crypto.StreamCipher;
import org.bouncycastle.crypto.engines.*;
import org.bouncycastle.crypto.modes.CFBBlockCipher;
import org.bouncycastle.crypto.modes.OFBBlockCipher;
import org.bouncycastle.crypto.modes.SICBlockCipher;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SSCiphersConstant {
    /**
     * 定义加密的类型方法
     */
    private static SSCrypto[] ssCryptos = {
            new SSCrypto("aes-128-cfb", 16, 16, () -> new CFBBlockCipher(new AESEngine(), 128)),
            new SSCrypto("aes-192-cfb", 24, 16, () -> new CFBBlockCipher(new AESEngine(), 128)),
            new SSCrypto("aes-256-cfb", 32, 16, () -> new CFBBlockCipher(new AESEngine(), 128)),
            new SSCrypto("aes-128-cfb8", 16, 16, () -> new CFBBlockCipher(new AESEngine(), 8)),
            new SSCrypto("aes-192-cfb8", 24, 16, () -> new CFBBlockCipher(new AESEngine(), 8)),
            new SSCrypto("aes-256-cfb8", 32, 16, () -> new CFBBlockCipher(new AESEngine(), 8)),
            new SSCrypto("aes-128-cfb1", 16, 16, () -> new CFBBlockCipher(new AESEngine(), 1)),
            new SSCrypto("aes-192-cfb1", 24, 16, () -> new CFBBlockCipher(new AESEngine(), 1)),
            new SSCrypto("aes-256-cfb1", 32, 16, () -> new CFBBlockCipher(new AESEngine(), 1)),
            new SSCrypto("aes-128-ofb", 16, 16, () -> new OFBBlockCipher(new AESEngine(), 128)),
            new SSCrypto("aes-192-ofb", 24, 16, () -> new OFBBlockCipher(new AESEngine(), 128)),
            new SSCrypto("aes-256-ofb", 32, 16, () -> new OFBBlockCipher(new AESEngine(), 128)),
            new SSCrypto("aes-128-ctr", 16, 16, () -> new SICBlockCipher(new AESEngine())),
            new SSCrypto("aes-192-ctr", 24, 16, () -> new SICBlockCipher(new AESEngine())),
            new SSCrypto("aes-256-ctr", 32, 16, () -> new SICBlockCipher(new AESEngine())),
            new SSCrypto("bf-cfb", 16, 8, () -> new CFBBlockCipher(new BlowfishEngine(), 128)),
            new SSCrypto("camellia-128-cfb", 16, 16, () -> new CFBBlockCipher(new CamelliaEngine(), 128)),
            new SSCrypto("camellia-192-cfb", 24, 16, () -> new CFBBlockCipher(new CamelliaEngine(), 128)),
            new SSCrypto("camellia-256-cfb", 32, 16, () -> new CFBBlockCipher(new CamelliaEngine(), 128)),
            new SSCrypto("cast5-cfb", 16, 8, () -> new CFBBlockCipher(new CAST5Engine(), 128)),
            new SSCrypto("des-cfb", 8, 8, () -> new CFBBlockCipher(new DESEngine(), 128)),
            new SSCrypto("idea-cfb", 16, 8, () -> new CFBBlockCipher(new IDEAEngine(), 128)),
            new SSCrypto("rc2-cfb", 16, 8, () -> new CFBBlockCipher(new RC2Engine(), 128)),
            new SSCrypto("rc4", 16, 0, RC4Engine::new),
            new SSCrypto("seed-cfb", 16, 16, () -> new CFBBlockCipher(new SEEDEngine(), 128)),
    };

    /**
     * 用Map来作为快速查找的方式
     */
    private static Map<String, SSCrypto> CIPHERS = new HashMap<>();

    static {
        for (SSCrypto ssCrypto : ssCryptos) {
            CIPHERS.put(ssCrypto.name, ssCrypto);
        }
    }

    public static class SSCrypto {
        private final String name;
        private final int keyLength;
        private final int ivLength;
        private final Supplier<StreamCipher> supplier;

        public SSCrypto(String name, int keyLength, int ivLength, Supplier<StreamCipher> supplier) {
            this.name = name;
            this.keyLength = keyLength;
            this.ivLength = ivLength;
            this.supplier = supplier;
        }

        public String getName() {
            return name;
        }

        public int getKeyLength() {
            return keyLength;
        }

        public int getIvLength() {
            return ivLength;
        }

        public Supplier<StreamCipher> getSupplier() {
            return supplier;
        }
    }

    /**
     * 判断是否存在该加密方法
     */
    public static boolean containsMethod(String method) {
        return CIPHERS.containsKey(method);
    }

    public static SSCrypto getSSCrypto(String method) {
        return CIPHERS.get(method);
    }


}
