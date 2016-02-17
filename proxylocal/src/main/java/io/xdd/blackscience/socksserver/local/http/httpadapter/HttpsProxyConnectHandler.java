package io.xdd.blackscience.socksserver.local.http.httpadapter;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Promise;
import io.xdd.blackscience.socksserver.local.common.handler.PromiseHandler;
import io.xdd.blackscience.socksserver.local.common.handler.RelayHandler;
import io.xdd.blackscience.socksserver.local.common.utils.SocksServerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

public class HttpsProxyConnectHandler extends SimpleChannelInboundHandler<HttpRequest> {

    private Logger logger= LoggerFactory.getLogger(getClass());

    private final Bootstrap b = new Bootstrap();

    /**
     * 通向目标的Channel
     * */
    private Channel outboundChannel;

    @Override
    public void channelRead0(final ChannelHandlerContext ctx, final HttpRequest request) throws Exception {
        Promise<Channel> promise = ctx.executor().newPromise();
        promise.addListener(
                new GenericFutureListener<Future<Channel>>() {
                    @Override
                    public void operationComplete(final Future<Channel> future) throws Exception {
                        final Channel outboundChannel = future.getNow();
                        if (future.isSuccess()) {
                            processSuccess(ctx,request,outboundChannel);
                        } else {
                            processFailed(ctx,request,outboundChannel);
                            SocksServerUtils.closeOnFlush(ctx.channel());
                        }
                    }
                });
        /**
         * 配置Handler
         * */
        final Channel inboundChannel = ctx.channel();
        b.group(inboundChannel.eventLoop())
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new PromiseHandler(promise));//把promise设置到Handler中来触发promise

        /**
         * 连接目标服务器,从request中获取
         * */
        URI uri=new URI(request.getUri());
        b.connect(uri.getHost(),uri.getPort()).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    //ctx.channel().writeAndFlush(new SocksCmdResponse(SocksCmdStatus.SUCCESS, request.addressType()));
                    // Connection established use handler provided results
                } else {
                    // Close the connection if the connection attempt has failed.
                    connectFailed(ctx,request);
                    SocksServerUtils.closeOnFlush(ctx.channel());
                }
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        SocksServerUtils.closeOnFlush(ctx.channel());
    }

    public void processSuccess(final ChannelHandlerContext ctx, final HttpRequest request,final Channel outboundChannel){
        ctx.channel().writeAndFlush(respondCONNECTSuccessful()).addListener(future -> {
            ctx.pipeline().remove(HttpsProxyConnectHandler.this);
            //移除原先的Decode 和 Encoder
            clean(ctx);
            //relay
            ctx.pipeline().addLast(new RelayHandler(outboundChannel));
            outboundChannel.pipeline().addLast(new RelayHandler(ctx.channel()));
        });

    }

    public void clean(final ChannelHandlerContext ctx){
        try{
            if (ctx.pipeline().get(HttpRequestDecoder.class)!=null){
                ctx.pipeline().remove(HttpRequestDecoder.class);
            }
            if (ctx.pipeline().get(HttpResponseEncoder.class)!=null){
                ctx.pipeline().remove(HttpResponseEncoder.class);
            }
        }catch (Exception ignored){}
    }

    public void processFailed(final ChannelHandlerContext ctx, final HttpRequest request,final Channel outboundChannel){

    }

    public void connectFailed(final ChannelHandlerContext ctx, final HttpRequest request){

    }

    public HttpResponse respondCONNECTSuccessful(){
        HttpResponse response=new DefaultHttpResponse(HttpVersion.HTTP_1_1, CONNECTION_ESTABLISHED);
        response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        response.headers().set("Proxy-Connection", HttpHeaders.Values.KEEP_ALIVE);
        return response;
    }

    private static final HttpResponseStatus CONNECTION_ESTABLISHED = new HttpResponseStatus(
            200, "HTTP/1.1 200 Connection established");
}
