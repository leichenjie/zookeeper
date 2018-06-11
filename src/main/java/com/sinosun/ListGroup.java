package com.sinosun;

import org.apache.zookeeper.KeeperException;

import java.io.IOException;
import java.util.List;

public class ListGroup extends ConnectionWatcher {
    public void list(String groupName) throws KeeperException, InterruptedException {
        String path = "/" + groupName;
        try {
            List<String> children = zk.getChildren(path,false);
            if (children.isEmpty()) {
                System.out.printf("No memebers in group %s\n",groupName);
                System.exit(1);
            }
            for (String child:children) {
                System.out.printf("Group %s does not exist \n",groupName);
            }
        } catch (KeeperException.NoNodeException e) {
            System.out.printf("Group %s does not exist \n",groupName);
            System.exit(1);
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        ListGroup listGroup = new ListGroup();
        listGroup.connect("192.168.133.128:2181");
        listGroup.list("GroupTest");
        listGroup.close();
    }
}
