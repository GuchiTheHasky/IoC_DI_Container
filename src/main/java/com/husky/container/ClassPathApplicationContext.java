package com.husky.container;

import com.husky.container.contract.ApplicationContext;
import com.husky.container.contract.BeanDefinitionReader;
import com.husky.container.entity.Bean;
import com.husky.container.entity.BeanDefinition;
import com.husky.container.util.BeanInstantiationException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClassPathApplicationContext<T> implements ApplicationContext<T> {
    private String[] paths;
    private BeanDefinitionReader beanReader;
    private Map<String, BeanDefinition> beanDefinitions;
    private List<Bean> beans;

    @Override
    public T getBean(Class<T> clazz) {
        for (Bean bean : beans) {
            if (clazz.isInstance(bean)) {
                return (T) bean;
            }
        }
        return null;
    }

    @Override
    public T getBean(String id, Class<T> clazz) {
        for (Bean bean : beans) {
            if (bean.getId().equals(id) && clazz.isInstance(bean)) {
                return (T) bean;
            }
        }
        return null;
    }

    @Override
    public Object getBean(String id) {
        for (Bean bean : beans) {
            if (bean.getId().equals(id)) {
                return (T) bean;
            }
        }
        return null;
    }

    @Override
    public List<String> getBeans() {
        List<String> beanIds = new ArrayList<>();
        for (Bean bean : beans) {
            beanIds.add(bean.getId());
        }
        return beanIds;
    }

    @Override
    public void setBeanDefinitionReader(BeanDefinitionReader definitionReader) {
        this.beanReader = definitionReader;
    }

    public void createBeansFromBeanDefinition() {
        beans = new ArrayList<>();

        for (BeanDefinition beanDefinition : beanDefinitions.values()) {
            Object beanInstance;
            try {
                Class<?> beanClass = Class.forName(beanDefinition.getBeanClassName());
                beanInstance = beanClass.newInstance();

                if (beanInstance instanceof Bean) {
                    Bean bean = (Bean) beanInstance;
                    bean.setValue(beanDefinition.getBeanClassName());
                    bean.setId(beanDefinition.getId());

                    beans.add(bean);
                }
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                throw new BeanInstantiationException("Failed to create bean instance for " + beanDefinition.getId(), e);
            }
        }
    }


    public void injectDependencies() {
        for (Bean bean : beans) {
            BeanDefinition beanDefinition = getBeanDefinition(bean.getId());
            Map<String, Object> dependencies = bean.getDependencies(beanDefinition, this);

            for (Map.Entry<String, Object> entry : dependencies.entrySet()) {
                String propertyName = entry.getKey();
                Object dependency = entry.getValue();

                injectDependency(bean.getValue(), propertyName, dependency);
            }
        }
    }

    public void loadBeanDefinitions() {
        beanDefinitions = beanReader.readBeanDefinition();
    }

    private void injectDependency(Object target, String propertyName, Object dependency) {
        try {
            String setterMethodName = "set" + capitalize(propertyName);
            Method[] methods = target.getClass().getMethods();

            for (Method method : methods) {
                if (method.getName().equals(setterMethodName)) {
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    if (parameterTypes.length == 1) {
                        if (parameterTypes[0].isAssignableFrom(dependency.getClass())) {
                            method.invoke(target, dependency);
                            return;
                        }
                    }
                }
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new BeanInstantiationException("Failed to inject dependency for property " + propertyName, e);
        }
    }

    private BeanDefinition getBeanDefinition(String id) {
        return beanDefinitions.get(id);
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }
}
