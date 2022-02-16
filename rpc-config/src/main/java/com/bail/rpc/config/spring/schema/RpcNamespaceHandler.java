package com.bail.rpc.config.spring.schema;

import com.bail.rpc.config.spring.consumer.ReferenceBean;
import com.bail.rpc.config.spring.ServiceBean;
import com.bail.rpc.config.spring.po.ApplicationConfig;
import com.bail.rpc.config.spring.po.ProtocolConfig;
import com.bail.rpc.config.spring.po.RegistryConfig;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * @Description：远程服务命名空间处理器
 * @author: ext.liukai3
 * @date: 2021/11/9 17:35
 */
public class RpcNamespaceHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {
        registerBeanDefinitionParser("application",new RpcBeanDefinitionParser(ApplicationConfig.class,true));
        registerBeanDefinitionParser("registry",new RpcBeanDefinitionParser(RegistryConfig.class,true));
        registerBeanDefinitionParser("protocol",new RpcBeanDefinitionParser(ProtocolConfig.class,true));
        registerBeanDefinitionParser("service", new RpcBeanDefinitionParser(ServiceBean.class,true));
        registerBeanDefinitionParser("reference",new RpcBeanDefinitionParser(ReferenceBean.class,true));
    }
}
