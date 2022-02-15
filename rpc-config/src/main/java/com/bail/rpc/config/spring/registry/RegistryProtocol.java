package com.bail.rpc.config.spring.registry;

import com.bail.rpc.config.spring.common.Constants;
import com.bail.rpc.config.spring.common.URL;
import com.bail.rpc.config.spring.exception.RpcException;
import com.bail.rpc.config.spring.exporter.Exporter;
import com.bail.rpc.config.spring.listener.NotifyListener;
import com.bail.rpc.config.spring.protocol.BailProtocol;
import com.bail.rpc.config.spring.protocol.Protocol;
import com.bail.rpc.config.spring.proxy.Invoker;
import com.bail.rpc.config.spring.proxy.InvokerWrapper;
import com.bail.rpc.config.spring.support.ProviderConsumerRegTable;

import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description：create
 * @author: ext.liukai3
 * @date: 2022/1/29 11:23
 */
public class RegistryProtocol implements Protocol {

    private final Map<String, ExporterChangeableWrapper<?>> bounds = new ConcurrentHashMap<String, ExporterChangeableWrapper<?>>();

    public static final String QOS_ENABLE = "qos.enable";

    public static final String QOS_PORT = "qos.port";

    public static final String ACCEPT_FOREIGN_IP = "qos.accept.foreign.ip";

    private final Map<URL,NotifyListener> overrideListeners = new ConcurrentHashMap<URL,NotifyListener>();

    private Protocol  bailProtocol= new BailProtocol();

    private Registry registry = new ZookeeperRegistry();

    @Override
    public int getDefaultPort() {
        return 0;
    }

    @Override
    public <T> Exporter<T> export(final Invoker<T> originInvoker) throws RpcException {
        ExporterChangeableWrapper<T> exporter = doLocalExport(originInvoker);
        //获取注册url
        final URL  registryUrl = getRegistryUrl(originInvoker);
        final URL registedProviderUrl = getRegistedProviderUrl(originInvoker);

        boolean register = registedProviderUrl.getParameter("register", true);
        ProviderConsumerRegTable.registerProvider(originInvoker, registryUrl, registedProviderUrl);
        if(register){
            register(registryUrl,registedProviderUrl);
            ProviderConsumerRegTable.getProviderWrapper(originInvoker).setReg(true);
        }

        //订阅覆盖数据
//        final URL overrideSubscribedUrl = getSubscribedOverrideUrl(registedProviderUrl);
//        OverrideListener overrideSubscribeListener = new OverrideListener(overrideSubscribedUrl, originInvoker);
//        overrideListeners.put(overrideSubscribedUrl,overrideSubscribeListener);
//        registry.subscribe(overrideSubscribeUrl,overrideSubscribeListener);

        return null;
    }

    /**
     * 往注册中心注册提供者地址
     * @param registryUrl
     * @param registedProviderUrl
     */
    private void register(URL registryUrl, URL registedProviderUrl) {

    }

    private <T> URL getRegistedProviderUrl(Invoker<T> originInvoker) {
        URL providerUrl = getProviderUrl(originInvoker);
        final URL registedProviderUrl = providerUrl.removeParameters(getFilteredKeys(providerUrl))
                .removeParameter(Constants.MONITOR_KEY)
                .removeParameter(Constants.BIND_IP_KEY)
                .removeParameter(Constants.BIND_PORT_KEY)
                .removeParameter(QOS_ENABLE)
                .removeParameter(QOS_PORT)
                .removeParameter(ACCEPT_FOREIGN_IP);
        return registedProviderUrl;
        return null;
    }

    private <T> URL getRegistryUrl(Invoker<T> originInvoker) {
        URL registryUrl = originInvoker.getUrl();
        if (Constants.REGISTRY_PROTOCOL.equals(registryUrl.getProtocol())) {
            String protocol = registryUrl.getParameter(Constants.REGISTRY_KEY, Constants.DEFAULT_DIRECTORY);
            registryUrl = registryUrl.setProtocol(protocol).removeParameter(Constants.REGISTRY_KEY);
        }
        return registryUrl;
    }

    //Filter the parameters that do not need to be output in url(Starting with .)
    private static String[] getFilteredKeys(URL url) {
        Map<String, String> params = url.getParameters();
        if (params != null && !params.isEmpty()) {
            List<String> filteredKeys = new ArrayList<String>();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (entry != null && entry.getKey() != null && entry.getKey().startsWith(Constants.HIDE_KEY_PREFIX)) {
                    filteredKeys.add(entry.getKey());
                }
            }
            return filteredKeys.toArray(new String[filteredKeys.size()]);
        } else {
            return new String[]{};
        }
    }

    private <T> ExporterChangeableWrapper<T> doLocalExport(Invoker<T> originInvoker) {
        String key = getCacheKey(originInvoker);
        ExporterChangeableWrapper<T> exporter = (ExporterChangeableWrapper<T>)bounds.get(key);
        if(exporter == null){
            synchronized (bounds){
                exporter = (ExporterChangeableWrapper<T>)bounds.get(key);
                if(exporter == null){
                    final InvokerDelegete<T> invokerDelegete = new InvokerDelegete<>(originInvoker, getProviderUrl(originInvoker));
                    exporter = new ExporterChangeableWrapper<>((Exporter<T>) bailProtocol.export(invokerDelegete), originInvoker);
                    bounds.put(key,exporter);
                }
            }
        }
        return exporter;
    }

    private  String getCacheKey(Invoker<?> originInvoker) {
        URL providerUrl = getProviderUrl(originInvoker);
        String key = providerUrl.removeParameters("dynamic","enabled").toFullString();
        return key;
    }

    private URL getProviderUrl(Invoker<?> origininvoker) {
        String export = origininvoker.getUrl().getParameterAndDecoded(Constants.EXPORT_KEY);
        if (export == null || export.length() == 0) {
            throw new IllegalArgumentException("The registry export url is null! registry: " + origininvoker.getUrl());
        }

        URL providerUrl = URL.valueOf(export);
        return providerUrl;
    }

    @Override
    public <T> Invoker<T> refer(Class<T> type, URL url) throws RpcException {
        return null;
    }

    @Override
    public void destroy() {

    }

    private class ExporterChangeableWrapper<T> implements Exporter<T> {

        private final Invoker<T> originInvoker;
        private Exporter<T> exporter;

        public ExporterChangeableWrapper(Exporter<T> exporter,Invoker<T> originInvoker){
            this.originInvoker = originInvoker;
            this.exporter = exporter;
        }

        @Override
        public Invoker<T> getInvoker() {
            return originInvoker;
        }

        @Override
        public void unexport() {
            String key = getCacheKey(this.originInvoker);
            bounds.remove(key);
            exporter.unexport();
        }
    }

    public static class InvokerDelegete<T> extends InvokerWrapper<T> {
        private final Invoker<T> invoker;

        /**
         * @param invoker
         * @param url     invoker.getUrl return this value
         */
        public InvokerDelegete(Invoker<T> invoker, URL url) {
            super(invoker, url);
            this.invoker = invoker;
        }

        public Invoker<T> getInvoker() {
            if (invoker instanceof InvokerDelegete) {
                return ((InvokerDelegete<T>) invoker).getInvoker();
            } else {
                return invoker;
            }
        }
    }

    private class OverrideListener implements NotifyListener{

        private final URL subscribeUrl;
        private final Invoker originInvoker;

        public OverrideListener(URL subscribeUrl,Invoker originInvoker){
            this.subscribeUrl = subscribeUrl;
            this.originInvoker = originInvoker;
        }

        @Override
        public synchronized void notify(List<URL> urls) {

        }
    }

}
