package com.ling.zookeeper.lock;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 分布式锁 Watch回调
 * @author zhangling 2021/3/29 14:53
 */
public class WatchCallBack implements Watcher, AsyncCallback.StringCallback, AsyncCallback.Children2Callback, AsyncCallback.StatCallback {

    private ZooKeeper zooKeeper;
    private CountDownLatch latch = new CountDownLatch(1);
    private String threadName;      // 线程名，用做锁的名称
    private String pathName;

    public ZooKeeper getZooKeeper() {
        return zooKeeper;
    }

    public void setZooKeeper(ZooKeeper zooKeeper) {
        this.zooKeeper = zooKeeper;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public CountDownLatch getLatch() {
        return latch;
    }

    public void setLatch(CountDownLatch latch) {
        this.latch = latch;
    }


    /**
     * 获取分布式锁
     * 创建一个临时带序列的节点，
     */
    public void tryLock() {
        try {
            System.out.println("create...");
            //在第一个节点获取到锁的时候设置数据，后续获取这个数据，对其进行判断，可以设置重入锁
            // if(zk.getData("/")
            zooKeeper.create("/lock", threadName.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL, this, "context");
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 释放分布式锁
     * 删除节点
     */
    public void unLock() {
        try {
            zooKeeper.delete(pathName,-1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    /**
     * Watch回调，监控节点事件
     *
     * @param event
     */
    @Override
    public void process(WatchedEvent event) {

        /**
         *  如果是第一个挂了，只有第二个收到了回调事件
         *  如果，不是第一个挂了，其中一个挂了，也能造成它这个后面收到这个通知，从而造成它后面的监控挂了的消息前面的消息
         */
        switch (event.getType()) {

            case None:
                break;
            case NodeCreated:
                break;
            case NodeDeleted:   // 节点锁删除时，判断下一个序列节点是否是第一个节点，获取锁
                zooKeeper.getChildren("/", false, this, "context");
                break;
            case NodeDataChanged:
                break;
            case NodeChildrenChanged:
                break;
            case DataWatchRemoved:
                break;
            case ChildWatchRemoved:
                break;
        }

    }

    /**
     * create创建节点成功后 异步的回调
     *
     * 创建节点成功后，获取锁目录下的所有节点（一个节点表示一个获取锁的Client），判断当前节点是不是第一个节点
     *
     * @param rc
     * @param path
     * @param ctx
     * @param name
     */
    @Override
    public void processResult(int rc, String path, Object ctx, String name) {
        if (name != null) {
            System.out.println(threadName+"创建节点：" + name);
            pathName = name;
            // 不需要监控锁的目录节点/（监控锁目录节点，就变成每次锁释放，所有Client争抢锁）,每个节点只需要监控前一个序列节点的事件
            zooKeeper.getChildren("/", false, this, "context");
        }
    }

    /**
     * getChildren()获取子节点 异步回调
     *
     * 判断是不是第一个序列节点，是第一个节点直接释放阻塞，获取到锁执行业务代码，不是第一个节点就监控前一个序列节点
     *
     * @param rc
     * @param path
     * @param ctx
     * @param children
     * @param stat
     */
    @Override
    public void processResult(int rc, String path, Object ctx, List<String> children, Stat stat) {

        // 取得子节点无序，无法监控前一个节点
        Collections.sort(children);
        int i = children.indexOf(pathName.substring(1));        // 判断当前节点排序

        if (i == 0) {
            try {
                // 第一个节点获取到锁的时候，设置一个数据，可以设置可重入锁，后续tryLock获取到节点数据的时候，判断zookeeper节点中有没有该数据，有则可以直接获取该数据
                zooKeeper.setData("/", threadName.getBytes(), -1);
                // 第一个序列节点
                latch.countDown();
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else{
            // 当前节点不是第一个序列节点的时候，判断前一个节点是否存在，并监控前一个节点
            zooKeeper.exists("/" + children.get(i - 1), this, this, "context");
        }


    }

    /**
     * exists() 异步方法 的回调
     *
     * 判断前一个节点存不存在时，刚好前一个节点挂了，
     *
     * @param rc
     * @param path
     * @param ctx
     * @param stat
     */
    @Override
    public void processResult(int rc, String path, Object ctx, Stat stat) {
        // TODO
    }
}
