package com.lujianbo.app.shadowsocks.local.manager;


/**
 * 服务器描述
 */
public class SSServerInstance {

    private String address;

    private int port;

    private String method;

    private String password;

    public SSServerInstance(String address, int port, String method, String password) {
        this.address = address;
        this.port = port;
        this.method = method;
        this.password = password;
    }

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
