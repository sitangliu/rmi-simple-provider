package com.bail.rpc.config.spring.cluster;

import com.bail.rpc.config.spring.common.URL;
import com.bail.rpc.config.spring.exception.RpcException;
import com.bail.rpc.config.spring.proxy.Invocation;
import com.bail.rpc.config.spring.proxy.Invoker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @Description：create
 * @author: ext.liukai3
 * @date: 2022/2/17 16:44
 */
public abstract class AbstractDirectory<T> implements Directory<T>{

    private static final Logger logger = LoggerFactory.getLogger(AbstractDirectory.class);
    private final URL url;
    private volatile boolean destroyed = false;

    private volatile URL consumerUrl;

    private volatile List<Router> routers;

    public AbstractDirectory(URL url) {
        this(url, null);
    }

    public AbstractDirectory(URL url, List<Router> routers) {
        this(url, url, routers);
    }

    public AbstractDirectory(URL url, URL consumerUrl, List<Router> routers) {
        if (url == null)
            throw new IllegalArgumentException("url == null");
        this.url = url;
        this.consumerUrl = consumerUrl;
        setRouters(routers);
    }

    protected void setRouters(List<Router> routers){
        this.routers = routers;
    }

    protected abstract List<Invoker<T>> doList(Invocation invocation) throws RpcException;
}
