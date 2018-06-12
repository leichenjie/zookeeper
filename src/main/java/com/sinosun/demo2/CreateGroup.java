package com.sinosun.demo2;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class CreateGroup implements Watcher {

    private static final int SESSION_TIMEOUT = 5000;

    private ZooKeeper zk;

    private CountDownLatch connectedSignal = new CountDownLatch(1);
    @Override
    public void process(WatchedEvent watchedEvent) {
        if(watchedEvent.getState() == Event.KeeperState.SyncConnected) {
            connectedSignal.countDown();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        CreateGroup createGroup = new CreateGroup();
        createGroup.connect("192.168.133.128:2181");
        createGroup.create("GroupTest");
        createGroup.close();
    }

    private void close() throws InterruptedException {
            zk.close();
    }

    private void create(String name) throws KeeperException, InterruptedException {
        String path = "/" + name;
        if (zk.exists(path,false) == null) {
            zk.create(path,null, ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
        }
        System.out.println("Created" + path);
    }

    private void connect(String hosts) throws InterruptedException, IOException {
        zk = new ZooKeeper(hosts,SESSION_TIMEOUT,this);
        connectedSignal.await();
    }
 }
