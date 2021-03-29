package com.ling.zookeeper.config;

import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * 连接zookeeper工具类
 * @author zhangling  2021/3/27 16:37
 */
public class ZKUtils {

    private static ZooKeeper zk;

    // 以testConf为当前Client连接的根目录,可能多个项目公用一个zookeeper，所以不同的服务在不同的目录下工作
     private static String address = "192.168.191.128:2181,192.168.191.129:2181,192.168.191.130:2181,192.168.191.132:2181/testLock";
    //private static String address = "192.168.164.132:2181,192.168.164.133:2181,192.168.164.134:2181,192.168.164.135:2181/testConf";

    /**
     * 默认new Zookeeper时注册的Watch
     * 当监控到zookeeper连接成功后，放开阻塞
     */
    private static DefaultWatch watch = new DefaultWatch();

    private static CountDownLatch init = new CountDownLatch(1);

    public static ZooKeeper getZK(){
        try {
            zk = new ZooKeeper(address, 1000, watch);

            watch.setLatch(init);
            init.await();           // zk没有连接成功就阻塞住，直到Watch中连接成功后放开阻塞
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return zk;
    }
}
