package com.bail.rpc.config.spring.consumer;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description：create
 * @author: ext.liukai3
 * @date: 2022/2/17 20:15
 */
public class ConsumerModel {

    private ReferenceConfig metadata;
    private Object proxyObject;
    private String serviceName;

    private final Map<Method, ConsumerMethodModel> methodModels = new IdentityHashMap<Method, ConsumerMethodModel>();

    public ConsumerModel(String serviceName,ReferenceConfig metadata, Object proxyObject, Method[] methods) {
        this.serviceName = serviceName;
        this.metadata = metadata;
        this.proxyObject = proxyObject;

        if (proxyObject != null) {
            for (Method method : methods) {
                methodModels.put(method, new ConsumerMethodModel(method, metadata));
            }
        }
    }


    /**
     * Return service metadata for consumer
     *
     * @return service metadata
     */
    public ReferenceConfig getMetadata() {
        return metadata;
    }

    public Object getProxyObject() {
        return proxyObject;
    }

    /**
     * Return method model for the given method on consumer side
     *
     * @param method method object
     * @return method model
     */
    public ConsumerMethodModel getMethodModel(Method method) {
        return methodModels.get(method);
    }

    /**
     * Return all method models for the current service
     *
     * @return method model list
     */
    public List<ConsumerMethodModel> getAllMethods() {
        return new ArrayList<ConsumerMethodModel>(methodModels.values());
    }

    public String getServiceName() {
        return serviceName;
    }
}
