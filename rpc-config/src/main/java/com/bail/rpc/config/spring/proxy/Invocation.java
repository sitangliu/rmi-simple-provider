package com.bail.rpc.config.spring.proxy;

import java.util.Map;

/**
 * @Description：create
 * @author: ext.liukai3
 * @date: 2022/1/28 16:43
 */
public interface Invocation {

    String getMethodName();

    Class<?>[] getParameterTypes();

    Object[] getArguments();

    Map<String,String> getAttachments();

    String getAttachment(String key);

    String getAttachment(String key,String defaultValue);

    Invoker<?> getInvoker();
}
