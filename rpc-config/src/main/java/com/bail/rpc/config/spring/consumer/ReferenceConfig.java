package com.bail.rpc.config.spring.consumer;

import com.bail.rpc.config.spring.common.*;
import com.bail.rpc.config.spring.container.ApplicationModel;
import com.bail.rpc.config.spring.po.ApplicationConfig;
import com.bail.rpc.config.spring.po.RegistryConfig;
import com.bail.rpc.config.spring.protocol.Protocol;
import com.bail.rpc.config.spring.proxy.Invoker;
import com.bail.rpc.config.spring.proxy.JdkProxyFactory;
import com.bail.rpc.config.spring.proxy.ProxyFactory;
import com.bail.rpc.config.spring.registry.RegistryProtocol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description：create
 * @author: ext.liukai3
 * @date: 2021/11/10 10:08
 */
public class ReferenceConfig<T> extends AbstractReferenceConfig{

    private String interfaceName;
    private Class<?> interfaceClass;

    private transient volatile T ref;
    private transient volatile Invoker<?> invoker;
    private transient volatile boolean destroyed;

    private static final ProxyFactory proxyFactory = new JdkProxyFactory();

    private String protocol;

    private String url;
    private final List<URL> urls = new ArrayList<URL>();

    private static final Protocol refprotocol = new RegistryProtocol();

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

            if (registries == null) {
                registries = consumer.getRegistries();
            }
        }

        if (application != null) {
            if (registries == null) {
                registries = application.getRegistries();
            }
        }

        Map<String, String> map = new HashMap<String, String>();
        Map<Object, Object> attributes = new HashMap<Object, Object>();
        map.put(Constants.SIDE_KEY, Constants.CONSUMER_SIDE);
        map.put(Constants.DUBBO_VERSION_KEY, "1.0.0");
        map.put(Constants.TIMESTAMP_KEY, String.valueOf(System.currentTimeMillis()));
        if (ConfigUtils.getPid() > 0) {
            map.put(Constants.PID_KEY, String.valueOf(ConfigUtils.getPid()));
        }
        map.put(Constants.INTERFACE_KEY, interfaceName);
        appendParameters(map, application);
        appendParameters(map, consumer, Constants.DEFAULT_KEY);
        appendParameters(map, this);
        String prefix = StringUtils.getServiceKey(map);
        String hostToRegistry = ConfigUtils.getSystemProperty(Constants.DUBBO_IP_TO_REGISTRY);
        if (hostToRegistry == null || hostToRegistry.length() == 0) {
            hostToRegistry = NetUtils.getLocalHost();
        }
        map.put(Constants.REGISTER_IP_KEY, hostToRegistry);
        ref = createProxy(map);
        ConsumerModel consumerModel = new ConsumerModel(getUniqueServiceName(), this, ref, interfaceClass.getMethods());
        ApplicationModel.initConsumerModel(getUniqueServiceName(),consumerModel);
    }

    private T createProxy(Map<String, String> map) {
        URL tmpUrl = new URL("temp", "localhost", 0, map);
        if (url != null && url.length() > 0) { // user specified URL, could be peer-to-peer address, or register center's address.
            String[] us = Constants.SEMICOLON_SPLIT_PATTERN.split(url);
            if (us != null && us.length > 0) {
                for (String u : us) {
                    URL url = URL.valueOf(u);
                    if (url.getPath() == null || url.getPath().length() == 0) {
                        url = url.setPath(interfaceName);
                    }
                    if (Constants.REGISTRY_PROTOCOL.equals(url.getProtocol())) {
                        urls.add(url.addParameterAndEncoded(Constants.REFER_KEY, StringUtils.toQueryString(map)));
                    } else {
                        urls.add(ClusterUtils.mergeUrl(url, map));
                    }
                }
            }
        }else{
            List<URL> us = loadRegistries(false);
            if (us != null && !us.isEmpty()) {
                for (URL u : us) {
                    urls.add(u.addParameterAndEncoded(Constants.REFER_KEY, StringUtils.toQueryString(map)));
                }
            }
        }
        if(urls.size() == 1){
            invoker = refprotocol.refer(interfaceClass, urls.get(0));
        }else{
            List<Invoker<?>> invokers = new ArrayList<Invoker<?>>();
            URL registryURL = null;
            for (URL url : urls) {
                invokers.add(refprotocol.refer(interfaceClass, url));
                if (Constants.REGISTRY_PROTOCOL.equals(url.getProtocol())) {
                    registryURL = url; // use last registry url
                }
            }
//            if (registryURL != null) { // registry url is available
//                // use AvailableCluster only when register's cluster is available
//                URL u = registryURL.addParameter(Constants.CLUSTER_KEY, AvailableCluster.NAME);
//                invoker = cluster.join(new StaticDirectory(u, invokers));
//            } else { // not a registry url
//                invoker = cluster.join(new StaticDirectory(invokers));
//            }
        }

        return (T)proxyFactory.getProxy(invoker);
    }

    public String getUniqueServiceName() {
        StringBuilder buf = new StringBuilder();
        if (group != null && group.length() > 0) {
            buf.append(group).append("/");
        }
        buf.append(interfaceName);
        if (version != null && version.length() > 0) {
            buf.append(":").append(version);
        }
        return buf.toString();
    }

    private void checkDefault() {
        if (consumer == null) {
            consumer = new ConsumerConfig();
        }
        appendProperties(consumer);

    }


}
