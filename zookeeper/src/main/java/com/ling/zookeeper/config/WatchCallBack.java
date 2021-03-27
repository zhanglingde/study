package com.ling.zookeeper.config;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

/**
 * @author zhangling  2021/3/27 17:09
 */
public class WatchCallBack implements Watcher, AsyncCallback.StatCallback, AsyncCallback.DataCallback{

    ZooKeeper zk;
    MyConf conf;

    CountDownLatch latch = new CountDownLatch(1);

    public MyConf getConf() {
        return conf;
    }

    public void setConf(MyConf conf) {
        this.conf = conf;
    }

    public ZooKeeper getZk() {
        return zk;
    }

    public void setZk(ZooKeeper zk) {
        this.zk = zk;
    }

    public void aWait(){
        // 异步
        zk.exists("/AppConf",this,this ,"ABC");

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * getData() 的异步接口的回调，调用getData()后不会阻塞当前调用的线程，而是取到数据后调用该回调方法
     * @param rc
     * @param path
     * @param ctx
     * @param data
     * @param stat
     */
    @Override
    public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
        if(data != null){
            String s = new String(data);
            // 更新配置
            conf.setConf(s);
            latch.countDown();

        }
    }

    /**
     * exists方法的回调
     *
     * 1. 项目刚启动的时候，调用exists()的异步接口判断节点配置数据是否存在
     * 2. exists()执行完后回调，若配置节点存在数据调用getData()的异步接口，
     * 3. 当getData()异步接口执行完获得数据后，调用getData()的回调，更新配置
     * @param rc
     * @param path
     * @param ctx
     * @param stat
     */
    @Override
    public void processResult(int rc, String path, Object ctx, Stat stat) {
        if (stat != null) {
            //  AppConf节点存在，回调调用processResult(int rc, String path, Object ctx, byte[] data, Stat stat)
            zk.getData("/AppConf",this,this,"ctx");

        }
    }

    /**
     * Watch回调方法
     *
     * 1. 当配置节点数据被更改后，就调用getData()的异步接口
     * 2. 当getData()异步接口执行完获得数据后，调用getData()的回调，更新配置
     *
     * @param watchedEvent
     */
    @Override
    public void process(WatchedEvent watchedEvent) {
        switch (watchedEvent.getType()) {
            case None:
                break;
            case NodeCreated:               // 节点被创建
                zk.getData("/AppConf",this,this,"ctx");
                break;
            case NodeDeleted:               // 容忍性：之前节点已经存在，当节点被删除，配置不更改
                conf.setConf("");
                latch = new CountDownLatch(1);
                break;
            case NodeDataChanged:           // 节点数据变更了，再取一遍数据，然后回调更新配置
                zk.getData("/AppConf",this,this,"ctx");
                break;
            case NodeChildrenChanged:
                break;
            case DataWatchRemoved:
                break;
            case ChildWatchRemoved:
                break;
        }
    }
}
