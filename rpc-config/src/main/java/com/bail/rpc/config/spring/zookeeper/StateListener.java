package com.bail.rpc.config.spring.zookeeper;

/**
 * @Description：状态监听者
 * @author: ext.liukai3
 * @date: 2022/2/16 16:44
 */
public interface StateListener {

    int DISCONNECTED = 0;

    int CONNECTED = 1;

    int RECONNECTED = 2;

    void stateChanged(int connected);
}
