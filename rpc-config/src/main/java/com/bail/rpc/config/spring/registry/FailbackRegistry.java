package com.bail.rpc.config.spring.registry;

import com.bail.rpc.config.spring.common.URL;

/**
 * @Description：create
 * @author: ext.liukai3
 * @date: 2022/2/16 17:18
 */
public abstract class FailbackRegistry extends AbstractRegistry<T> {

    public FailbackRegistry(){

    }

    public FailbackRegistry(URL url) {
        super(url);
    }
}
