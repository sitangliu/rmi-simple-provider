package org.bl.server;

import org.bl.handler.ProcessorHandler;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description：create
 * @author: ext.liukai3
 * @date: 2021/11/9 15:21
 */
@Component
public class SocketServerInitial implements ApplicationListener<ContextRefreshedEvent> {
    private final ExecutorService executorService= Executors.newCachedThreadPool();

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        //启动socket服务
        ServerSocket serverSocket = null;
        try{
            serverSocket = new ServerSocket(8090);
            while(true){
                Socket socket=serverSocket.accept(); //监听客户端请求
                executorService.execute(new ProcessorHandler(socket));
            }
        }catch (Exception e){
            System.out.println("启动服务出错："+e.getMessage());
        }
    }
}
