package com.husky.container.reader.sax;

import com.husky.container.entity.BeanDefinition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SAXBeanDefinitionReaderTest {
    private final SAXBeanDefinitionReader SAX_READER =
            new SAXBeanDefinitionReader("default_content_test.xml", "default_import_content_test.xml");

    @Test
    @DisplayName("Test, read BeanDefinition.")
    public void testReadBeanDefinition() {
        List<BeanDefinition> beanDefinitions = SAX_READER.readBeanDefinition();
        int expectedBeanCount = 3;
        int actualBeanCount = beanDefinitions.size();

        assertNotNull(beanDefinitions);
        assertEquals(expectedBeanCount, actualBeanCount);

        assertTrue(beanDefinitions.stream().anyMatch(beanDefinition -> beanDefinition.getId().equals("paymentService")));
        assertTrue(beanDefinitions.stream().anyMatch(beanDefinition -> beanDefinition.getId().equals("mailService")));
        assertTrue(beanDefinitions.stream().anyMatch(beanDefinition -> beanDefinition.getId().equals("userService")));
    }
}
