package com.husky.container.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class BeanDefinition {
    private String id;
    private String beanClassName;
    private Map<String, String> dependencies;
    private Map<String, String> refDependencies;

}
