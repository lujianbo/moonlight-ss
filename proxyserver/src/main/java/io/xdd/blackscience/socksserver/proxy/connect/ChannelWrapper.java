package io.xdd.blackscience.socksserver.proxy.connect;

import io.netty.channel.Channel;

public interface ChannelWrapper {

    public void wrapper(Channel channel);
}
