package org.bl.rpc;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description：远程请求对象
 * @author: ext.liukai3
 * @date: 2021/11/8 15:25
 */
@Data
public class RpcRequest implements Serializable {

    private Long serialVersionUID = 1L;

    private String className;
    private String methodName;
    private Object[] args;
    private Class[] types;

}
