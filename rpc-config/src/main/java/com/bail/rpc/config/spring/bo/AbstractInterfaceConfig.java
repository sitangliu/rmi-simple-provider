package com.bail.rpc.config.spring.bo;

import com.bail.rpc.config.spring.po.ApplicationConfig;
import com.bail.rpc.config.spring.po.RegistryConfig;

import java.io.Serializable;
import java.util.List;

/**
 * @Description：抽象接口配置
 * @author: ext.liukai3
 * @date: 2022/1/21 19:54
 */
public abstract class AbstractInterfaceConfig implements Serializable {

    /**
     * 序列化
     */
    private static final long serialVersionUID = -4546430137492895830L;

    /**
     * 代理类
     */
    protected String proxy;

    /**
     * 应用配置
     */
    protected ApplicationConfig application;

    /**
     * 注册中心
     */
    protected List<RegistryConfig> registries;


    public String getProxy() {
        return proxy;
    }

    public void setProxy(String proxy) {
        this.proxy = proxy;
    }

    public ApplicationConfig getApplication() {
        return application;
    }

    public void setApplication(ApplicationConfig application) {
        this.application = application;
    }

    public List<RegistryConfig> getRegistries() {
        return registries;
    }

    public void setRegistries(List<RegistryConfig> registries) {
        this.registries = registries;
    }
}
