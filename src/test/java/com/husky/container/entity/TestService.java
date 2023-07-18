package com.husky.container.entity;

import java.util.Map;

public class TestService extends BeanDefinition {
    TestService(String id, String beanClassName, Map<String, String> dependencies, Map<String, String> refDependencies) {
        super(id, beanClassName, dependencies, refDependencies);
    }
}
