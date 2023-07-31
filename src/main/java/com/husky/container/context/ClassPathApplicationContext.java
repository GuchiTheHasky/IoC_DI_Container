package com.husky.container.context;

import com.husky.container.entity.*;
import com.husky.container.exception.BeanInstantiationException;
import com.husky.container.exception.NoUniqueBeanDefinitionException;
import com.husky.container.reader.BeanDefinitionReader;
import com.husky.container.reader.dom.DOMBeanDefinitionReader;
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
        this.beans = new BeanCreator().createBeans(beanDefinitions);
    }

    @Override
    public <T> T getBean(Class<T> clazz) {
        validateClass(clazz);
        for (Bean bean : beans.values()) {
            if (clazz.isInstance(bean.getValue())) {
                return clazz.cast(bean.getValue());
            }
        }
        return null;
    }

    @Override
    public <T> T getBean(String id, Class<T> clazz) {
        validateId(id);
        validateClass(clazz);
        for (Bean bean : beans.values()) {
            if (bean.getId().equals(id) && clazz.isInstance(bean.getValue())) {
                return clazz.cast(bean.getValue());
            }
        }
        return null;
    }

    @Override
    public Object getBean(String id) {
        validateId(id);
        return beans.get(id).getValue();
    }

    @Override
    public List<String> getBeansNames() {
        return new ArrayList<>(beans.keySet());
    }

    void validateId(String id) {
        if (id == null || id.isEmpty()) {
            log.error("Bean id must not be null or empty");
            throw new BeanInstantiationException("Bean id must not be null or empty");
        }
    }

    void validateClass(Class<?> clazz) {
        if (clazz == null) {
            log.error("Bean class must not be null");
            throw new BeanInstantiationException("Bean class must not be null");
        }
        int sameClassCount = 0;
        for (Bean bean : beans.values()) {
            if (clazz.isInstance(bean.getValue())) {
                sameClassCount++;
            }
        }
        if (sameClassCount > 1) {
            log.error("No unique bean of class: {}", clazz.getName());
            throw new NoUniqueBeanDefinitionException("No unique bean of class: " + clazz.getName());
        }
    }
}