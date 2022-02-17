package com.bail.rpc.config.spring.cluster;

import com.bail.rpc.config.spring.exception.RpcException;
import com.bail.rpc.config.spring.proxy.Invoker;

/**
 * @Description：create
 * @author: ext.liukai3
 * @date: 2022/2/17 16:10
 */
public interface Cluster {

    /**
     * Merge the directory invokers to a virtual invoker.
     *
     * @param <T>
     * @param directory
     * @return cluster invoker
     * @throws RpcException
     */
    <T> Invoker<T> join(Directory<T> directory) throws RpcException;

}
