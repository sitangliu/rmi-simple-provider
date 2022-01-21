package com.bail.rpc.config.spring.po;

/**
 * @Description：注册中心对象
 * @author: ext.liukai3
 * @date: 2022/1/19 20:24
 */
public class RegistryConfig extends ApplicationConfig{

    /**
     * 注册中心直连
     */
    public static final String NO_AVAILABLE = "N/A";

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


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
}
