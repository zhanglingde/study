package com.ling.zookeeper.config;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.concurrent.CountDownLatch;

/**
 * @author zhangling  2021/3/27 16:54
 */
public class DefaultWatch implements Watcher {

    private CountDownLatch latch ;

    public void setLatch(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void process(WatchedEvent watchedEvent) {

        System.out.println("触发watch回调事件：" + watchedEvent.toString());

        switch (watchedEvent.getState()) {

            case Unknown:
                break;
            case Disconnected:
                break;
            case NoSyncConnected:
                break;
            case SyncConnected:
                latch.countDown();          // 连接成功后放开阻塞
                break;
            case AuthFailed:
                break;
            case ConnectedReadOnly:
                break;
            case SaslAuthenticated:
                break;
            case Expired:
                break;
            case Closed:
                break;
        }
    }
}
