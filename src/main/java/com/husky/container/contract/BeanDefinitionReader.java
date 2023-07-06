package com.husky.container.contract;

import com.husky.container.entity.BeanDefinition;

import java.util.List;
import java.util.Map;

public interface BeanDefinitionReader {
    Map<String, BeanDefinition> readBeanDefinition();
}
