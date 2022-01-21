package com.bail.rpc.config.spring.registry;

import com.bail.rpc.config.spring.common.URL;

import java.util.List;

/**
 * @Description：注册服务
 * @author: ext.liukai3
 * @date: 2022/1/21 21:09
 */
public interface RegistryService {

    void register(URL url);

    void unregister(URL url);

    void subscribe(URL url,  String listener);

    void unsubscribe(URL url, String listener);

    List<URL> lookup(URL url);
}
