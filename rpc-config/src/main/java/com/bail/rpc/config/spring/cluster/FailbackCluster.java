package com.bail.rpc.config.spring.cluster;

import com.bail.rpc.config.spring.cluster.invoker.FailbackClusterInvoker;
import com.bail.rpc.config.spring.exception.RpcException;
import com.bail.rpc.config.spring.proxy.Invoker;

/**
 * @Description：create
 * @author: ext.liukai3
 * @date: 2022/2/17 19:34
 */
public class FailbackCluster implements Cluster{

    public final static String NAME = "failback";

    @Override
    public <T> Invoker<T> join(Directory<T> directory) throws RpcException {
        return new FailbackClusterInvoker<>(directory);
    }
}
