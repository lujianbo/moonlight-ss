package com.lujianbo.app.shadowsocks.local.connector.socks;

import com.lujianbo.app.shadowsocks.common.codec.ShadowSocksAddressType;
import com.lujianbo.app.shadowsocks.common.codec.ShadowSocksRequest;
import com.lujianbo.app.shadowsocks.common.utils.NetUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.socks.*;

public class SocksRequestHandler extends SimpleChannelInboundHandler<SocksRequest> {

    @Override
    public void channelRead0(ChannelHandlerContext ctx, SocksRequest socksRequest) throws Exception {
        switch (socksRequest.requestType()) {
            case INIT:  //初始化
                ctx.pipeline().addFirst(new SocksCmdRequestDecoder());
                ctx.write(new SocksInitResponse(SocksAuthScheme.NO_AUTH));
                break;
            case AUTH://鉴权
                ctx.pipeline().addFirst(new SocksCmdRequestDecoder());
                ctx.write(new SocksAuthResponse(SocksAuthStatus.SUCCESS));
                break;
            case CMD:  //代理
                SocksCmdRequest req = (SocksCmdRequest) socksRequest;
                if (req.cmdType() == SocksCmdType.CONNECT) {
                    ctx.pipeline().remove(this);
                    //response ok
                    ctx.writeAndFlush(new SocksCmdResponse(SocksCmdStatus.SUCCESS, req.addressType())).sync();
                    ctx.pipeline().remove(SocksMessageEncoder.class);
                    ctx.fireChannelRead(buildShadowSocksRequest(req));
                } else {
                    ctx.close();
                }
                break;
            case UNKNOWN:
                ctx.close();
                break;
        }
    }

    private ShadowSocksRequest buildShadowSocksRequest(SocksCmdRequest request) {
        SocksAddressType addressType = request.addressType();
        String host = request.host();
        int port = request.port();
        ShadowSocksRequest shadowSocksRequest = null;
        switch (addressType) {
            case IPv4: {
                shadowSocksRequest = new ShadowSocksRequest(ShadowSocksAddressType.IPv4, host, port);
                break;
            }
            case DOMAIN: {
                shadowSocksRequest = new ShadowSocksRequest(ShadowSocksAddressType.hostname, host, port);
                break;
            }
            case IPv6: {
                shadowSocksRequest = new ShadowSocksRequest(ShadowSocksAddressType.IPv6, host, port);
                break;
            }
            case UNKNOWN:
                break;
        }
        return shadowSocksRequest;
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable throwable) {
        NetUtils.closeOnFlush(ctx.channel());
    }
}
