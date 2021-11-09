package org.bl;

import org.bl.server.RpcProxyServer;
import org.bl.service.IOrderService;
import org.bl.service.impl.OrderServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @Description：启动类
 * @author: ext.liukai3
 * @date: 2021/11/8 16:54
 */
@Configuration
@ComponentScan("org.bl")
public class BootStrap {

    public static void main(String[] args) {

        ApplicationContext applicationContext =
                new AnnotationConfigApplicationContext(BootStrap.class);


        //声明一个本地服务类
//        IOrderService orderService = new OrderServiceImpl();
//        RpcProxyServer rpcProxyServer = new RpcProxyServer();
//        rpcProxyServer.publisher(orderService,8080);
    }

}
