package com.bail.rpc.config.spring.listener;

import com.bail.rpc.config.spring.common.URL;

import java.util.List;

/**
 * @Description:通知监听器
 * @author: ext.liukai3
 * @date: 2022/1/29 16:07
 */
public interface NotifyListener {

    /**
     * 监听的路径
     * @param urls
     */
    void notify(List<URL> urls);

}
