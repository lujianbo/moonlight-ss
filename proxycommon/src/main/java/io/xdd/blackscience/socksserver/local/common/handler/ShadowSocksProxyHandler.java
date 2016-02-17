package io.xdd.blackscience.socksserver.local.common.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.xdd.blackscience.socksserver.local.common.handler.codec.SSDecoder;
import io.xdd.blackscience.socksserver.local.common.handler.codec.SSEncoder;
import io.xdd.blackscience.socksserver.local.common.handler.codec.ShadowSocksRequest;
import io.xdd.blackscience.socksserver.local.common.handler.codec.ShadowSocksRequestEncoder;
import io.xdd.blackscience.socksserver.local.common.handler.manager.SSServerInstance;

public class ShadowSocksProxyHandler extends ChannelInboundHandlerAdapter {

    /**
     * 目标信息
     * */
    private SSServerInstance instance;

    private ShadowSocksRequest request;

    public ShadowSocksProxyHandler(ShadowSocksRequest request,SSServerInstance instance){
        this.instance=instance;
        this.request=request;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext outboundChannel) throws Exception {

        outboundChannel.pipeline().addLast(new SSEncoder(instance.getMethod(),instance.getPassword()));

        outboundChannel.pipeline().addLast(new ShadowSocksRequestEncoder());

        outboundChannel.channel().write(request);

        outboundChannel.pipeline().addLast(new SSDecoder(instance.getMethod(),instance.getPassword()));

    }

}
