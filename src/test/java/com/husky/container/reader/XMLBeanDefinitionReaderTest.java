package com.husky.container.reader;

import com.husky.container.entity.BeanDefinition;
import lombok.Cleanup;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class XMLBeanDefinitionReaderTest {
    private final XMLBeanDefinitionReader reader = new XMLBeanDefinitionReader();

    @BeforeEach
    public void init() {
        reader.setPaths(new String[]{"test_xml.xml"});
    }

    @Test
    @DisplayName("Test, read BeanDefinition.")
    public void testReadBeanDefinition() {
        Map<String, BeanDefinition> beanDefinitions = reader.readBeanDefinition();
        int expectedBeanCount = 2;
        int actualBeanCount = beanDefinitions.size();
        assertNotNull(beanDefinitions);
        assertEquals(expectedBeanCount, actualBeanCount);
    }

    @Test
    @DisplayName("Test, get resource as stream.")
    public void testGetResourceAsStream() {
        InputStream inputStream = reader.getResourceAsStream("test_xml.xml");
        assertNotNull(inputStream);
    }

    @Test
    @DisplayName("Test, build BeanDefinition.")
    public void testBuildBeanDefinition() {
        String id = "beanId";
        String className = "com.example.BeanClass";
        BeanDefinition beanDefinition = reader.buildBeanDefinition(id, className);
        assertNotNull(beanDefinition);
        assertEquals(id, beanDefinition.getId());
        assertEquals(className, beanDefinition.getBeanClassName());
        assertNotNull(beanDefinition.getDependencies());
    }

    @SneakyThrows
    @Test
    @DisplayName("Test, get content xml file.")
    public void testGetXMLContent() {
        @Cleanup InputStream inputStream = reader.getResourceAsStream("test_xml.xml");
        String expectedContent = reader.getXMLContent(inputStream);
        String actualContent = getTestXMLContent().replaceAll("\n", "\r\n");
        assertEquals(expectedContent, actualContent);
    }

    private String getTestXMLContent() {
        return """
                <?xml version="1.0" encoding="UTF-8" ?>
                <beans>
                    <bean id="userService" class="com.husky.container.entity.Bean">
                        <property name="mailService" ref="mailService" />
                    </bean>

                    <bean id="paymentService" class="com.husky.container.entity.Bean">
                        <property name="mailService" value="mailService" />
                    </bean>
                </beans>""";
    }
}
