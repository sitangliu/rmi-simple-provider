package com.bail.rpc.config.spring.proxy;

import com.bail.rpc.config.spring.common.URL;
import com.bail.rpc.config.spring.exception.RpcException;

import java.lang.reflect.Proxy;

/**
 * @Description：create
 * @author: ext.liukai3
 * @date: 2022/1/21 18:16
 */
public class JdkProxyFactory extends AbstractProxyFactory {

    @Override
    public <T> T getProxy(Invoker<T> invoker, Class<?>[] interfaces) throws RpcException {

        return (T)Proxy.newProxyInstance(Proxy.class.getClassLoader(),interfaces,new InvokerInvocationHandler(invoker));
    }

    @Override
    public <T> Invoker<T> getInvoker(T proxy, Class<T> type, URL url) throws RpcException {
        final Wrapper wrapper = Wrapper.getWrapper(type);
        return new AbstractProxyInvoker<T>(proxy, type, url) {
            @Override
            protected Object doInvoke(T proxy, String methodName, Class<?>[] parameterTypes, Object[] arguments) throws Throwable {
                return wrapper.invokeMethod(proxy,methodName,parameterTypes,arguments);
            }
        };
    }
}
