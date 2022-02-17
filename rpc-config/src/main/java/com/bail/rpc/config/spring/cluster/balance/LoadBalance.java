package com.bail.rpc.config.spring.cluster.balance;

import com.bail.rpc.config.spring.common.URL;
import com.bail.rpc.config.spring.exception.RpcException;
import com.bail.rpc.config.spring.proxy.Invocation;
import com.bail.rpc.config.spring.proxy.Invoker;

import java.util.List;

/**
 * @Description：create
 * @author: ext.liukai3
 * @date: 2022/2/17 19:52
 */
public interface LoadBalance {

    <T> Invoker<T> select(List<Invoker<T>> invokers, URL url, Invocation invocation) throws RpcException;

}
