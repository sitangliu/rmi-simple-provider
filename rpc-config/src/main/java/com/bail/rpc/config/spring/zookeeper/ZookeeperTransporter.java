package com.bail.rpc.config.spring.zookeeper;

import com.bail.rpc.config.spring.common.URL;

/**
 * @Description：create
 * @author: ext.liukai3
 * @date: 2022/2/15 17:48
 */
public interface ZookeeperTransporter {

    ZookeeperClient connect(URL url);

}
