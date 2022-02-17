package com.bail.rpc.config.spring.cluster;

import com.bail.rpc.config.spring.exception.RpcException;
import com.bail.rpc.config.spring.proxy.Invocation;
import com.bail.rpc.config.spring.proxy.Invoker;
import com.bail.rpc.config.spring.proxy.Node;

import java.util.List;

/**
 * @Description：create
 * @author: ext.liukai3
 * @date: 2022/2/17 16:11
 */
public interface Directory<T> extends Node {


    /**
     * get service type.
     *
     * @return service type.
     */
    Class<T> getInterface();

    /**
     * list invokers.
     *
     * @return invokers
     */
    List<Invoker<T>> list(Invocation invocation) throws RpcException;

}
