package com.lujianbo.app.shadowsocks.common.crypto;

import java.security.NoSuchAlgorithmException;

/**
 * Created by jianbo on 2017/3/25.
 */
public class ShadowSocksContext {

    private final String method;

    private final String password;

    private ShadowSocksCrypto crypto;

    public ShadowSocksContext(String method, String password) throws NoSuchAlgorithmException {
        this.method = method;
        this.password = password;
        this.crypto = ShadowSocksCiphersConstant.getShadowSocksCrypto(method);
        if (crypto == null) {
            throw new NoSuchAlgorithmException("no such Algorithm named " + method);
        }
    }

    public String getMethod() {
        return method;
    }

    public String getPassword() {
        return password;
    }

    public ShadowSocksCrypto getCrypto() {
        return crypto;
    }

    public void setCrypto(ShadowSocksCrypto crypto) {
        this.crypto = crypto;
    }

}
