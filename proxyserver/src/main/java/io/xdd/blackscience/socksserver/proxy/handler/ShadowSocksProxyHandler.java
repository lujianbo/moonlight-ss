package io.xdd.blackscience.socksserver.proxy.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.xdd.blackscience.socksserver.common.manager.SSServerInstance;
import io.xdd.blackscience.socksserver.proxy.handler.RelayHandler;
import io.xdd.blackscience.socksserver.proxy.handler.codec.SSDecoder;
import io.xdd.blackscience.socksserver.proxy.handler.codec.SSEncoder;
import io.xdd.blackscience.socksserver.proxy.handler.codec.ShadowSocksRequest;
import io.xdd.blackscience.socksserver.proxy.handler.codec.ShadowSocksRequestEncoder;

public class ShadowSocksProxyHandler extends ChannelInboundHandlerAdapter {

    /**
     * 需要桥接的Channel
     * */
    private Channel relayChannel;

    /**
     * 目标信息
     * */
    private SSServerInstance instance;

    private ShadowSocksRequest request;

    public ShadowSocksProxyHandler(Channel relayChannel,ShadowSocksRequest request,SSServerInstance instance){
        this.relayChannel=relayChannel;
        this.instance=instance;
        this.request=request;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext outboundChannel) throws Exception {

        outboundChannel.pipeline().addLast(new SSEncoder(instance.getMethod(),instance.getPassword()));

        outboundChannel.pipeline().addLast(new ShadowSocksRequestEncoder());

        outboundChannel.channel().write(request);

        relayChannel.pipeline().addLast(new RelayHandler(outboundChannel.channel()));

        outboundChannel.pipeline().addLast(new SSDecoder(instance.getMethod(),instance.getPassword()));

        outboundChannel.pipeline().addLast(new RelayHandler(relayChannel));
    }

}
