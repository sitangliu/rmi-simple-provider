package com.bail.rpc.config.spring.consumer;

/**
 * @Description：抽象消费配置
 * @author: ext.liukai3
 * @date: 2022/2/16 18:09
 */
public abstract class AbstractReferenceConfig extends AbstractInterfaceConfig{

    // version
    protected String version;

    // group
    protected String group;

    // whether to use generic interface
    protected String generic;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getGeneric() {
        return generic;
    }

    public void setGeneric(String generic) {
        this.generic = generic;
    }
}
