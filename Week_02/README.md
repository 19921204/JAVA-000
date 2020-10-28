**学习笔记**
-------------
### 作业一 根据上述自己对于 1 和 2 的演示，写一段对于不同 GC 的总结
#### 1、串行GC
使用单个线程进行垃圾回收，**年轻代**使用**复制算法**，回收的效率没有并行gc高

#### 2、并行GC
使用cpu核心数的线程进行垃圾回收，相比串行gc用多个线程并行进行垃圾回收。**年轻代**的并行gc有两个都采用的是**复制算法**，一个是ParNew，它的特点是串行gc的多线程版本，另一个是Parallel Scavenge收集器，它相比ParNew的进步是自适应的参数配置，它是一个注重吞吐量的gc

#### 3、CMS
老年代收集器CMS是一个采用**标记清除算法**并且注重停顿时间的的收集器，适用于web服务器等注重请求低延迟的场景。它的特点是除了初始标记阶段和最终标记阶段外，其他阶段gc的回收线程和用户线程是并发进行的，所以cms能做到低停顿时间。cms并发标记时的线程数是cpu核心数的1/4

参数配置：
-XX:+UseConcMarkSweepGC 开启cms进行垃圾回收，默认新生代gc适用的是ParNew



#### 4、G1
G1是CMS的改进版本，为了实现高吞吐、减少内存碎片、停顿时间可控为目标来设计的。G1将整个堆默认分成2048个region，进行回收时会分析各个region回收的性价比，选择耗时最少，释放空间大的region进行回收




### 作业二 写一段代码，使用 HttpClient 或 OkHttp 访问 http://localhost:8801
```java
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HttpClientDemo {

    public static void main(String[] args) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://localhost:8801");
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200) {
                String content = EntityUtils.toString(response.getEntity(), "UTF-8");
                System.out.println(content);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                response.close();
            }
            httpclient.close();
        }
    }
}
```





