package com.lujianbo.app.shadowsocks;

import com.lujianbo.app.shadowsocks.local.connector.socks.SocksServerInitializer;
import com.lujianbo.app.shadowsocks.server.handler.ShadowSocksServerInitializer;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * Created by jianbo on 2017/3/21.
 */
public class Application {

    public static void main(String[] args) {

        String mode=args[0]!=null?args[0]:"server";
        ChannelInitializer<SocketChannel> initializer = null;
        switch (mode){
            case "local":
                initializer=new SocksServerInitializer();
                break;
            case "server":
                initializer=new ShadowSocksServerInitializer();
                break;
            default:
                break;
        }
        if (initializer!=null){
            ProxyServer proxyServer = new ProxyServer(initializer);
            proxyServer.start();
        }
    }
}
