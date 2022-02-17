package com.bail.rpc.config.spring.bo;

import com.bail.rpc.config.spring.common.Constants;
import com.bail.rpc.config.spring.common.StringUtils;
import com.bail.rpc.config.spring.common.URL;
import com.bail.rpc.config.spring.common.UrlUtils;
import com.bail.rpc.config.spring.exporter.Exporter;
import com.bail.rpc.config.spring.po.ProtocolConfig;
import com.bail.rpc.config.spring.po.RegistryConfig;
import com.bail.rpc.config.spring.protocol.Protocol;
import com.bail.rpc.config.spring.proxy.DelegateProviderMetaDataInvoker;
import com.bail.rpc.config.spring.proxy.Invoker;
import com.bail.rpc.config.spring.proxy.JdkProxyFactory;
import com.bail.rpc.config.spring.proxy.ProxyFactory;
import com.bail.rpc.config.spring.registry.RegistryProtocol;
import com.bail.rpc.config.spring.registry.RegistryService;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bail.rpc.config.spring.common.NetUtils.LOCALHOST;

/**
 * @Description：服务配置
 * @author: ext.liukai3
 * @date: 2022/1/21 20:30
 */
public class ServiceConfig<T> extends AbstractServiceConfig{

    private T ref;
    private String interfaceName;
    private Class<?> interfaceClass;

    private ProxyFactory proxyFactory = new JdkProxyFactory();

    private Protocol registryProtocol = new RegistryProtocol();

    /**
     * 协议集合
     */
    private static Map<String,Protocol> protocolMap = new HashMap<>();

    /**
     * URL信息
     */
    private final List<URL> urls = new ArrayList<URL>();

    // service name
    private String path;
    /**
     * 发布服务
     */
    protected void doExport() {

        doExportUrls();

    }

    private void doExportUrls() {

        List<URL> registryURLs = loadRegistries(false);
        //根据协议和注册中心发布服务
        for (ProtocolConfig protocolConfig : protocols) {
            doExportUrlsFor1Protocol(protocolConfig,registryURLs);
        }

    }

    private void doExportUrlsFor1Protocol(ProtocolConfig protocolConfig, List<URL> registryURLs) {

        String name = protocolConfig.getName();
        if (name == null || name.length() == 0) {
            name = "bail";
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put(Constants.SIDE_KEY, Constants.PROVIDER_SIDE);
        map.put(Constants.BAIL_VERSION_KEY, "1.0.0");
        map.put(Constants.TIMESTAMP_KEY, String.valueOf(System.currentTimeMillis()));
        appendParameters(map,application);
        appendParameters(map,protocolConfig);//bailProtocol
        appendParameters(map,this);
        String contextPath = protocolConfig.getContextpath();
        String host = protocolConfig.getHost();
        Integer port = Integer.valueOf(protocolConfig.getPort());

        URL url = new URL(name,host,port,contextPath+"/"+path,map);

        //首先发布本地服务
        //exportLocal(url);
        try{
            for(URL registryURL: registryURLs){
                Invoker invoker = proxyFactory.getInvoker(ref, (Class) interfaceClass,
                        registryURL.addParameterAndEncoded(Constants.EXPORT_KEY,url.toFullString()));

                DelegateProviderMetaDataInvoker wrapperInvoker = new DelegateProviderMetaDataInvoker(invoker, this);
                Exporter exporter= registryProtocol.export(wrapperInvoker);

            }



        }catch (Exception e){

        }




    }

    /**
     * 在本地发布服务
     * @param url
     */
    private void exportLocal(URL url) {
        URL local = URL.valueOf(url.toFullString())
                .setProtocol(Constants.LOCAL_PROTOCOL)
                .setHost(LOCALHOST)
                .setPort(0);
        //生成本地服务代理类及其调用者
    }




    public Class<?> getInterfaceClass() {
        if (interfaceClass != null) {
            return interfaceClass;
        }
        if (ref instanceof GenericService) {
            return GenericService.class;
        }
        try {
            if (interfaceName != null && interfaceName.length() > 0) {
                this.interfaceClass = Class.forName(interfaceName, true, Thread.currentThread()
                        .getContextClassLoader());
            }
        } catch (ClassNotFoundException t) {
            throw new IllegalStateException(t.getMessage(), t);
        }
        return interfaceClass;
    }

    private static boolean isPrimitive(Class<?> type) {
        return type.isPrimitive()
                || type == String.class
                || type == Character.class
                || type == Boolean.class
                || type == Byte.class
                || type == Short.class
                || type == Integer.class
                || type == Long.class
                || type == Float.class
                || type == Double.class
                || type == Object.class;
    }
}
