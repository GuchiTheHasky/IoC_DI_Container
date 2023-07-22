package com.husky.container.util;

import com.husky.container.entity.Bean;
import com.husky.container.entity.BeanDefinition;
import com.husky.container.exception.BeanInstantiationException;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class BeanCreator {

    public static Map<String, Bean> createBeans(List<BeanDefinition> beanDefinitions) {
        Map<String, Bean> beans = initializeBeans(beanDefinitions);
        injectValueDependencies(beans, beanDefinitions);
        injectRefDependencies(beans, beanDefinitions);
        return beans;
    }

    static Map<String, Bean> initializeBeans(List<BeanDefinition> beanDefinitions) {
        Map<String, Bean> beans = new HashMap<>();
        for (BeanDefinition definition : beanDefinitions) {
            String beanId = definition.getId();
            Object object = null;
            try {
                object = Class.forName(definition.getBeanClassName()).getConstructor().newInstance();
            } catch (Exception e) {
                log.error("Failed to create bean instance for bean: {}", beanId, e);
                throw new BeanInstantiationException("Bean creating failed: " + beanId, e);
            }
            Bean bean = new Bean(beanId, object);
            beans.put(beanId, bean);
        }
        return beans;
    }

    static void injectValueDependencies(Map<String, Bean> beans, List<BeanDefinition> beanDefinitions) {
        for (BeanDefinition beanDefinition : beanDefinitions) {
            Bean bean = beans.get(beanDefinition.getId());
            if (bean != null) {
                Object beanInstance = bean.getValue();
                Map<String, String> dependencies = beanDefinition.getDependencies();
                for (Map.Entry<String, String> dependencyEntry : dependencies.entrySet()) {
                    String propertyName = dependencyEntry.getKey();
                    String propertyValue = dependencyEntry.getValue();
                    setPropertyValue(beanInstance, propertyName, propertyValue);
                }
            }
        }
    }

    static void injectRefDependencies(Map<String, Bean> beans, List<BeanDefinition> beanDefinitions) {
        for (BeanDefinition beanDefinition : beanDefinitions) {
            Bean bean = beans.get(beanDefinition.getId());
            if (bean != null) {
                Object beanInstance = bean.getValue();
                Map<String, String> refDependencies = beanDefinition.getRefDependencies();
                for (Map.Entry<String, String> refDependencyEntry : refDependencies.entrySet()) {
                    String propertyName = refDependencyEntry.getKey();
                    String refBeanId = refDependencyEntry.getValue();
                    Bean refBean = beans.get(refBeanId);
                    if (refBean != null) {
                        Object refBeanInstance = refBean.getValue();
                        setPropertyValue(beanInstance, propertyName, refBeanInstance);
                    } else {
                        log.error("Failed to find reference bean with id: {}", refBeanId);
                        throw new BeanInstantiationException("Can't find reference: " + refBeanId);
                    }
                }
            }
        }
    }

    static void setPropertyValue(Object beanInstance, String propertyName, Object propertyValue) {
        try {
            Class<?> beanClass = beanInstance.getClass();
            Field field = beanClass.getDeclaredField(propertyName);
            field.setAccessible(true);

            Class<?> fieldType = field.getType();
            Object convertedValue = obtainValue(propertyValue, fieldType);

            field.set(beanInstance, convertedValue);
        } catch (Exception e) {
            log.error("Failed to set property value for property: {} on bean instance: {}",
                    propertyName, beanInstance, e);
            throw new BeanInstantiationException("Failed to set property value.", e);
        }
    }

    static Object obtainValue(Object propertyValue, Class<?> fieldType) {
        Object value = null;
        if (fieldType.isAssignableFrom(propertyValue.getClass())) {
            value = propertyValue;
        } else if (fieldType == int.class) {
            value = Integer.parseInt(propertyValue.toString());
        } else if (fieldType == long.class) {
            value = Long.parseLong(propertyValue.toString());
        } else if (fieldType == double.class) {
            value = Double.parseDouble(propertyValue.toString());
        } else if (fieldType == float.class) {
            value = Float.parseFloat(propertyValue.toString());
        } else if (fieldType == short.class) {
            value = Short.parseShort(propertyValue.toString());
        } else if (fieldType == byte.class) {
            value = Byte.parseByte(propertyValue.toString());
        } else if (fieldType == boolean.class) {
            value = Boolean.parseBoolean(propertyValue.toString());
        } else if (fieldType == char.class) {
            if (propertyValue.toString().length() > 0) {
                value = propertyValue.toString().charAt(0);
            }
        } else {
            log.error("Failed to set property value.");
            throw new BeanInstantiationException("Unsupported data type.");
        }
        return value;
    }
}
