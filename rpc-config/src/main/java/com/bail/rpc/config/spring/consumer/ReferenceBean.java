package com.bail.rpc.config.spring.consumer;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @Description：create
 * @author: ext.liukai3
 * @date: 2021/11/10 10:16
 */
public class ReferenceBean<T> extends ReferenceConfig<T> implements FactoryBean, ApplicationContextAware
    , InitializingBean, DisposableBean {

    private transient ApplicationContext applicationContext;

    /**
     * 提供对象实例化的方法
     * @return
     * @throws Exception
     */
    @Override
    public Object getObject() throws Exception {
        //提供获取对象实例的方法
        return get();
    }

    @Override
    public Class<?> getObjectType() {
        return getInterfaceClass();
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 初始化之后
     * bean
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {

    }

    /**
     * bean关闭之后
     * @throws Exception
     */
    @Override
    public void destroy() throws Exception {

    }
}
