package com.ling.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Zookeeper连接创建节点
 *
 * @author zhangling 2020/10/27 9:44
 */
public class App {
    public static void main(String[] args) throws InterruptedException, IOException, KeeperException {

        //zookeeper是有session概念的，没有连接池的概念
        // Watch:观察，回调
        // 第一类：new zk的时候，传入的watch，这个watch是session级别的，跟path、node没有关系
        CountDownLatch cd = new CountDownLatch(1);      // CountDownLatch是线程安全的
        // Watch的注册只发生在 读类型调用：get，exist

        /**
         * sessionTimeout：Client连接断开后，session会存在3s后消失
         */
        ZooKeeper zk = new ZooKeeper("192.168.191.128:2181,192.168.191.129:2181,192.168.191.130:2181,192.168.191.132:2181",
                3000, new Watcher() {
            /**
             * Watch的回调方法
             * @param event watch监听的事件
             */
            @Override
            public void process(WatchedEvent event) {
                Event.KeeperState state = event.getState();  // 事件状态
                Event.EventType type = event.getType();      // 事件类型：create、delete、change、children
                String path = event.getPath();               // 事件监听的节点
                System.out.println("new Zookeeper时创建的 watch:" + event.toString());

                switch (state) {
                    case Unknown:
                        break;
                    case Disconnected:
                        break;
                    case NoSyncConnected:
                        break;
                    case SyncConnected: // 同步连接
                        System.out.println("sync connected...");
                        cd.countDown();
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

                /**
                 * 根据监听事件类型，回调执行不同操作
                 */
                switch (type) {
                    case None:
                        break;
                    case NodeCreated:
                        break;
                    case NodeDeleted:
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
        });

        // zookeeper中的方法是异步的，所以需要先阻塞住，等SyncConnected连接完后，才在这里放开阻塞
        cd.await();
        ZooKeeper.States state = zk.getState();
        switch (state) {

            case CONNECTING:   // 正在连接zookeeper
                System.out.println("zookeeper连接中...");
                break;
            case ASSOCIATING:
                break;

            case CONNECTED:   //  连接成功
                System.out.println("zookeeper连接成功...");
                break;
            case CONNECTEDREADONLY:
                break;
            case CLOSED:
                break;
            case AUTH_FAILED:
                break;
            case NOT_CONNECTED:
                break;
        }

        /**
         * "hello".getBytes()：二进制安全的，需要传字节数组
         * ACL：权限控制，
         * CreateMode.EPHEMERAL：节点类型：临时节点，临时序列节点，持久节点，持久序列节点
         */
        String pathName = zk.create("/node01", "hello".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

        // 读取节点数据
        Stat stat = new Stat();
        /**
         * watch监控path时，只能回调一次
         * 所以每次setData触发回调时，在回调函数中都给节点重新设置一个Watch，这样每次该节点的每次事件都会触发回调
         */
        byte[] node = zk.getData("/node01", new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                System.out.println("getData时创建的 watch: " + event.toString());
                try {
                    // watch传true时，设置的watch是defaultWatch，new Zookeeper时传的Watch
                    //zk.getData("/ooxx", true, stat);

                    // ，所以每次setData触发回调时，都给节点重新设置一个Watch，这样每次setData都会触发回调
                    zk.getData("/node01", this, stat);
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, stat);
        System.out.println("读取节点" + pathName + "的数据：" + new String(node));

        // 修改节点数据，修改数据的同时会触发回调
        Stat stat1 = zk.setData("/node01", "world".getBytes(), 0);
        // 和path有关的watch，只会回调一次，所以第二次修改数据时不会触发回调
        Stat stat2 = zk.setData("/node01", "new world".getBytes(), stat1.getVersion());


        // 异步回调
        System.out.println("----------------async start------------------");
        zk.getData("/node01", false, new AsyncCallback.DataCallback() {
            /**
             * 调用getData方法后不会阻塞，该方法没有返回值，当getData执行完后，会调用回调方法
             * @param rc    返回状态码
             * @param path 节点path
             * @param ctx  context上下文，可以传外部的任何参数，对象到回调方法中中
             * @param data 节点数据
             * @param stat 节点元数据
             */
            @Override
            public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
                System.out.println("----------------async call back------------------");
                System.out.println(ctx.toString());
                System.out.println(new String(data));
            }
        }, "上下文context");
        System.out.println("----------------async over------------------");


        Thread.sleep(1000000);


    }
}
