package com.husky.container.context;

import com.husky.container.entity.*;
import com.husky.container.reader.BeanDefinitionReader;
import com.husky.container.reader.dom.DOMBeanDefinitionReader;
import com.husky.container.reader.sax.SAXBeanDefinitionReader;
import com.husky.container.util.BeanCreator;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Slf4j
public class ClassPathApplicationContext implements ApplicationContext {
    private Map<String, Bean> beans;

    public ClassPathApplicationContext(String... paths) {
        this(new DOMBeanDefinitionReader(paths));
    }

    public ClassPathApplicationContext(BeanDefinitionReader beanReader) {
        List<BeanDefinition> beanDefinitions = beanReader.readBeanDefinition();
        this.beans = BeanCreator.createBeans(beanDefinitions);
    }

    @Override
    public <T> T getBean(Class<T> clazz) {
        // todo NoUniqueBeanDefinitionException
        for (Bean bean : beans.values()) {
            if (clazz.isInstance(bean.getValue())) {
                return (T) bean.getValue();
            }
        }
        return null;
    }

    @Override
    public <T> T getBean(String name, Class<T> clazz) {
        for (Bean bean : beans.values()) {
            if (bean.getId().equals(name) && clazz.isInstance(bean.getValue())) {
                return (T) bean.getValue();
            }
        }
        return null;
    }

    @Override
    public Object getBean(String id) {
        return beans.get(id).getValue();
    }

    @Override
    public List<String> getBeansNames() {
        return new ArrayList<>(beans.keySet());
    }

    private void validateId(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Bean id must not be null or empty");
        }
    }

    private void validateClass(Class<?> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("Bean class must not be null");
        }
        int sameClassCount = 0;
        for (Bean bean : beans.values()) {
            if (clazz.isInstance(bean.getValue())) {
                sameClassCount++;
            }
        }
        if (sameClassCount > 1) {
            throw new IllegalArgumentException("No unique bean of class " + clazz.getName());
        }
    }
}