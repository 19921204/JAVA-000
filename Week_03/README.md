**学习笔记**
--------------------
**main方法参数为:** -DproxyServer=http://localhost:8801,http://localhost:8802,http://localhost:8803

### 作业一 使用netty实现后端http访问

```java
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;

import java.net.URI;

public class NettyHttpClient {
    private NettyHttpClientOutboundHandler clientHandler = new NettyHttpClientOutboundHandler();

    public String execute(String url) throws Exception {
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    // 客户端接收到的是httpResponse响应，所以要使用HttpResponseDecoder进行解码
                    ch.pipeline().addLast(new HttpResponseDecoder());
                    // 客户端发送的是httpRequest，所以要使用HttpRequestEncoder进行编码
                    ch.pipeline().addLast(new HttpRequestEncoder());
                    ch.pipeline().addLast(clientHandler);
                }
            });

            URI uri = new URI(url);
            // Start the client.
            ChannelFuture f = b.connect(uri.getHost(), uri.getPort()).sync();

            // 构建http请求
            DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, url);
            request.headers().set(HttpHeaders.Names.HOST, uri.getHost());
            request.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
            request.headers().set(HttpHeaders.Names.CONTENT_LENGTH, request.content().readableBytes());

            f.channel().writeAndFlush(request);
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
        return clientHandler.getData();
    }
}
```

### 作业二 实现过滤器

```java
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
```

### 作业三 实现路由

```java
import java.util.List;
import java.util.Random;

/**
 * 随机访问路由
 *
 * @author 19921204
 * @date 2020/11/01
 */
public class RandomLoadBalancerRouter implements HttpEndpointRouter {
    private Random random = new Random();

    @Override
    public String route(List<String> endpoints) {
        int size = endpoints.size();
        int nextInt = random.nextInt(size * 100);
        int slot = nextInt % size;
        return endpoints.get(slot);
    }
}
```