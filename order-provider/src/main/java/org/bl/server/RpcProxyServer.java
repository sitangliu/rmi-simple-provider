package org.bl.server;

import org.bl.handler.ProcessorHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description：远程服务发布启动类
 * @author: ext.liukai3
 * @date: 2021/11/8 16:48
 */
public class RpcProxyServer {

    private final ExecutorService executorService= Executors.newCachedThreadPool();

    /**
     * 发布某个服务
     * @param service
     * @param port
     */
    public void publisher(Object service,int port){
        ServerSocket serverSocket = null;

        try{
            serverSocket = new ServerSocket(port);
            while (true){
                System.out.println("等待服务接入...");
                Socket socket = serverSocket.accept();
                executorService.execute(new ProcessorHandler(socket));
            }
        }catch (Exception e){
            System.out.println("发布服务失败："+e.getMessage());
        }finally {
            if(serverSocket!=null){
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
