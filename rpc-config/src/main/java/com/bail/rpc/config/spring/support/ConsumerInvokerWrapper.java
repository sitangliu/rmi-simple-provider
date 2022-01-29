package com.bail.rpc.config.spring.support;

import com.bail.rpc.config.spring.common.URL;
import com.bail.rpc.config.spring.exception.RpcException;
import com.bail.rpc.config.spring.proxy.Invocation;
import com.bail.rpc.config.spring.proxy.Invoker;
import com.bail.rpc.config.spring.proxy.Result;
import com.bail.rpc.config.spring.registry.RegistryDirectory;

/**
 * @Description：create
 * @author: ext.liukai3
 * @date: 2022/1/29 15:14
 */
public class ConsumerInvokerWrapper<T> implements Invoker {
    private Invoker<T> invoker;
    private URL originUrl;
    private URL registryUrl;
    private URL consumerUrl;
    private RegistryDirectory registryDirectory;

    public ConsumerInvokerWrapper(Invoker<T> invoker, URL registryUrl, URL consumerUrl, RegistryDirectory registryDirectory) {
        this.invoker = invoker;
        this.originUrl = URL.valueOf(invoker.getUrl().toFullString());
        this.registryUrl = URL.valueOf(registryUrl.toFullString());
        this.consumerUrl = consumerUrl;
        this.registryDirectory = registryDirectory;
    }

    @Override
    public Class<T> getInterface() {
        return invoker.getInterface();
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
    public Result invoke(Invocation invocation) throws RpcException {
        return invoker.invoke(invocation);
    }

    @Override
    public void destroy() {
        invoker.destroy();
    }

    public URL getOriginUrl() {
        return originUrl;
    }

    public URL getRegistryUrl() {
        return registryUrl;
    }

    public Invoker<T> getInvoker() {
        return invoker;
    }

    public URL getConsumerUrl() {
        return consumerUrl;
    }

    public RegistryDirectory getRegistryDirectory() {
        return registryDirectory;
    }
}
