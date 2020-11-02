package com.example.week03.gateway.inbound;

import com.example.week03.gateway.filter.HttpHeadersFilter;
import com.example.week03.gateway.filter.HttpRequestFilter;
import com.example.week03.gateway.outbound.httpclient4.HttpOutboundHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.ReferenceCountUtil;

public class HttpInboundHandler extends ChannelInboundHandlerAdapter {

    private HttpOutboundHandler outboundHandler;
    private HttpRequestFilter headersFilter;

    HttpInboundHandler(String proxyServer) {
        outboundHandler = new HttpOutboundHandler(proxyServer);
        headersFilter = new HttpHeadersFilter();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            FullHttpRequest fullRequest = (FullHttpRequest) msg;
            headersFilter.filter(fullRequest, ctx);
            outboundHandler.handle(fullRequest, ctx);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }
}
