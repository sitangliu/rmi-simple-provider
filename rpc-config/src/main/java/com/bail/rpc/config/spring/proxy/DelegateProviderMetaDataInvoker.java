package com.bail.rpc.config.spring.proxy;

import com.bail.rpc.config.spring.bo.ServiceConfig;
import com.bail.rpc.config.spring.common.URL;
import com.bail.rpc.config.spring.exception.RpcException;

/**
 * @Description：create
 * @author: ext.liukai3
 * @date: 2022/1/29 10:51
 */
public class DelegateProviderMetaDataInvoker<T> implements Invoker{

    protected final Invoker<T> invoker;
    private ServiceConfig metadata;

    public DelegateProviderMetaDataInvoker(Invoker<T> invoker,ServiceConfig serviceConfig){
        this.invoker = invoker;
        this.metadata = serviceConfig;
    }

    @Override
    public Class getInterface() {
        return invoker.getInterface();
    }

    @Override
    public Result invoke(Invocation invocation) throws RpcException {
        return invoker.invoke(invocation);
    }

    @Override
    public URL getUrl() {
        return invoker.getUrl();
    }

    @Override
    public boolean isAvailable() {
        return invoker.isAvailable();
    }

    @Override
    public void destroy() {
        invoker.destroy();
    }
}
