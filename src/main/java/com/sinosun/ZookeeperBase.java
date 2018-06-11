package com.sinosun;

import org.apache.zookeeper.*;

import java.util.concurrent.CountDownLatch;

public class ZookeeperBase {
    /** zookeeper地址 */
    static final String CONNECT_ADDR = "192.168.133.128:2181";
    /** session超时时间 */
    static final int SESSION_OUTTIME = 2000;
    /** 信号量 */
    static final CountDownLatch connectedSemaphore = new CountDownLatch(1);

    public static void main(String[] args) throws Exception {
        ZooKeeper zk = new ZooKeeper(CONNECT_ADDR, SESSION_OUTTIME, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                Event.KeeperState keeperState = watchedEvent.getState();
                        Event.EventType eventType = watchedEvent.getType();
                        if(Event.KeeperState.SyncConnected == keeperState) {
                            if (Event.EventType.None == eventType) {
                                connectedSemaphore.countDown();
                                System.out.println("zk 建立连接");
                    }
                }
            }
        });

        connectedSemaphore.await();

        System.out.println("..");

//        zk.create("/testRoot", "testRoot".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        zk.create("/testRoot/children","children data".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL);
//
//        byte[] data = zk.getData("/testRoot",false,null);
//        System.out.println(new String(data));
//        System.out.println(zk.getChildren("/testRoot",false));

        zk.setData("/testRoot","modify data root".getBytes(),-1);

        byte[] data = zk.getData("/testRoot",false,null);
        System.out.println(new String(data));

        System.out.println(zk.exists("/testRoot/children",false));
        zk.delete("/testRoot/children",-1);
        System.out.println(zk.exists("/testRoot/children",false));
        zk.close();
    }

}
