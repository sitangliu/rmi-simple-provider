package com.bail.rpc.config.spring.zookeeper;

import com.bail.rpc.config.spring.common.URL;
import org.apache.curator.CuratorZookeeperClient;

/**
 * @Description：传输者
 * @author: ext.liukai3
 * @date: 2022/2/15 17:58
 */
public class CuratorZookeeperTransporter implements ZookeeperTransporter{


    @Override
    public ZookeeperClient connect(URL url) {
        return new CuratorZookeeperClient(url);
    }
}
