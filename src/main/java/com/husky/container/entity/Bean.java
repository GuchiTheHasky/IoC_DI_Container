package com.husky.container.entity;

import com.husky.container.contract.ApplicationContext;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Bean {
    private Object value;
    private String id;

    public void  yes() {
        System.out.println("yes " + this.id);
    }

    public Map<String, Object> getDependencies(BeanDefinition beanDefinition, ApplicationContext<?> context) {
        Map<String, Object> dependencies = new HashMap<>();

        for (Map.Entry<String, String> entry : beanDefinition.getDependencies().entrySet()) {
            String propertyName = entry.getKey();
            String propertyRef = entry.getValue();

            Object dependency = context.getBean(propertyRef);

            dependencies.put(propertyName, dependency);
        }

        return dependencies;
    }
}



