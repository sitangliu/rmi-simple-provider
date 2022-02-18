package com.bail.rpc.config.spring.channel;

import com.bail.rpc.config.spring.endpoint.Endpoint;
import com.bail.rpc.config.spring.exception.RemotingException;

/**
 * @Description：create
 * @author: ext.liukai3
 * @date: 2022/2/18 18:23
 */
public interface Client extends Endpoint,Channel,Resetable {


    /**
     * reconnect.
     */
    void reconnect() throws RemotingException;

}
