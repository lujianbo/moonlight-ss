package io.xdd.blackscience.socksserver.common;


/**
 * 服务器描述
 * */
public class ShadowSocksServerInstance {

    private String address;

    private int port;

    private String method;

    private String password;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
