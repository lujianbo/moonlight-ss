package io.xdd.blackscience.socksserver.local.common.handler;


import io.netty.channel.Channel;

/**
 * 桥接器用于 串联两个Channel
 * */
public class BridgeHandler {


    public BridgeHandler(Channel inboundChannel,Channel outboundChannel){
        inboundChannel.pipeline().addLast(new RelayHandler(outboundChannel));
        outboundChannel.pipeline().addLast(new RelayHandler(inboundChannel));
    }
}
