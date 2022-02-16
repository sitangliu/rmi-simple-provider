package com.bail.rpc.config.spring.registry;

import com.bail.rpc.config.spring.common.URL;

import java.io.File;

/**
 * @Description：create
 * @author: ext.liukai3
 * @date: 2022/2/16 20:14
 */
public abstract class AbstractRegistry implements Registry{

    private URL registryUrl;

    private File file;

    public AbstractRegistry(){

    }

    public AbstractRegistry(URL url) {
        setUrl(url);
    }

    protected void setUrl(URL url) {
        if (url == null) {
            throw new IllegalArgumentException("registry url == null");
        }
        this.registryUrl = url;
    }

}
