package com.bail.rpc.config.spring.consumer;

import com.bail.rpc.config.spring.po.ApplicationConfig;

/**
 * @Description：create
 * @author: ext.liukai3
 * @date: 2021/11/10 10:08
 */
public class ReferenceConfig<T> extends AbstractReferenceConfig{

    private String interfaceName;
    private Class<?> interfaceClass;
    private transient volatile T ref;
    private transient volatile boolean destroyed;
    private String protocol;

    protected ApplicationConfig application;



    private transient volatile boolean initialized;

    private ConsumerConfig consumer;

    public ConsumerConfig getConsumer() {
        return consumer;
    }

    public void setConsumer(ConsumerConfig consumer) {
        this.consumer = consumer;
    }

    public Class<?> getInterfaceClass() {
        if (interfaceClass != null) {
            return interfaceClass;
        }
        return interfaceClass;
    }

    public synchronized T get() {
        if (destroyed) {
            throw new IllegalStateException("Already destroyed!");
        }
        if (ref == null) {
            init();
        }
        return ref;
    }


    private void init(){
        if(initialized){
            return;
        }
        initialized = true;
        if(interfaceName == null || interfaceName.length() == 0){
            throw new IllegalStateException("<bail:reference interface=\"\" /> interface not allow null!");
        }
        checkDefault();
        appendProperties(this);
        if (getGeneric() == null && getConsumer() != null) {
            setGeneric(getConsumer().getGeneric());
        }
        try {
            interfaceClass = Class.forName(interfaceName, true, Thread.currentThread()
                    .getContextClassLoader());
        }catch (Exception e){
            throw new IllegalStateException(e.getMessage(), e);
        }
        if (consumer != null) {
            if (application == null) {
                application = consumer.getApplication();
            }
            if (module == null) {
                module = consumer.getModule();
            }
            if (registries == null) {
                registries = consumer.getRegistries();
            }
            if (monitor == null) {
                monitor = consumer.getMonitor();
            }
        }
    }

    private void checkDefault() {
        if (consumer == null) {
            consumer = new ConsumerConfig();
        }
        appendProperties(consumer);

    }


}
