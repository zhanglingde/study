package com.ling.zookeeper.config;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author zhangling  2021/3/27 16:59
 */
public class TestConfig {

    ZooKeeper zooKeeper;

    @Before
    public void con(){
        zooKeeper = ZKUtils.getZK();
    }

    @After
    public void close(){
        try {
            zooKeeper.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test(){

        WatchCallBack watchCallBack = new WatchCallBack();
        watchCallBack.setZk(zooKeeper);
        MyConf myConf = new MyConf();
        watchCallBack.setConf(myConf);

        watchCallBack.aWait();
        // 1.节点不存在
        // 2.节点存在

        while (true){               // 一直保持zookeeper连接
            // 业务代码
            if(myConf.getConf().equals("")){
                System.out.println("conf lose...");
                watchCallBack.aWait();
            }
            System.out.println(myConf.getConf());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
