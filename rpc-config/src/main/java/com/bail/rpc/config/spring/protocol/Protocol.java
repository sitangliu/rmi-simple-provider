package com.bail.rpc.config.spring.protocol;

import com.bail.rpc.config.spring.common.URL;
import com.bail.rpc.config.spring.exception.RpcException;
import com.bail.rpc.config.spring.exporter.Exporter;
import com.bail.rpc.config.spring.proxy.Invoker;

/**
 * @Description：发布协议
 * @author: ext.liukai3
 * @date: 2022/1/21 20:37
 */
public interface Protocol {

    int getDefaultPort();

    <T> Exporter<T> export(Invoker<T> invoker) throws RpcException;

    <T> Invoker<T> refer(Class<T> type, URL url) throws RpcException;

    void destroy();

}
