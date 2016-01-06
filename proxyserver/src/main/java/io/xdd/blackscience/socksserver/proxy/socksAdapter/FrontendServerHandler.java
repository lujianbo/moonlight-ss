package io.xdd.blackscience.socksserver.proxy.socksadapter;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.socks.*;
import io.xdd.blackscience.socksserver.proxy.utils.SocksServerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ChannelHandler.Sharable
public class FrontendServerHandler extends SimpleChannelInboundHandler<SocksRequest> {

    private Logger logger= LoggerFactory.getLogger(getClass());

    @Override
    public void channelRead0(ChannelHandlerContext ctx, SocksRequest socksRequest) throws Exception {
        switch (socksRequest.requestType()) {
            case INIT: { //初始化
                // auth support example
                //ctx.pipeline().addFirst(new SocksAuthRequestDecoder());
                //ctx.write(new SocksInitResponse(SocksAuthScheme.AUTH_PASSWORD));
                logger.info("init");
                ctx.pipeline().addFirst(new SocksCmdRequestDecoder());
                ctx.write(new SocksInitResponse(SocksAuthScheme.NO_AUTH));
                break;
            }
            case AUTH://鉴权
                ctx.pipeline().addFirst(new SocksCmdRequestDecoder());
                ctx.write(new SocksAuthResponse(SocksAuthStatus.SUCCESS));
                break;

            case CMD:  //代理
                SocksCmdRequest req = (SocksCmdRequest) socksRequest;
                if (req.cmdType() == SocksCmdType.CONNECT) {
                    /**
                     * 切换进入代理模式
                     * */
                    logger.info("cmd");
                    ctx.pipeline().addLast(new FrontendServerConnectHandler());
                    ctx.pipeline().remove(this);
                    /**
                     * 然后再fire继续处理
                     * */
                    ctx.fireChannelRead(socksRequest);
                } else {
                    ctx.close();
                }
                break;
            case UNKNOWN:
                ctx.close();
                break;
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable throwable) {
        throwable.printStackTrace();
        SocksServerUtils.closeOnFlush(ctx.channel());
    }
}
