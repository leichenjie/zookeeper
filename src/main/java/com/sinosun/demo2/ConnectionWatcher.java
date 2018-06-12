package com.sinosun.demo2;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ConnectionWatcher implements Watcher {

    private static final int SESSION_TIMEOUT = 5000;

    protected ZooKeeper zk;

    CountDownLatch connnectedSignal = new CountDownLatch(1);

    public void connect(String hose) throws IOException, InterruptedException {
        zk = new ZooKeeper(hose,SESSION_TIMEOUT,this);
        connnectedSignal.await();
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
            connnectedSignal.countDown();
        }
    }

    public  void close() throws InterruptedException {
        zk.close();
    }
}
