package com.bail.rpc.config.spring.po;

/**
 * @Description：注册中心对象
 * @author: ext.liukai3
 * @date: 2022/1/19 20:24
 */
public class RegistryConfig extends ApplicationConfig{
    /**
     * 序列化ID
     */
    private static final long serialVersionUID = 5376029739572560057L;

    /**
     * registry address
     */
    private String address;

    /**
     * registry port
     */
    private String port;

    /**
     * registry server username
     */
    private String username;

    /**
     * registry server password
     */
    private String password;

    /**
     * registry protocol
     */
    private String protocol;
}
