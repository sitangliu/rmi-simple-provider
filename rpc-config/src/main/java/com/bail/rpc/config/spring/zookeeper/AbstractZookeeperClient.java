package com.bail.rpc.config.spring.zookeeper;

import com.bail.rpc.config.spring.common.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @Description：create
 * @author: ext.liukai3
 * @date: 2022/2/15 18:04
 */
public class AbstractZookeeperClient implements ZookeeperClient{

    protected static final Logger logger = LoggerFactory.getLogger(AbstractZookeeperClient.class);

    private final URL url;

    public AbstractZookeeperClient(URL url) {
        this.url = url;
    }

    @Override
    public void create(String path, boolean ephemeral) {

    }

    @Override
    public void delete(String path) {

    }

    @Override
    public List<String> getChildren(String path) {
        return null;
    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public void close() {

    }

    @Override
    public URL getUrl() {
        return null;
    }
}
