package com.bail.rpc.config.spring.proxy;

import com.bail.rpc.config.spring.common.URL;
import com.bail.rpc.config.spring.exception.RpcException;

/**
 * @Description：代理类工厂
 * @author: ext.liukai3
 * @date: 2022/1/28 16:28
 */
public interface ProxyFactory {


    <T> T getProxy(Invoker<T> invoker) throws RpcException;


    /**
     * 获取invoker调用者
     * @param proxy
     * @param type
     * @param url
     * @param <T>
     * @return
     * @throws RpcException
     */
    <T> Invoker<T> getInvoker(T proxy, Class<T> type, URL url) throws RpcException;
}
