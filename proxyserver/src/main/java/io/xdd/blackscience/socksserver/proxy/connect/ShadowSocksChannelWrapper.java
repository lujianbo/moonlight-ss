package io.xdd.blackscience.socksserver.proxy.connect;

import io.netty.channel.Channel;
import io.xdd.blackscience.socksserver.common.manager.SSServerInstance;
import io.xdd.blackscience.socksserver.proxy.handler.codec.SSDecoder;
import io.xdd.blackscience.socksserver.proxy.handler.codec.SSEncoder;
import io.xdd.blackscience.socksserver.proxy.handler.codec.ShadowSocksRequest;
import io.xdd.blackscience.socksserver.proxy.handler.codec.ShadowSocksRequestEncoder;

public class ShadowSocksChannelWrapper implements ChannelWrapper{

    private SSServerInstance instance;

    private ShadowSocksRequest request;

    public ShadowSocksChannelWrapper(ShadowSocksRequest request,SSServerInstance instance){
        this.instance=instance;
        this.request=request;
    }

    @Override
    public void wrapper(Channel channel) {
        channel.pipeline().addLast(new SSEncoder(instance.getMethod(),instance.getPassword()));

        channel.pipeline().addLast(new ShadowSocksRequestEncoder());

        channel.write(request);

        channel.pipeline().addLast(new SSDecoder(instance.getMethod(),instance.getPassword()));
    }
}
