package com.bail.rpc.config.spring.proxy;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description：create
 * @author: ext.liukai3
 * @date: 2022/1/28 17:24
 */
public class RpcResult implements Result, Serializable {
    private static final long serialVersionUID = -5218722259992984050L;

    private Object result;

    private Throwable exception;

    private Map<String, String> attachments = new HashMap<String, String>();

    public RpcResult(Object result) {
        this.result = result;
    }

    public RpcResult(Throwable exception) {
        this.exception = exception;
    }

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public Throwable getException() {
        return null;
    }

    @Override
    public boolean hasException() {
        return false;
    }

    @Override
    public Object recreate() throws Throwable {
        if (exception != null) {
            throw exception;
        }
        return result;
    }

    @Override
    public Object getResult() {
        return getValue();
    }

    @Override
    public Map<String, String> getAttachments() {
        return attachments;
    }

    @Override
    public String getAttachment(String key) {
        return attachments.get(key);
    }

    @Override
    public String getAttachment(String key, String defaultValue) {
        String result = attachments.get(key);
        if (result == null || result.length() == 0) {
            result = defaultValue;
        }
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public void setException(Throwable exception) {
        this.exception = exception;
    }

    public void setAttachments(Map<String, String> attachments) {
        this.attachments = attachments;
    }

    public String toString() {
        return "RpcResult [result=" + result + ", exception=" + exception + "]";
    }
}
