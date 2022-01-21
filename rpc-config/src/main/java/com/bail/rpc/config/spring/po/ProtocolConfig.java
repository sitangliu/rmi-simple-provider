package com.bail.rpc.config.spring.po;

/**
 * @Description：远程协议对象
 * @author: ext.liukai3
 * @date: 2022/1/19 20:32
 */
public class ProtocolConfig extends AbstractConfig{

    /**
     * 序列化ID
     */
    private static final long serialVersionUID = 6190780109135857841L;

    /**
     * protocol name
     */
    private String name;

    /**
     * protocol host
     */
    private String host;

    /**
     * protocol port
     */
    private String port;

    /**
     * protocol threadpool
     */
    private String threadpool;

    /**
     * protocol threads
     */
    private String threads;

    /**
     * protocol serialization
     */
    private String serialization;


    // context path
    private String contextpath;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getThreadpool() {
        return threadpool;
    }

    public void setThreadpool(String threadpool) {
        this.threadpool = threadpool;
    }

    public String getThreads() {
        return threads;
    }

    public void setThreads(String threads) {
        this.threads = threads;
    }

    public String getSerialization() {
        return serialization;
    }

    public void setSerialization(String serialization) {
        this.serialization = serialization;
    }

    public String getContextpath() {
        return contextpath;
    }

    public void setContextpath(String contextpath) {
        this.contextpath = contextpath;
    }
}
