package com.lujianbo.app.shadowsocks.common.crypto;

import org.bouncycastle.crypto.StreamCipher;

import java.security.SecureRandom;
import java.util.function.Supplier;

/**
 * Created by jianbo on 2017/3/25.
 */
public class ShadowSocksCrypto {

    private final String name;
    private final int keyLength;
    private final int ivLength;
    private final Supplier<StreamCipher> supplier;

    public ShadowSocksCrypto(String name, int keyLength, int ivLength, Supplier<StreamCipher> supplier) {
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

    public byte[] generatorIvParameter() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[ivLength];
        random.nextBytes(bytes);
        return bytes;
    }
}
