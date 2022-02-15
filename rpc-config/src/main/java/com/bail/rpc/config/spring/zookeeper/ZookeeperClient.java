package com.bail.rpc.config.spring.zookeeper;

import com.bail.rpc.config.spring.common.URL;

import java.util.List;

/**
 * @Description：zookeeper client
 * @author: ext.liukai3
 * @date: 2022/2/15 17:49
 */
public interface ZookeeperClient {

    void create(String path, boolean ephemeral);

    void delete(String path);

    List<String> getChildren(String path);

//    List<String> addChildListener(String path, ChildListener listener);
//
//    void removeChildListener(String path, ChildListener listener);
//
//    void addStateListener(StateListener listener);
//
//    void removeStateListener(StateListener listener);

    boolean isConnected();

    void close();

    URL getUrl();

}
