package com.example.week03.gateway.outbound.netty4;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpContent;

public class NettyHttpClientOutboundHandler extends ChannelInboundHandlerAdapter {
    private String data;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("Client " + ctx.channel().remoteAddress() + "connected");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof HttpContent) {
            HttpContent content = (HttpContent) msg;
            ByteBuf buf = content.content();
            data += buf.toString(io.netty.util.CharsetUtil.UTF_8);
            buf.release();
        }
    }

    String getData() {
        return data;
    }
}