package com.bail.rpc.config.spring.schema;


import com.bail.rpc.config.spring.ServiceBean;
import com.bail.rpc.config.spring.po.ProtocolConfig;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.cglib.core.ReflectUtils;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import sun.reflect.misc.ReflectUtil;

import java.util.Objects;

/**
 * @Description：Bean解析器
 * @author: ext.liukai3
 * @date: 2022/1/19 20:54
 */
public class RpcBeanDefinitionParser implements BeanDefinitionParser {

    /**
     * 日志记录
     */
    private static final Logger logger = LoggerFactory.getLogger(RpcBeanDefinitionParser.class);

    private final Class<?> beanClass;
    private final boolean required;

    /**
     * 构造函数
     * @param beanClass
     * @param required
     */
    public RpcBeanDefinitionParser(Class<?> beanClass,boolean required){
        this.beanClass = beanClass;
        this.required = required;
    }

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        RootBeanDefinition beanDefinition = new RootBeanDefinition();
        beanDefinition.setBeanClass(beanClass);
        beanDefinition.setLazyInit(false);
        String id = element.getAttribute("id");
        //id必要
        if(StringUtils.isBlank(id) && required){
            String generatedBeanName = element.getAttribute("name");
            // 给generatedBeanName赋值
            if(StringUtils.isBlank(generatedBeanName)){
                if(ProtocolConfig.class.equals(beanClass)){
                    generatedBeanName = "blRpc";
                }else{
                    generatedBeanName = element.getAttribute("interface");
                }
            }
            if(StringUtils.isBlank(generatedBeanName)){
                generatedBeanName = beanClass.getName();
            }
            id = generatedBeanName;
            int counter = 2;
            while (parserContext.getRegistry().containsBeanDefinition(id)){
                id = generatedBeanName + (counter++);
            }
        }

        if(StringUtils.isNotBlank(id)){
            if(parserContext.getRegistry().containsBeanDefinition(id)){
                throw new IllegalStateException("Duplicate spring bean id" + id);
            }
            //注册BeanDefinition
            parserContext.getRegistry().registerBeanDefinition(id,beanDefinition);
            beanDefinition.getPropertyValues().addPropertyValue("id",id);
        }
        //针对不同的类进行处理
        if(ProtocolConfig.class.equals(beanClass)){
            for(String name: parserContext.getRegistry().getBeanDefinitionNames()){
                BeanDefinition definition = parserContext.getRegistry().getBeanDefinition(name);
                PropertyValue property = definition.getPropertyValues().getPropertyValue("protocol");
                if(property != null){
                    Object value = property.getValue();
                    if(value instanceof ProtocolConfig && id.equals(((ProtocolConfig) value).getName())){
                        definition.getPropertyValues().addPropertyValue("protocol",new RuntimeBeanReference(id));
                    }
                }
            }
        }else if (ServiceBean.class.equals(beanClass)){
            // 解析实现类
            String className = element.getAttribute("class");
            if(StringUtils.isNotBlank(className)){
                RootBeanDefinition classDefinition = new RootBeanDefinition();
                try{
                    classDefinition.setBeanClass(ReflectUtil.forName(className));
                }catch (ClassNotFoundException e){
                    logger.error(e.getMessage());
                }
                classDefinition.setLazyInit(false);
                parseProperties(element.getChildNodes(),classDefinition);
                beanDefinition.getPropertyValues().addPropertyValue("ref",new BeanDefinitionHolder(classDefinition,id+"impl"));
            }
        }

        return null;
    }

    private void parseProperties(NodeList childNodes, RootBeanDefinition classDefinition) {

    }
}
