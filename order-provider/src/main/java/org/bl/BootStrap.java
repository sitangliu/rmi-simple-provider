package org.bl;

import org.bl.server.RpcProxyServer;
import org.bl.service.IOrderService;
import org.bl.service.impl.OrderServiceImpl;

/**
 * @Description：启动类
 * @author: ext.liukai3
 * @date: 2021/11/8 16:54
 */
public class BootStrap {

    public static void main(String[] args) {

        //声明一个本地服务类
        IOrderService orderService = new OrderServiceImpl();
        RpcProxyServer rpcProxyServer = new RpcProxyServer();
        rpcProxyServer.publisher(orderService,8080);
    }

}
