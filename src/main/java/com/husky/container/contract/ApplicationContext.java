package com.husky.container.contract;

import java.util.List;

/**
 * The ApplicationContext interface represents a container that manages beans and their dependencies.
 * It provides methods for retrieving beans based on their class, ID, or obtaining a list of all available beans.
 *
 */
public interface ApplicationContext {
    /**
     * Retrieves a bean of the specified class from the application context.
     *
     */
    <T> T getBean(Class<T> clazz);

    /**
     * Retrieves a bean of the specified class and ID from the application context.
     *

     */
    <T> T getBean(String id, Class<T> clazz);

    /**
     * Retrieves a bean with the specified ID from the application context.
     *
     * @param id the ID of the bean to retrieve
     * @return the bean with the specified ID, or null if not found
     */
    Object getBean(String id);

    /**
     * Retrieves a list of IDs of all available beans in the application context.
     *
     * @return a list of bean IDs
     */
    List<String> getBeans();
}
