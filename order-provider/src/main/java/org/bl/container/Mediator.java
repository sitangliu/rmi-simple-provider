package org.bl.container;

import org.bl.bean.BeanMethod;
import org.bl.rpc.RpcRequest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description：服务容器
 * @author: ext.liukai3
 * @date: 2021/11/9 14:50
 */
public class Mediator {

    //用来存储发布的服务的实例(服务调用的路由)
    public static Map<String,BeanMethod> map=new ConcurrentHashMap<>();

    private volatile static Mediator instance;

    private Mediator(){}

    public static Mediator getInstance(){
        if(instance==null){
            synchronized (Mediator.class){
                if(instance==null){
                    instance=new Mediator();
                }
            }
        }
        return instance;
    }

    /**
     * 容器中获取服务实例，并进行方法调用
     * @param request
     * @return
     */
    public Object processor(RpcRequest request){
        String key = request.getClassName()+"."+request.getMethodName();
        BeanMethod beanMethod = map.get(key);
        if(beanMethod == null){
            return null;
        }
        Object bean = beanMethod.getBean();
        Method method = beanMethod.getMethod();
        try {
            return method.invoke(bean,request.getArgs());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

}
