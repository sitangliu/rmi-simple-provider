package com.bail.rpc.config.spring.consumer;

import com.bail.rpc.config.spring.common.Constants;
import com.bail.rpc.config.spring.common.URL;
import com.bail.rpc.config.spring.common.UrlUtils;
import com.bail.rpc.config.spring.po.ApplicationConfig;
import com.bail.rpc.config.spring.po.RegistryConfig;
import com.bail.rpc.config.spring.registry.RegistryService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description：create
 * @author: ext.liukai3
 * @date: 2022/2/16 18:13
 */
public class AbstractInterfaceConfig extends AbstractMethodConfig{
    private static final long serialVersionUID = -4327187091290376612L;

    protected ApplicationConfig application;
    protected List<RegistryConfig> registries;

    public ApplicationConfig getApplication() {
        return application;
    }

    public void setApplication(ApplicationConfig application) {
        this.application = application;
    }

    public List<RegistryConfig> getRegistries() {
        return registries;
    }

    public void setRegistries(List<RegistryConfig> registries) {
        this.registries = registries;
    }

    protected List<URL> loadRegistries(boolean provider) {
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
}
