package com.bail.rpc.config.spring.bo;

/**
 * @Description：create
 * @author: ext.liukai3
 * @date: 2022/2/17 20:51
 */
public interface GenericService {

    Object $invoke(String method, String[] parameterTypes, Object[] args) throws Exception;
}
