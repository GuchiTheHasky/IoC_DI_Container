package com.husky.container.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BeanDefinition {
    private String id;
    private String beanClassName;
    private Map<String, String> dependencies;
    private Map<String, String> refDependencies;

    public static BeanDefinition buildBeanDefinition(String id, String className) {
        return BeanDefinition.builder()
                .id(id)
                .beanClassName(className)
                .dependencies(new HashMap<>())
                .refDependencies(new HashMap<>())
                .build();
    }
}
