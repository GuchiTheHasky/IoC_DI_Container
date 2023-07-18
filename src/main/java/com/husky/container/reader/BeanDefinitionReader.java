package com.husky.container.reader;

import com.husky.container.entity.BeanDefinition;

import java.util.Map;

/**
 * The BeanDefinitionReader interface represents a reader that reads bean definitions and creates a map of bean definitions.
 * It provides a method for reading bean definitions from a source and returning the map of bean definitions.
 */
public interface BeanDefinitionReader {
    /**
     * Reads bean definitions and creates a map of bean definitions.
     *
     * @return a map of bean definitions, where the key is the bean ID and the value is the BeanDefinition object
     */
    Map<String, BeanDefinition> readBeanDefinition();
}
