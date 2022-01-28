package com.bail.rpc.config.spring.proxy;

import com.bail.rpc.config.spring.common.URL;

import java.lang.reflect.Proxy;

/**
 * @Description：create
 * @author: ext.liukai3
 * @date: 2022/1/21 18:16
 */
public class JdkProxyFactory implements ProxyFactory{

    @Override
    public <T> T getProxy(Invoker<T> invoker, Class<?>[] interfaces) throws Exception {

        return (T)Proxy.newProxyInstance(Proxy.class.getClassLoader(),interfaces,new InvokerInvocationHandler(invoker));
    }

    @Override
    public <T> Invoker<T> getInvoker(T proxy, Class<T> type, URL url) throws Exception {

        return new AbstractProxyInvoker<T>(proxy, type, url) {
            @Override
            protected Object doInvoke(T proxy, String methodName, Class<?>[] parameterTypes, Object[] arguments) throws Throwable {

            }
        }
        return null;
    }
}
