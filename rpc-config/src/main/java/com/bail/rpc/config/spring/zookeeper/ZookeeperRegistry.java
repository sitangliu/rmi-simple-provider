package com.bail.rpc.config.spring.zookeeper;

import com.bail.rpc.config.spring.common.Constants;
import com.bail.rpc.config.spring.common.URL;
import com.bail.rpc.config.spring.registry.FailbackRegistry;

import java.util.List;

/**
 * @Description：create
 * @author: ext.liukai3
 * @date: 2022/2/16 17:25
 */
public class ZookeeperRegistry extends FailbackRegistry {

    private final static int DEFAULT_ZOOKEEPER_PORT = 2181;
    private final static String DEFAULT_ROOT = "dubbo";


    private final String root;

    private final ZookeeperClient zkClient;

    public ZookeeperRegistry(URL url,ZookeeperTransporter zookeeperTransporter){
        super(url);
        if (url.isAnyHost()) {
            throw new IllegalStateException("registry address == null");
        }
        String group = url.getParameter(Constants.GROUP_KEY, DEFAULT_ROOT);
        if (!group.startsWith(Constants.PATH_SEPARATOR)) {
            group = Constants.PATH_SEPARATOR + group;
        }
        this.root = group;
        zkClient = zookeeperTransporter.connect(url);
    }

    @Override
    public URL getUrl() {
        return null;
    }

    @Override
    public boolean isAvailable() {
        return false;
    }

    @Override
    public void destroy() {

    }

    @Override
    public void register(URL url) {

    }

    @Override
    public void unregister(URL url) {

    }

    @Override
    public void subscribe(URL url, String listener) {

    }

    @Override
    public void unsubscribe(URL url, String listener) {

    }

    @Override
    public List<URL> lookup(URL url) {
        return null;
    }
}
