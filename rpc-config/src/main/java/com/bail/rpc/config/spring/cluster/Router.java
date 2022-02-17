package com.bail.rpc.config.spring.cluster;

import com.bail.rpc.config.spring.common.URL;
import com.bail.rpc.config.spring.exception.RpcException;
import com.bail.rpc.config.spring.proxy.Invocation;
import com.bail.rpc.config.spring.proxy.Invoker;

import java.util.List;

/**
 * @Description：create
 * @author: ext.liukai3
 * @date: 2022/2/17 17:24
 */
public interface Router extends Comparable<Router>{

    /**
     * get the router url.
     *
     * @return url
     */
    URL getUrl();

    /**
     * route.
     *
     * @param invokers
     * @param url        refer url
     * @param invocation
     * @return routed invokers
     * @throws RpcException
     */
    <T> List<Invoker<T>> route(List<Invoker<T>> invokers, URL url, Invocation invocation) throws RpcException;
}
