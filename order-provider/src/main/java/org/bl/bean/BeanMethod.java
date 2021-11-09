package org.bl.bean;

import lombok.Data;

import java.lang.reflect.Method;

/**
 * @Description：方法级别的Bean
 * @author: ext.liukai3
 * @date: 2021/11/9 14:50
 */
@Data
public class BeanMethod {

    private Object bean;

    private Method method;

}
