package com.bail.rpc.config.spring.endpoint;

import com.bail.rpc.config.spring.channel.ChannelHandler;
import com.bail.rpc.config.spring.common.URL;
import com.bail.rpc.config.spring.exception.RemotingException;

import java.net.InetSocketAddress;

/**
 * @Description：通信端点
 * @author: ext.liukai3
 * @date: 2022/2/18 17:59
 */
public interface Endpoint {



    /**
     * get url.
     *
     * @return url
     */
    URL getUrl();


    /**
     * get channel handler.
     *
     * @return channel handler
     */
    ChannelHandler getChannelHandler();

    /**
     * get local address.
     *
     * @return local address.
     */
    InetSocketAddress getLocalAddress();

    /**
     * send message.
     *
     * @param message
     * @throws RemotingException
     */
    void send(Object message) throws RemotingException;

    /**
     * send message.
     *
     * @param message
     * @param sent    already sent to socket?
     */
    void send(Object message, boolean sent) throws RemotingException;

    /**
     * close the channel.
     */
    void close();

    /**
     * Graceful close the channel.
     */
    void close(int timeout);

    void startClose();

    /**
     * is closed.
     *
     * @return closed
     */
    boolean isClosed();
}
