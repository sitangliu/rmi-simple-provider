package com.bail.rpc.config.spring.proxy;

import com.bail.rpc.config.spring.common.Constants;
import com.bail.rpc.config.spring.common.URL;
import com.bail.rpc.config.spring.exception.RpcException;
import com.bail.rpc.config.spring.wrapper.ReflectUtils;

/**
 * @Description：create
 * @author: ext.liukai3
 * @date: 2022/2/17 15:46
 */
public abstract class AbstractProxyFactory implements ProxyFactory{
    @Override
    public <T> T getProxy(Invoker<T> invoker) throws RpcException {
        Class<?>[] interfaces = null;
        String config = invoker.getUrl().getParameter("interfaces");
        if (config != null && config.length() > 0) {
            String[] types = Constants.COMMA_SPLIT_PATTERN.split(config);
            if (types != null && types.length > 0) {
                interfaces = new Class<?>[types.length + 2];
                interfaces[0] = invoker.getInterface();
                for (int i = 0; i < types.length; i++) {
                    interfaces[i + 1] = ReflectUtils.forName(types[i]);
                }
            }
        }
        if (interfaces == null) {
            interfaces = new Class<?>[]{invoker.getInterface()};
        }
        return getProxy(invoker, interfaces);
    }

    public abstract <T> T getProxy(Invoker<T> invoker, Class<?>[] types);
}
