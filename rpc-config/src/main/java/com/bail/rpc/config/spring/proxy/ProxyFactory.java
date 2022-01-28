package com.bail.rpc.config.spring.proxy;

import com.bail.rpc.config.spring.common.URL;

/**
 * @Description：代理类工厂
 * @author: ext.liukai3
 * @date: 2022/1/28 16:28
 */
public interface ProxyFactory {

    /**
     * 获取代理类对象
     * @param invoker
     * @param <T>
     * @return
     * @throws Exception
     */
    <T> T getProxy(Invoker<T> invoker,Class<?>[] interfaces) throws Exception;

    /**
     * 获取invoker调用者
     * @param proxy
     * @param type
     * @param url
     * @param <T>
     * @return
     * @throws Exception
     */
    <T> Invoker<T> getInvoker(T proxy, Class<T> type, URL url) throws Exception;
}
