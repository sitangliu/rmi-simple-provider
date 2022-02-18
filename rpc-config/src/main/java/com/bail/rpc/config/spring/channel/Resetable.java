package com.bail.rpc.config.spring.channel;

import com.bail.rpc.config.spring.common.URL;

/**
 * @Description：create
 * @author: ext.liukai3
 * @date: 2022/2/18 18:22
 */
public interface Resetable {


    /**
     * reset.
     *
     * @param url
     */
    void reset(URL url);

}
