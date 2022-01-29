package com.bail.rpc.config.spring.exporter;

import com.bail.rpc.config.spring.proxy.Invoker;

/**
 * @Description：create
 * @author: ext.liukai3
 * @date: 2022/1/29 11:19
 */
public interface Exporter<T> {


    /**
     * get invoker.
     *
     * @return invoker
     */
    Invoker<T> getInvoker();

    /**
     * unexport.
     * <p>
     * <code>
     * getInvoker().destroy();
     * </code>
     */
    void unexport();

}
