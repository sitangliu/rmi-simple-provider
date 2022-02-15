package com.bail.rpc.config.spring;
/**
 * @Description：create
 * @author: ext.liukai3
 * @date: 2021/11/10 10:08
 */
public class ReferenceConfig<T> {

    private String interfaceName;
    private Class<?> interfaceClass;
    private transient volatile T ref;
    private transient volatile boolean destroyed;
    private String protocol;


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

    }
}
