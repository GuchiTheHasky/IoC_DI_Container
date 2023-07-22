package com.husky.container.reader;

import com.husky.container.entity.BeanDefinition;

import java.util.List;

/**
 * The BeanDefinitionReader interface represents a reader that
 * reads bean definitions and creates a List of bean definitions.
 * It provides a method for reading bean definitions from a
 * source and returning the list of bean definitions.
 */
public interface BeanDefinitionReader {
    /**
     * Reads bean definitions and creates a List of bean definitions.
     *
     * @return a List of bean definitions, where the key is the bean ID and the value is the BeanDefinition object
     */
    List<BeanDefinition> readBeanDefinition();
}
