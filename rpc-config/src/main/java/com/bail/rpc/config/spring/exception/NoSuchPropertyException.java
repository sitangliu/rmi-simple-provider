package com.bail.rpc.config.spring.exception;

/**
 * @Description：create
 * @author: ext.liukai3
 * @date: 2022/1/28 18:00
 */
public class NoSuchPropertyException extends RuntimeException {
    private static final long serialVersionUID = -2725364246023268766L;

    public NoSuchPropertyException() {
        super();
    }

    public NoSuchPropertyException(String msg) {
        super(msg);
    }
}
