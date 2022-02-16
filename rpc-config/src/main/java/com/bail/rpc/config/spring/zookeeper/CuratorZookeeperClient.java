package com.bail.rpc.config.spring.zookeeper;

import com.bail.rpc.config.spring.common.URL;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.RetryNTimes;

/**
 * @Description：create
 * @author: ext.liukai3
 * @date: 2022/2/15 18:00
 */
public class CuratorZookeeperClient extends AbstractZookeeperClient<CuratorWatcher>{

    private final CuratorFramework client;

    public CuratorZookeeperClient(URL url) {
        super(url);
        try{
            CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
                    .connectString(url.getBackupAddress())
                    .retryPolicy(new RetryNTimes(1, 1000))
                    .connectionTimeoutMs(5000);
            String authority = url.getAuthority();
            if (authority != null && authority.length() > 0) {
                builder = builder.authorization("digest", authority.getBytes());
            }
            client = builder.build();
            client.getConnectionStateListenable().addListener(new ConnectionStateListener() {
                @Override
                public void stateChanged(CuratorFramework client, ConnectionState state) {
                    if(state == ConnectionState.LOST){
                        CuratorZookeeperClient.this.stateChanged(StateListener.DISCONNECTED);
                    }else if(state == ConnectionState.CONNECTED){
                        CuratorZookeeperClient.this.stateChanged(StateListener.CONNECTED);
                    }else if (state == ConnectionState.RECONNECTED) {
                        CuratorZookeeperClient.this.stateChanged(StateListener.RECONNECTED);
                    }
                }
            });
            client.start();
        }catch (Exception e){
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
