package com.bail.rpc.config.spring.proxy;

import com.bail.rpc.config.spring.common.URL;

/**
 * @Description：create
 * @author: ext.liukai3
 * @date: 2022/1/28 16:32
 */
public interface Node {



    /**
     * get url.
     *
     * @return url.
     */
    URL getUrl();

    /**
     * is available.
     *
     * @return available.
     */
    boolean isAvailable();

    /**
     * destroy.
     */
    void destroy();
}
