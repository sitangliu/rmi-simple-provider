package com.bail.rpc.config.spring.po;

/**
 * @Description：应用配置
 * @author: ext.liukai3
 * @date: 2022/1/19 20:10
 */
public class ApplicationConfig extends AbstractConfig{

    /**
     * 序列化ID
     */
    private static final long serialVersionUID = 3955165080582783048L;

    /**
     * 应用名称
     */
    private String name;

    /**
     * 应用版本号
     */
    private String version;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
