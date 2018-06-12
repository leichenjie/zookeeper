package com.sinosun.demo2;

import com.sinosun.demo2.ActiveKeyValueStore;
import com.sinosun.demo2.ConfigUpdater;
import org.apache.zookeeper.KeeperException;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class ResilientConfigUpdater {
    public static final String PATH = "/config";

    private ActiveKeyValueStore store;
    private Random random = new Random();

    public ResilientConfigUpdater(String hosts) throws IOException, InterruptedException {
        store = new ActiveKeyValueStore();
        store.connect(hosts);
    }

    public void run() throws KeeperException, InterruptedException {
        while (true) {
            String value = random.nextInt(100) + "";
            store.write(PATH,value);
            System.out.printf("Set %s to %s\n",PATH,value);
            TimeUnit.SECONDS.sleep(random.nextInt(5));
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        while (true) {
            try {
                ConfigUpdater configUpdater = new ConfigUpdater("192.168.133.129:2181");
                configUpdater.run();
            }catch (KeeperException.SessionExpiredException e) {

            }catch (KeeperException e) {
                e.printStackTrace();
                break;
            }
        }

    }
}
