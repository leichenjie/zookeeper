package com.sinosun.demo2;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.io.IOException;

public class ConfigWatcher implements Watcher {

    private ActiveKeyValueStore activeKeyValueStore;

    @Override
    public void process(WatchedEvent watchedEvent) {
        if (watchedEvent.getType() == Event.EventType.NodeDataChanged) {
            try {
                displayConfig();
            }catch (InterruptedException e) {
                System.err.println("Interrupted. existing.");
                Thread.currentThread().interrupt();
            }catch (KeeperException e) {
                System.out.printf("KeeperException %s Existing\n",e);
            }
        }
    }

    public ConfigWatcher(String hosts) throws IOException, InterruptedException {
        activeKeyValueStore = new ActiveKeyValueStore();
        activeKeyValueStore.connect(hosts);
    }

    public void displayConfig() throws KeeperException, InterruptedException {
        String value = activeKeyValueStore.read(ConfigUpdater.PATH,this);
        System.out.printf("Read %s as %s\n",ConfigUpdater.PATH,value);
    }

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        ConfigWatcher configWatcher = new ConfigWatcher("192.168.133.129:2181");
        configWatcher.displayConfig();
        Thread.sleep(Long.MAX_VALUE);
    }

}
