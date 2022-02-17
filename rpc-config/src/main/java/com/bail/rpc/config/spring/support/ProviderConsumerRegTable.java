package com.bail.rpc.config.spring.support;

import com.bail.rpc.config.spring.common.Constants;
import com.bail.rpc.config.spring.common.URL;
import com.bail.rpc.config.spring.proxy.Invoker;
import com.bail.rpc.config.spring.registry.RegistryDirectory;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description：服务消费注册表
 * @author: ext.liukai3
 * @date: 2022/1/29 15:08
 */
public class ProviderConsumerRegTable {

    public static ConcurrentHashMap<String, Set<ProviderInvokerWrapper>> providerInvokers = new ConcurrentHashMap<String, Set<ProviderInvokerWrapper>>();
    public static ConcurrentHashMap<String, Set<ConsumerInvokerWrapper>> consumerInvokers = new ConcurrentHashMap<String, Set<ConsumerInvokerWrapper>>();

    public static void registerProvider(Invoker invoker, URL registryUrl, URL providerUrl) {
        ProviderInvokerWrapper wrapperInvoker = new ProviderInvokerWrapper<>(invoker, registryUrl, providerUrl);
        String serviceUniqueName = providerUrl.getServiceKey();
        Set<ProviderInvokerWrapper> invokers = providerInvokers.get(serviceUniqueName);
        if(invokers == null){
            providerInvokers.putIfAbsent(serviceUniqueName,new HashSet<ProviderInvokerWrapper>());
            invokers = providerInvokers.get(serviceUniqueName);
        }
        invokers.add(wrapperInvoker);
    }

    public static Set<ProviderInvokerWrapper> getProviderInvoker(String serviceUniqueName) {
        Set<ProviderInvokerWrapper> invokers = providerInvokers.get(serviceUniqueName);
        if (invokers == null) {
            return Collections.emptySet();
        }
        return invokers;
    }

    public static ProviderInvokerWrapper getProviderWrapper(Invoker invoker) {
        URL providerUrl = invoker.getUrl();
        if (Constants.REGISTRY_PROTOCOL.equals(providerUrl.getProtocol())) {
            providerUrl = URL.valueOf(providerUrl.getParameterAndDecoded(Constants.EXPORT_KEY));
        }
        String serviceUniqueName = providerUrl.getServiceKey();
        Set<ProviderInvokerWrapper> invokers = providerInvokers.get(serviceUniqueName);
        if (invokers == null) {
            return null;
        }

        for (ProviderInvokerWrapper providerWrapper : invokers) {
            Invoker providerInvoker = providerWrapper.getInvoker();
            if (providerInvoker == invoker) {
                return providerWrapper;
            }
        }

        return null;
    }

    public static <T> void registerConsmer(Invoker<T> invoker, URL registryUrl, URL consumerUrl, RegistryDirectory<T> registryDirectory) {
        ConsumerInvokerWrapper<T> wrapperInvoker = new ConsumerInvokerWrapper<>(invoker, registryUrl, consumerUrl, registryDirectory);
        String serviceUniqueName = consumerUrl.getServiceKey();
        Set<ConsumerInvokerWrapper> invokers = consumerInvokers.get(serviceUniqueName);
        if (invokers == null) {
            consumerInvokers.putIfAbsent(serviceUniqueName, new HashSet<ConsumerInvokerWrapper>());
            invokers = consumerInvokers.get(serviceUniqueName);
        }
        invokers.add(wrapperInvoker);
    }
}
