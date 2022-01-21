package com.bail.rpc.config.spring.schema;


import com.bail.rpc.config.spring.ServiceBean;
import com.bail.rpc.config.spring.po.ProtocolConfig;
import com.bail.rpc.config.spring.po.RegistryConfig;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.cglib.core.ReflectUtils;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import sun.reflect.misc.ReflectUtil;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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

        Set<String> props = new HashSet<>();
        ManagedMap parameters = null;
        for(Method setter : beanClass.getMethods()){
            String name = setter.getName();
            if(name.length() > 3 && name.startsWith("set") && Modifier.isPublic(setter.getModifiers())
                    && setter.getParameterTypes().length == 1){
                Class<?> type = setter.getParameterTypes()[0];
                String property = name.substring(3, 4).toLowerCase();
                props.add(property);
                Method getter = null;
                try {
                    getter = beanClass.getMethod("get"+name.substring(3),new Class<?>[0]);
                } catch (NoSuchMethodException e) {
                    try {
                        getter = beanClass.getMethod("is" + name.substring(3), new Class<?>[0]);
                    } catch (NoSuchMethodException e2) {
                    }
                }
                if(getter == null || !Modifier.isPublic(getter.getModifiers()) || !type.equals(getter.getReturnType())){
                    continue;
                }
                if("parameters".equals(property)){
                    parameters = parseParameters(element.getChildNodes(),beanDefinition);
                }else if("methods".equals(property)){
                    parseMethods(id,element.getChildNodes(),beanDefinition,parserContext);
                }else if("arguments".equals(property)){
                    parseArguments(id,element.getChildNodes(),beanDefinition,parserContext);
                }else{
                    String value = element.getAttribute(property);
                    if(value != null){
                        value  = value.trim();
                        if(value.length()>0){
                            if("registry".equals(property) && RegistryConfig.NO_AVAILABLE.equalsIgnoreCase(value)){
                                RegistryConfig registryConfig = new RegistryConfig();
                                registryConfig.setAddress(RegistryConfig.NO_AVAILABLE);
                                beanDefinition.getPropertyValues().addPropertyValue(property,registryConfig);
                            }else if("registry".equals(property) && value.indexOf(",") != -1){
                                parseMultiRef("registries",value,beanDefinition,parserContext);
                            }else if("protocol".equals(property) && value.indexOf(",") != -1){
                                parseMultiRef("protocols",value,beanDefinition,parserContext);
                            }else{
                                Object reference;
                                if(type.isPrimitive()){
                                    reference = value;
                                }else if("protocol".equals(property)){
                                    ProtocolConfig protocol = new ProtocolConfig();
                                    protocol.setName(value);
                                    reference = protocol;
                                }else{
                                    if("ref".equals(property) && parserContext.getRegistry().containsBeanDefinition(value)){
                                        BeanDefinition refBean = parserContext.getRegistry().getBeanDefinition(value);
                                        if (!refBean.isSingleton()) {
                                            throw new IllegalStateException("The exported service ref " + value + " must be singleton! Please set the " + value + " bean scope to singleton, eg: <bean id=\"" + value + "\" scope=\"singleton\" ...>");
                                        }
                                    }
                                    reference = new RuntimeBeanReference(value);
                                }
                                beanDefinition.getPropertyValues().addPropertyValue(property,reference);
                            }
                        }
                    }
                }

            }
        }

        NamedNodeMap attributes = element.getAttributes();
        int len = attributes.getLength();
        for(int i= 0 ; i< len;i++) {
            Node node = attributes.item(i);
            String name = node.getLocalName();
            if (!props.contains(name)) {
                if (parameters == null) {
                    parameters = new ManagedMap();
                }
                String value = node.getNodeValue();
                parameters.put(name, new TypedStringValue(value, String.class));
            }
        }
        if(parameters != null){
            beanDefinition.getPropertyValues().addPropertyValue("parameters",parameters);
        }

        return beanDefinition;

    }

    private void parseMultiRef(String registries, String value, RootBeanDefinition beanDefinition, ParserContext parserContext) {

    }

    private void parseArguments(String id, NodeList childNodes, RootBeanDefinition beanDefinition, ParserContext parserContext) {

    }

    private void parseMethods(String id, NodeList childNodes, RootBeanDefinition beanDefinition, ParserContext parserContext) {

    }

    private ManagedMap parseParameters(NodeList childNodes, RootBeanDefinition beanDefinition) {
        return null;
    }

    private void parseProperties(NodeList childNodes, RootBeanDefinition classDefinition) {

    }
}
