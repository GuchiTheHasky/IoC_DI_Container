package com.husky.container.context;

import java.util.List;

/**
 * The ApplicationContext interface represents a container for managing beans and their dependencies.
 * It provides methods for retrieving beans by their class or ID, as well as accessing all registered bean names.
 */
public interface ApplicationContext {
    /**
     * Retrieves a bean of the specified class from the container.
     *
     * @param clazz The class type of the bean to retrieve.
     * @param <T>   The generic type of the bean.
     * @return The bean instance of the specified class if it exists in the container, or null otherwise.
     */
    <T> T getBean(Class<T> clazz);

    /**
     * Retrieves a bean of the specified class and ID from the container.
     *
     * @param id    The ID of the bean to retrieve.
     * @param clazz The class type of the bean to retrieve.
     * @param <T>   The generic type of the bean.
     * @return The bean instance of the specified class and ID if it exists in the container, or null otherwise.
     */
    <T> T getBean(String id, Class<T> clazz);

    /**
     * Retrieves a bean by its ID from the container.
     *
     * @param id The ID of the bean to retrieve.
     * @return The bean instance with the specified ID if it exists in the container, or null otherwise.
     */
    Object getBean(String id);

    /**
     * Returns a list of all registered bean names in the container.
     *
     * @return A list of bean names.
     */
    List<String> getBeansNames();
}
