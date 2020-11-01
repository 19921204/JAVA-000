package com.example.week03.gateway.filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * 网关过滤器（添加自定义请求头）
 *
 * @author 19921204
 * @date 2020/11/01
 */
public class HttpHeadersFilter implements HttpRequestFilter {
    @Override
    public void filter(FullHttpRequest fullRequest, ChannelHandlerContext ctx) {
        fullRequest.headers().add("nio", "19921204");
    }
}
