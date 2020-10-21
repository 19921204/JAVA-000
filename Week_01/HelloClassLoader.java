import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * 自定义类加载器加载Hello.xlass文件
 *
 * @author 19921204
 * @date 2020/10/21
 */
public class HelloClassLoader extends ClassLoader {

    public static void main(String[] args) throws Exception {
        Class<?> hello = new HelloClassLoader().findClass("Hello");
        Method helloMethod = hello.getMethod("hello");
        helloMethod.invoke(hello.newInstance());
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        File file = new File("Week_01/Hello.xlass");
        byte[] buffer = new byte[(int) file.length()];
        FileInputStream fs = null;
        try {
            fs = new FileInputStream(file);
            int n;
            int offset = 0;
            while ((n = fs.read()) != -1) {
                buffer[offset++] = (byte) (255 - n);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return defineClass(name, buffer, 0, buffer.length);
    }
}
