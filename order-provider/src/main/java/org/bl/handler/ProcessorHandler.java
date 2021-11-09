package org.bl.handler;

import lombok.extern.slf4j.Slf4j;
import org.bl.container.Mediator;
import org.bl.rpc.RpcRequest;

import java.io.*;
import java.lang.reflect.Method;
import java.net.Socket;

/**
 * @Description：服务端处理器
 * @author: ext.liukai3
 * @date: 2021/11/8 15:39
 */
public class ProcessorHandler implements Runnable{

    /**
     * 网络连接，获取数据
     */
    private Socket socket;

    /**
     * 对应的远程接口
     */
    private Object service;

    public ProcessorHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        //从网络对象中获取数据流信息
        ObjectInputStream objectInputStream = null;
        ObjectOutputStream objectOutputStream = null;

        try{
            InputStream inputStream = socket.getInputStream();
            objectInputStream = new ObjectInputStream(inputStream);
            RpcRequest rpcRequest = (RpcRequest)objectInputStream.readObject();//进行对象反序列化
//            Object rs = invoke(rpcRequest);//调用本地服务，对请求数据进行业务处理
            //此处改为从容器中获取服务实例，并进行调用
            Mediator mediator = Mediator.getInstance();
            Object processor = mediator.processor(rpcRequest);

            OutputStream outputStream = socket.getOutputStream();
            objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(processor);//对对象进行序列化
        }catch (Exception e){
            System.err.println("线程处理任务出错："+e.getMessage());
        }finally {
            try {
                objectOutputStream.close();
                objectInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private Object invoke(RpcRequest rpcRequest) {

        String className = rpcRequest.getClassName();
        System.out.println("className:"+className);
        String methodName = rpcRequest.getMethodName();
        System.out.println("methodName:"+methodName);
        Object[] args = rpcRequest.getArgs();
        Class[] types = rpcRequest.getTypes();
        try {
            Class<?> aClass = Class.forName(className);
            Method method = aClass.getMethod(methodName, types);
            Object invoke = method.invoke(service, args);
            return invoke;
        }catch (Exception e){
            System.out.println("调用本地方法出错："+e.getMessage());
            e.printStackTrace();
        }

        return null;

    }


}
