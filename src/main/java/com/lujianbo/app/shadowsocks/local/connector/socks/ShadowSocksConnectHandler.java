package com.lujianbo.app.shadowsocks.local.connector.socks;

import com.lujianbo.app.shadowsocks.common.codec.ShadowSocksAddressType;
import com.lujianbo.app.shadowsocks.common.codec.ShadowSocksRequest;
import com.lujianbo.app.shadowsocks.common.crypto.ShadowSocksContext;
import com.lujianbo.app.shadowsocks.common.handler.RelayHandler;
import com.lujianbo.app.shadowsocks.common.utils.NetUtils;
import com.lujianbo.app.shadowsocks.local.handler.ShadowSocksClientInitializer;
import com.lujianbo.app.shadowsocks.local.manager.ShadowSocksServerInfo;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.socks.*;

/**
 * 连接 shadowsocks 服务器
 */
public class ShadowSocksConnectHandler extends SimpleChannelInboundHandler<SocksCmdRequest> {


    private static EventLoopGroup executors = new NioEventLoopGroup();

    private ShadowSocksServerInfo instance;

    public ShadowSocksConnectHandler(ShadowSocksServerInfo instance) {
        this.instance = instance;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, SocksCmdRequest request) throws Exception {
        Bootstrap b = new Bootstrap();
        b.group(executors)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ShadowSocksClientInitializer(new ShadowSocksContext(instance.getMethod(), instance.getPassword())));

        //连接目标服务器
        b.connect(instance.getAddress(), instance.getPort())
                .addListener((ChannelFutureListener) future -> {
                    if (future.isSuccess()) {
                        future.channel().pipeline().addLast(new RelayHandler(ctx.channel()));
                        ctx.channel().writeAndFlush(new SocksCmdResponse(SocksCmdStatus.SUCCESS, request.addressType()))
                                .addListener(channelFuture -> {
                                    if (channelFuture.isSuccess()) {
                                        //移除当前的处理数据
                                        ctx.pipeline().remove(ShadowSocksConnectHandler.this);
                                        ctx.pipeline().remove(SocksMessageEncoder.class);
                                        //写入request
                                        ctx.pipeline().addLast(new RelayHandler(future.channel()));
                                        future.channel().write(buildShadowSocksRequest(request));
                                    } else {
                                        NetUtils.closeOnFlush(future.channel());
                                        NetUtils.closeOnFlush(ctx.channel());
                                    }
                                });
                    } else {
                        // Close the connection if the connection attempt has failed.
                        ctx.channel().writeAndFlush(new SocksCmdResponse(SocksCmdStatus.FAILURE, request.addressType()));
                        NetUtils.closeOnFlush(ctx.channel());
                    }
                });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        NetUtils.closeOnFlush(ctx.channel());
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
}
