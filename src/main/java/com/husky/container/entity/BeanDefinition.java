package com.husky.container.entity;

import com.husky.container.contract.ApplicationContext;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Builder
public class BeanDefinition {
    private String id;
    private String beanClassName;
    private Map<String, String> dependencies;

    public Map<String, Object> getDependencies(ApplicationContext<?> context) {
        Map<String, Object> dependencies = new HashMap<>();

        for (Map.Entry<String, String> entry : this.dependencies.entrySet()) {
            String propertyName = entry.getKey();
            String propertyRef = entry.getValue();

            Object dependency = context.getBean(propertyRef);
            dependencies.put(propertyName, dependency);
        }
        return dependencies;
    }
}
