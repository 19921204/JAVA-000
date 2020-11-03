package com.example.week03.gateway.outbound.httpclient4;


import com.example.week03.gateway.outbound.netty4.NettyHttpClient;
import com.example.week03.gateway.router.HttpEndpointRouter;
import com.example.week03.gateway.router.RandomLoadBalancerRouter;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class HttpOutboundHandler {
    private ExecutorService proxyService;
    private String backendUrl;
    private HttpEndpointRouter router;

    public HttpOutboundHandler(String backendUrl) {
        this.backendUrl = backendUrl;
        int cores = Runtime.getRuntime().availableProcessors() * 2;
        long keepAliveTime = 1000;
        int queueSize = 2048;
        RejectedExecutionHandler handler = new ThreadPoolExecutor.CallerRunsPolicy();
        proxyService = new ThreadPoolExecutor(cores, cores,
                keepAliveTime, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(queueSize),
                new NamedThreadFactory("proxyService"), handler);
        router = new RandomLoadBalancerRouter();
    }

    public void handle(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx) {
        route(fullRequest);
        proxyService.submit(() -> fetchGet(fullRequest, ctx));
    }

    private void route(final FullHttpRequest fullRequest) {
        List<String> backendUrls = Stream.of(this.backendUrl.split(",")).collect(Collectors.toList());
        String host = router.route(backendUrls);
        fullRequest.setUri(host + fullRequest.uri());
    }

    private void fetchGet(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx) {
        try {
            NettyHttpClient client = new NettyHttpClient();
            String body = client.execute(fullRequest);
            handleResponse(fullRequest, ctx, body);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleResponse(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx, final String body) {
        FullHttpResponse response = null;
        try {
            byte[] bytes = body.getBytes();
            response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(bytes));
            response.headers().set("Content-Type", "application/json");
            response.headers().setInt("Content-Length", bytes.length);
        } catch (Exception e) {
            e.printStackTrace();
            response = new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.NO_CONTENT);
            exceptionCaught(ctx, e);
        } finally {
            if (fullRequest != null) {
                if (!HttpUtil.isKeepAlive(fullRequest)) {
                    ctx.write(response).addListener(ChannelFutureListener.CLOSE);
                } else {
                    ctx.write(response);
                }
            }
            ctx.flush();
        }
    }

    private void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }


}
