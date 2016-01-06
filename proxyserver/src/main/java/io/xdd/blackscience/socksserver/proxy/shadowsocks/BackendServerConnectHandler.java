/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package io.xdd.blackscience.socksserver.proxy.shadowsocks;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Promise;
import io.xdd.blackscience.socksserver.proxy.handler.PromiseHandler;
import io.xdd.blackscience.socksserver.proxy.handler.RelayHandler;
import io.xdd.blackscience.socksserver.proxy.handler.codec.ShadowSocksRequest;
import io.xdd.blackscience.socksserver.proxy.utils.SocksServerUtils;

@ChannelHandler.Sharable
public final class BackendServerConnectHandler extends SimpleChannelInboundHandler<ShadowSocksRequest> {

    private final Bootstrap b = new Bootstrap();

    @Override
    public void channelRead0(final ChannelHandlerContext ctx, final ShadowSocksRequest request) throws Exception {
        Promise<Channel> promise = ctx.executor().newPromise();
        promise.addListener(
                new GenericFutureListener<Future<Channel>>() {
                    @Override
                    public void operationComplete(Future<Channel> future) throws Exception {
                        final Channel outboundChannel = future.getNow();
                        if (future.isSuccess()){
                            ctx.pipeline().remove(BackendServerConnectHandler.this);
                            //交换数据
                            ctx.pipeline().addLast(new RelayHandler(outboundChannel));
                            outboundChannel.pipeline().addLast(new RelayHandler(ctx.channel()));
                        }else {
                            SocksServerUtils.closeOnFlush(ctx.channel());
                        }
                    }
                });


        final Channel inboundChannel = ctx.channel();

        b.group(inboundChannel.eventLoop())
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                .option(ChannelOption.SO_KEEPALIVE, true)
        .handler(new PromiseHandler(promise));

        //向目标端口发起连接
        b.connect(request.host(), request.port()).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                final Channel outboundChannel = future.channel();
                if (future.isSuccess()) {
                    ctx.pipeline().remove(BackendServerConnectHandler.this);

                    //两个 channel上的数据进行交换
                    outboundChannel.pipeline().addLast(new RelayHandler(ctx.channel()));

                    ctx.pipeline().addLast(new RelayHandler(outboundChannel));
                } else {
                    SocksServerUtils.closeOnFlush(ctx.channel());
                }
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        SocksServerUtils.closeOnFlush(ctx.channel());
    }
}
