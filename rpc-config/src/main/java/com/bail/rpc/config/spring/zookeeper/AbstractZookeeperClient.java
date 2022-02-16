package com.bail.rpc.config.spring.zookeeper;

import com.bail.rpc.config.spring.common.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @Description：create
 * @author: ext.liukai3
 * @date: 2022/2/15 18:04
 */
public class AbstractZookeeperClient<T> implements ZookeeperClient{

    protected static final Logger logger = LoggerFactory.getLogger(AbstractZookeeperClient.class);

    private final Set<StateListener> stateListeners = new CopyOnWriteArraySet<StateListener>();

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

    protected void stateChanged(int state) {
        for (StateListener sessionListener : getSessionListeners()) {
            sessionListener.stateChanged(state);
        }
    }

    public Set<StateListener> getSessionListeners() {
        return stateListeners;
    }
}
