package com.lujianbo.app.shadowsocks;

import com.lujianbo.app.shadowsocks.common.crypto.ShadowSocksContext;
import com.lujianbo.app.shadowsocks.local.connector.socks.SocksServerInitializer;
import com.lujianbo.app.shadowsocks.local.manager.SSServerInstance;
import com.lujianbo.app.shadowsocks.server.handler.ShadowSocksServerInitializer;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * Created by jianbo on 2017/3/21.
 */
public class Application {

    public static void main(String[] args) {

        try {
            String mode = args[0] != null ? args[0] : "server";
            ChannelInitializer<SocketChannel> initializer = null;
            Config config = Config.readConfig();
            ProxyServer proxyServer = null;
            switch (mode) {
                case "local":
                    SSServerInstance instance = new SSServerInstance(config.getAddress(), config.getServerPort(), config.getMethod(), config.getPassword());
                    proxyServer = new ProxyServer(config.getLocalPort(), new SocksServerInitializer(instance));
                    break;
                case "server":
                    ShadowSocksContext context = new ShadowSocksContext(config.getMethod(), config.getPassword());
                    proxyServer = new ProxyServer(config.getServerPort(), new ShadowSocksServerInitializer(context));
                    break;
                default:
                    break;
            }
            if (proxyServer != null) {
                proxyServer.start();
                Runtime.getRuntime().addShutdownHook(new Thread(proxyServer::stop));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
