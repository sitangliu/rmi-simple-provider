package com.bail.rpc.config.spring.bo;

import com.bail.rpc.config.spring.consumer.AbstractInterfaceConfig;
import com.bail.rpc.config.spring.po.ProtocolConfig;

import java.util.List;

/**
 * @Description：服务配置
 * @author: ext.liukai3
 * @date: 2022/1/21 15:18
 */
public abstract class AbstractServiceConfig extends AbstractInterfaceConfig {

    /**
     * 序列化默认值
     */
    private static final long serialVersionUID = -4140295880332496651L;

    /**
     * 协议配置
     */
    protected List<ProtocolConfig> protocols;

    // whether to register
    /**
     * 是否已注册
     */
    private Boolean register;

    /**
     * 是否延迟加载
     */
    protected Integer delay;

    public List<ProtocolConfig> getProtocols() {
        return protocols;
    }

    public void setProtocols(List<ProtocolConfig> protocols) {
        this.protocols = protocols;
    }

    public Boolean getRegister() {
        return register;
    }

    public void setRegister(Boolean register) {
        this.register = register;
    }

    public Integer getDelay() {
        return delay;
    }

    public void setDelay(Integer delay) {
        this.delay = delay;
    }
}
