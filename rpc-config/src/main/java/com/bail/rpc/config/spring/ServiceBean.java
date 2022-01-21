package com.bail.rpc.config.spring;

import com.bail.rpc.config.spring.bo.AbstractInterfaceConfig;
import com.bail.rpc.config.spring.bo.AbstractServiceConfig;
import com.bail.rpc.config.spring.bo.ServiceConfig;
import com.bail.rpc.config.spring.po.ApplicationConfig;
import com.bail.rpc.config.spring.po.ProtocolConfig;
import com.bail.rpc.config.spring.po.RegistryConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description：服务提供者
 * @author: ext.liukai3
 * @date: 2022/1/19 19:28
 */
public class ServiceBean<T> extends ServiceConfig<T> implements InitializingBean, DisposableBean, ApplicationContextAware, ApplicationListener<ContextRefreshedEvent>, BeanNameAware, Serializable {

    /**
     * 默认序列化
     */
    private static final long serialVersionUID = 213195494150089734L;

    /**
     * 应用上下文
     */
    private static transient ApplicationContext SPRING_CONTEXT;

    /**
     * 应用上下文
     */
    private transient ApplicationContext applicationContext;

    /**
     * beanName
     */
    private transient String beanName;

    /**
     * 是否支持监听器
     */
    private transient boolean supportedApplicationListener;


    public static ApplicationContext getSpringContext() {
        return SPRING_CONTEXT;
    }

    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    @Override
    public void destroy() throws Exception {

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //对象初始化之后，进行相关信息的加载，如applicationConfig、registry、protocol等
        //应用配置
        if(getApplication() == null){
            //获取对应的beanInstance
            Map<String, ApplicationConfig> applicationConfigMap = applicationContext == null ? null : BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, ApplicationConfig.class, false, false);
            if(applicationConfigMap != null && applicationConfigMap.size() >0){
                ApplicationConfig applicationConfig = null;
                for (ApplicationConfig config: applicationConfigMap.values()) {
                    applicationConfig = config;
                }
                if(applicationConfig != null){
                    setApplication(applicationConfig);
                }
            }

        }
        //注册中心
        if(getRegistries() == null){
            Map<String, RegistryConfig> registryConfigMap = applicationContext == null ? null : BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, RegistryConfig.class, false, false);
            List<RegistryConfig> registryConfigs = new ArrayList<RegistryConfig>();
            for (RegistryConfig config : registryConfigMap.values()) {
                    registryConfigs.add(config);
            }
            if (registryConfigs != null && !registryConfigs.isEmpty()) {
                super.setRegistries(registryConfigs);
            }

        }

        //发布协议
        if ((getProtocols() == null || getProtocols().isEmpty())) {
            Map<String, ProtocolConfig> protocolConfigMap = applicationContext == null ? null : BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, ProtocolConfig.class, false, false);
            if (protocolConfigMap != null && protocolConfigMap.size() > 0) {
                List<ProtocolConfig> protocolConfigs = new ArrayList<ProtocolConfig>();
                for (ProtocolConfig config : protocolConfigMap.values()) {
                    protocolConfigs.add(config);
                }
                if (protocolConfigs != null && !protocolConfigs.isEmpty()) {
                    super.setProtocols(protocolConfigs);
                }
            }
        }

        if(!isDelay()){
            export();
        }
    }

    /**
     * 对外发布服务
     */
    private synchronized void export() {

        doExport();

    }


    /**
     * 是否延迟加载
     * @return
     */
    private boolean isDelay() {
        Integer delay = getDelay();
        return supportedApplicationListener && (delay == null || delay == -1);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        if(applicationContext != null){
            SPRING_CONTEXT = applicationContext;
        }
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

    }
}
