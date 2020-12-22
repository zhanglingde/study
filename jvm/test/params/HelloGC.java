package params;

import java.util.LinkedList;
import java.util.List;

/**
 * 测试参数：
 * -Xmn10M
 * -Xms40M
 * -Xmx60M
 * -XX:+PrintGCDetails
 *
 * JVM参数测试
 */
public class HelloGC {
    public static void main(String[] args) {
        System.out.println("HelloGC!");
        List list = new LinkedList();
        for (; ; ) {
            byte[] b = new byte[1024 * 1024];   // 每new 一个对象，堆内存占用1M
            list.add(b);
        }
    }
}