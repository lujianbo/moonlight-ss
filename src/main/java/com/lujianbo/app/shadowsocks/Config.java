package com.lujianbo.app.shadowsocks;

import org.yaml.snakeyaml.Yaml;

/**
 * Created by jianbo on 2017/3/25.
 */
public class Config {

    private String address;

    private int localPort;

    private int serverPort;

    private String method;

    private String password;

    public static Config readConfig() {
        Yaml yaml = new Yaml();
        return yaml.loadAs(Config.class.getClassLoader().getResourceAsStream("config.yaml"), Config.class);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getLocalPort() {
        return localPort;
    }

    public void setLocalPort(int localPort) {
        this.localPort = localPort;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
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
