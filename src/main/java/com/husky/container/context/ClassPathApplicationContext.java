package com.husky.container.context;

import com.husky.container.reader.BeanDefinitionReader;
import com.husky.container.entity.BeanDefinition;
import com.husky.container.reader.XMLBeanDefinitionReader;
import com.husky.container.exception.BeanInstantiationException;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
class ClassPathApplicationContext implements ApplicationContext {
    private BeanDefinitionReader beanReader;
    private Map<String, BeanDefinition> beanDefinitions;
    private Map<String, Object> beans;

    public ClassPathApplicationContext(String... paths) {
        this.beanReader = new XMLBeanDefinitionReader(paths);
        this.beanDefinitions = beanReader.readBeanDefinition();
        this.beans = new HashMap<>();
        initializeBeans();
    }

    @Override
    public <T> T getBean(Class<T> clazz) {
        for (Object bean : beans.values()) {
            if (clazz.isInstance(bean)) {
                return clazz.cast(bean);
            }
        }
        return null;
    }

    @Override
    public <T> T getBean(String id, Class<T> clazz) {
        Object bean = beans.get(id);
        if (clazz.isInstance(bean)) {
            return clazz.cast(bean);
        }
        return null;
    }

    @Override
    public Object getBean(String id) {
        return beans.get(id);
    }

    @Override
    public List<String> getBeans() {
        return new ArrayList<>(beans.keySet());
    }

    private void initializeBeans() {
        for (Map.Entry<String, BeanDefinition> entry : beanDefinitions.entrySet()) {
            String beanId = entry.getKey();
            BeanDefinition beanDefinition = entry.getValue();

            if (!beans.containsKey(beanId)) {
                Object beanInstance = createBeanFromBeanDefinition(beanDefinition);
                beans.put(beanId, beanInstance);
            }
        }
        injectDependencies();
    }

    Object createBeanFromBeanDefinition(BeanDefinition beanDefinition) {
        try {
            Class<?> beanClass = Class.forName(beanDefinition.getBeanClassName());
            return beanClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            log.error("Failed to create bean instance for class: {}", beanDefinition.getBeanClassName(), e);
            throw new BeanInstantiationException("Failed to create bean instance", e);
        }
    }

    private void injectDependencies() {
        for (Map.Entry<String, BeanDefinition> entry : beanDefinitions.entrySet()) {
            String beanId = entry.getKey();
            BeanDefinition beanDefinition = entry.getValue();
            Object beanInstance = beans.get(beanId);

            injectPropertyDependencies(beanDefinition.getDependencies(), beanInstance);
            injectRefDependencies(beanDefinition.getRefDependencies(), beanInstance);
        }
    }

    void injectPropertyDependencies(Map<String, String> dependencies, Object beanInstance) {
        for (Map.Entry<String, String> dependencyEntry : dependencies.entrySet()) {
            String propertyName = dependencyEntry.getKey();
            String propertyValue = dependencyEntry.getValue();
            setPropertyValue(beanInstance, propertyName, propertyValue);
        }
    }

    void injectRefDependencies(Map<String, String> refDependencies, Object beanInstance) {
        for (Map.Entry<String, String> refDependencyEntry : refDependencies.entrySet()) {
            String propertyName = refDependencyEntry.getKey();
            String refBeanId = refDependencyEntry.getValue();
            Object refBeanInstance = beans.get(refBeanId);
            setPropertyValue(beanInstance, propertyName, refBeanInstance);
        }
    }

    void setPropertyValue(Object beanInstance, String propertyName, Object propertyValue) {
        try {
            Class<?> beanClass = beanInstance.getClass();
            Field field = beanClass.getDeclaredField(propertyName);
            field.setAccessible(true);

            Class<?> fieldType = field.getType();
            Object convertedValue;

            if (propertyValue == null) {
                convertedValue = null;
            } else if (fieldType == String.class) {
                convertedValue = propertyValue.toString();
            } else if (fieldType == int.class) {
                convertedValue = Integer.parseInt((String) propertyValue);
            } else {
                convertedValue = convertValue(propertyValue, fieldType);
            }

            field.set(beanInstance, convertedValue);
        } catch (Exception e) {
            log.error("Failed to set property value for property: " +
                    propertyName + " on bean instance: " + beanInstance, e);
            throw new BeanInstantiationException("Invalid property value.");
        }
    }

    Object convertValue(Object value, Class<?> clazz) {
        if (value == null) {
            return null;
        } else if (clazz.isAssignableFrom(value.getClass())) {
            return value;
        } else {
            return clazz.cast(value);
        }
    }
}
