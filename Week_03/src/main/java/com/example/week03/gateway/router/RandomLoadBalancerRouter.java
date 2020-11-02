package com.example.week03.gateway.router;

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
