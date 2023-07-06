package com.husky.container.contract;

import com.husky.container.reader.XMLBeanDefinitionReader;

import java.util.List;

public interface ApplicationContext <T> {
    T getBean(Class<T> clazz);
    T getBean(String id, Class<T> clazz);
    Object getBean(String id);
    List<String> getBeans();
    void setBeanDefinitionReader(BeanDefinitionReader definitionReader);
}
