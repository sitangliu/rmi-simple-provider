package com.bail.rpc.config.spring.proxy;


/**
 * @Description：调用者
 * @author: ext.liukai3
 * @date: 2022/1/28 16:30
 */
public interface Invoker<T> extends Node{

    /**
     * 获取服务的接口
     * @return
     */
    Class<T> getInterface();

    /**
     * 根据invocation参数调用方法
     * @param invocation
     * @return
     * @throws Exception
     */
    Object invoke(Invocation invocation) throws Exception;
}
