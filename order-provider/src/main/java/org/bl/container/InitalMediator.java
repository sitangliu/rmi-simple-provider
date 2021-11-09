package org.bl.container;

import org.bl.annotation.BlRemoteService;
import org.bl.bean.BeanMethod;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @Description：create
 * @author: ext.liukai3
 * @date: 2021/11/9 15:10
 */
@Component
public class InitalMediator implements BeanPostProcessor {

    /**
     * bean实例化之后，对bean进行处理
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        if(bean.getClass().isAnnotationPresent(BlRemoteService.class)) {
            //加了服务发布标记的bean进行远程发布
            Method[] declaredMethods = bean.getClass().getDeclaredMethods();
            for (Method method: declaredMethods) {
                String key=bean.getClass().getInterfaces()[0].getName()+"."+method.getName();
                BeanMethod beanMethod=new BeanMethod();
                beanMethod.setBean(bean);
                beanMethod.setMethod(method);
                Mediator.map.put(key,beanMethod);
            }

        }
        return bean;
    }
}
