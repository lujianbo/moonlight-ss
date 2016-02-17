package io.xdd.blackscience.socksserver.local.connect;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class ConnectBootstrap {

    private final Bootstrap b = new Bootstrap();

    public ConnectBootstrap(EventLoopGroup group){
        b.group(group)
            .channel(NioSocketChannel.class)
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
            .option(ChannelOption.SO_KEEPALIVE, true);
            //.handler(new PromiseHandler(promise));//把promise设置到Handler中来触发promise
    }

    public ChannelFuture Connect(){
        return b.connect();
    }
}
