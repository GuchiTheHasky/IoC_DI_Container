package com.husky.container;

import com.husky.container.contract.ApplicationContext;
import com.husky.container.contract.BeanDefinitionReader;
import com.husky.container.entity.Bean;
import com.husky.container.entity.BeanDefinition;
import com.husky.container.reader.XMLBeanDefinitionReader;
import com.husky.container.util.BeanInstantiationException;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
class ClassPathApplicationContext<T> implements ApplicationContext<T> {
    private final String[] PATHS;
    private BeanDefinitionReader beanReader;
    private Map<String, BeanDefinition> beanDefinitions;
    private List<Bean> beans;

    public ClassPathApplicationContext(String... PATHS) {
        this.PATHS = PATHS;
    }

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
                return bean;
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
    public void setBeanDefinitionReader(BeanDefinitionReader reader) {
        if (reader instanceof XMLBeanDefinitionReader) {
            reader = new XMLBeanDefinitionReader();
            ((XMLBeanDefinitionReader) reader).setPaths(PATHS);
            this.beanReader = reader;
            loadBeanDefinitions();
            createBeansFromBeanDefinition();
            injectDependencies();
        } else {
            log.error("Invalid reader.");
            throw new BeanInstantiationException("Invalid reader, try \"XMLBeanDefinitionReader\".");
        }
    }

    private void createBeansFromBeanDefinition() {
        beans = new ArrayList<>();
        for (BeanDefinition beanDefinition : beanDefinitions.values()) {
            try {
                Class<?> beanClass = Class.forName(beanDefinition.getBeanClassName());
                Object beanInstance = beanClass.getDeclaredConstructor().newInstance();

                if (beanInstance instanceof Bean) {
                    Bean bean = buildBean(beanDefinition);
                    beans.add(bean);
                }
            } catch (Exception e) {
                log.error("Failed to retrieve beans.", e);
                throw new BeanInstantiationException("Application initialization failed.", e);
            }
        }
    }

    private Bean buildBean(BeanDefinition beanDefinition) {
        return Bean.builder()
                .value(beanDefinition.getBeanClassName())
                .id(beanDefinition.getId())
                .build();
    }

    private void injectDependencies() {
        for (Bean bean : beans) {
            BeanDefinition beanDefinition = getBeanDefinition(bean.getId());
            Map<String, Object> dependencies = beanDefinition.getDependencies(this);

            for (Map.Entry<String, Object> entry : dependencies.entrySet()) {
                String propertyName = entry.getKey();
                Object dependency = entry.getValue();

                injectDependency(bean.getValue(), propertyName, dependency);
            }
        }
    }

    private void loadBeanDefinitions() {
        beanDefinitions = beanReader.readBeanDefinition();
    }



    private void injectDependency(Object target, String propertyName, Object dependency) {
        try {
            String setterMethodName = "set" + propertyName;
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
            log.error("Failed to inject dependency.");
            throw new BeanInstantiationException("Application initialization failed.", e);
        }
    }

    private BeanDefinition getBeanDefinition(String id) {
        return beanDefinitions.get(id);
    }
}
