package com.ling.zookeeper.lock;

import com.ling.zookeeper.config.ZKUtils;
import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁-测试类
 *
 * @author zhangling 2021/3/29 14:09
 */
public class TestLock {

    ZooKeeper zooKeeper;

    @Before
    public void con() {
        zooKeeper = ZKUtils.getZK();
    }

    @After
    public void close() {
        try {
            zooKeeper.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void lock() {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {

                WatchCallBack watchCallBack = new WatchCallBack();
                watchCallBack.setZooKeeper(zooKeeper);
                String threadName = Thread.currentThread().getName();
                watchCallBack.setThreadName(threadName);

                // 1.获得锁
                watchCallBack.tryLock();
                // 2.执行业务代码
                System.out.println(threadName + "执行业务代码...");

                // 当业务代码执行的太快，不是第一个序列节点需要回调Watch监控前一个节点，当还没有监控完成前一个节点，就释放掉锁，删除了前一个序列节点，
                // 后面就不会触发删除节点事件，监控前一个节点回调不会执行，就不能获取到锁
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 3.释放锁
                watchCallBack.unLock();


            }).start();
        }

        while (true) {

        }

    }
}
