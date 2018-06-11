package com.sinosun;

import org.apache.zookeeper.KeeperException;

import java.io.IOException;
import java.util.List;

public class DeleteGroup extends ConnectionWatcher {
    public void delete(String name) throws KeeperException, InterruptedException {
        String path = "/" + name;
        List<String> children;
        try {
            children = zk.getChildren(path,false);
            for (String child:children) {
                zk.delete(path + "/" + child,-1);
            }
            zk.delete(path,-1);
        } catch (KeeperException.NoNodeException e) {
            System.out.printf("Group %s does not exist\n",name);
            System.exit(1);
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        DeleteGroup deleteGroup = new DeleteGroup();
        deleteGroup.connect("192.168.133.128:2181");
        deleteGroup.delete("GroupTest");
        deleteGroup.close();
    }
}
