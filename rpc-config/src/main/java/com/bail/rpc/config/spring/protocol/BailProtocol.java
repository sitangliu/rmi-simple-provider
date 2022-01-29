package com.bail.rpc.config.spring.protocol;

import com.bail.rpc.config.spring.common.URL;
import com.bail.rpc.config.spring.exception.RpcException;
import com.bail.rpc.config.spring.exporter.Exporter;
import com.bail.rpc.config.spring.proxy.Invoker;

/**
 * @Description：自定义协议
 * @author: ext.liukai3
 * @date: 2022/1/21 20:37
 */
public class BailProtocol implements Protocol{
    @Override
    public int getDefaultPort() {
        return 20880;
    }

    @Override
    public <T> Exporter<T> export(Invoker<T> invoker) throws RpcException {
        return null;
    }

    @Override
    public <T> Invoker<T> refer(Class<T> type, URL url) throws RpcException {
        return null;
    }

    @Override
    public void destroy() {

    }
}
