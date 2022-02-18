package com.bail.rpc.config.spring.cluster.invoker;

import com.bail.rpc.config.spring.proxy.Invocation;
import com.bail.rpc.config.spring.proxy.Invoker;
import com.bail.rpc.config.spring.proxy.Result;

import java.util.Set;

/**
 * @Description：create
 * @author: ext.liukai3
 * @date: 2022/2/18 17:01
 */
public class BailInvoker<T> extends AbstractInvoker<T>{


    private final Set<Invoker<?>> invokers;

    @Override
    protected Result doInvoke(Invocation invocation) throws Throwable {
        return null;
    }
}
