package io.xdd.blackscience.socksserver.local.connect;

import io.netty.channel.Channel;
import io.netty.handler.codec.socks.SocksAuthResponseDecoder;
import io.netty.handler.codec.socks.SocksCmdResponseDecoder;
import io.netty.handler.codec.socks.SocksInitResponseDecoder;

/**
 * socks协议在发送数据之前需要协商
 * */
public class SocksChannelWrapper implements ChannelWrapper{

    @Override
    public void wrapper(Channel channel) {
        channel.pipeline().addLast(new SocksInitResponseDecoder());
        channel.pipeline().addLast(new SocksAuthResponseDecoder());
        channel.pipeline().addLast(new SocksCmdResponseDecoder());
    }
}
