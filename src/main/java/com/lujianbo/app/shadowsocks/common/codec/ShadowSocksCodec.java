package com.lujianbo.app.shadowsocks.common.codec;

import com.lujianbo.app.shadowsocks.common.crypto.ShadowSocksContext;
import io.netty.channel.CombinedChannelDuplexHandler;

/**
 * Created by jianbo on 2017/3/25.
 */
public class ShadowSocksCodec extends CombinedChannelDuplexHandler<ShadowSocksDecoder, ShadowSocksEncoder> {

    private final ShadowSocksContext context;

    public ShadowSocksCodec(ShadowSocksContext context) {
        this.context = context;
        init(new ShadowSocksDecoder(context.getPassword(), context.getCrypto()),
                new ShadowSocksEncoder(context.getPassword(), context.getCrypto()));
    }


}
