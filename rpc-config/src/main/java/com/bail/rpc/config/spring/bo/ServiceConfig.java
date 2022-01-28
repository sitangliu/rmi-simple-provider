package com.bail.rpc.config.spring.bo;

import com.bail.rpc.config.spring.common.Constants;
import com.bail.rpc.config.spring.common.StringUtils;
import com.bail.rpc.config.spring.common.URL;
import com.bail.rpc.config.spring.common.UrlUtils;
import com.bail.rpc.config.spring.common.NetUtils;
import com.bail.rpc.config.spring.po.ApplicationConfig;
import com.bail.rpc.config.spring.po.ProtocolConfig;
import com.bail.rpc.config.spring.po.RegistryConfig;
import com.bail.rpc.config.spring.protocol.Protocol;
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

    private List<URL> loadRegistries(boolean provider) {
        List<URL> registryList = new ArrayList<URL>();
        if(registries != null && registries.size() > 0){
            for(RegistryConfig config: registries){
                String address = config.getAddress();
                if (address == null || address.length() == 0) {
                    address = Constants.ANYHOST_VALUE;
                }
                //系统属性配置注册中心
                String sysaddress = System.getProperty("bail.registry.address");
                if (sysaddress != null && sysaddress.length() > 0) {
                    address = sysaddress;
                }
                if (address != null && address.length() > 0
                        && !RegistryConfig.NO_AVAILABLE.equalsIgnoreCase(address)) {

                    Map<String, String> map = new HashMap<String, String>();
                    appendParameters(map,application);
                    appendParameters(map,config);
                    map.put("path", RegistryService.class.getName());
                    map.put(Constants.TIMESTAMP_KEY, String.valueOf(System.currentTimeMillis()));
                    if (!map.containsKey("protocol")) {
                        map.put("protocol", "bail");
                    }

                    List<URL> urls = UrlUtils.parseURLs(address, map);
                    for (URL url : urls) {
                        url = url.addParameter(Constants.REGISTER_KEY,url.getProtocol());
                        url = url.setProtocol(Constants.REGISTRY_PROTOCOL);
                        registryList.add(url);
                    }
                }
            }
        }
        return registryList;
    }

    private void appendParameters(Map<String, String> parameters, Object config) {
        appendParameters(parameters,config,null);
    }

    private void appendParameters(Map<String, String> parameters, Object config, String prefix) {
        if (config == null) {
            return;
        }
        //根据方法获取需要拼接的参数
        Method[] methods = config.getClass().getMethods();
        for (Method method : methods) {
            try {
                String name = method.getName();
                if ((name.startsWith("get") || name.startsWith("is"))
                        && !"getClass".equals(name)
                        && Modifier.isPublic(method.getModifiers())
                        && method.getParameterTypes().length == 0
                        && isPrimitive(method.getReturnType())){
                    int i = name.startsWith("get") ? 3 : 2;
                    String prop = StringUtils.camelToSplitName(name.substring(i, i + 1).toLowerCase() + name.substring(i + 1), ".");
                    String key = prop;

                    Object value = method.invoke(config, new Object[0]);
                    String str = String.valueOf(value).trim();
                    if (value != null && str.length() > 0) {
                        if (prefix != null && prefix.length() > 0) {
                            key = prefix + "." + key;
                        }
                        parameters.put(key, str);
                    }
                }else if("getParameters".equals(name)
                            && Modifier.isPublic(method.getModifiers())
                            && method.getParameterTypes().length == 0
                            && method.getReturnType() == Map.class){
                    Map<String, String> map = (Map<String, String>) method.invoke(config, new Object[0]);
                    if (map != null && map.size() > 0) {
                        String pre = (prefix != null && prefix.length() > 0 ? prefix + "." : "");
                        for (Map.Entry<String, String> entry : map.entrySet()) {
                            parameters.put(pre + entry.getKey().replace('-', '.'), entry.getValue());
                        }
                    }
                }
            }catch (Exception e){

            }


        }
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
