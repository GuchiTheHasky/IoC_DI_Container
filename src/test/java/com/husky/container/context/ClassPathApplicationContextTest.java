package com.husky.container.context;

import com.husky.container.exception.BeanInstantiationException;
import com.husky.container.reader.sax.SAXBeanDefinitionReader;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ClassPathApplicationContextTest {
    private final ClassPathApplicationContext DOM_CONTEXT =
            new ClassPathApplicationContext("/context/default_content_test.xml",
                    "/context/default_import_content_test.xml");
    private final ClassPathApplicationContext SAX_CONTEXT =
            new ClassPathApplicationContext
                    (new SAXBeanDefinitionReader("/context/default_content_test.xml",
                            "/context/default_import_content_test.xml"));

    @Test
    public void testValidateId() {
        String emptyString = "";
        assertThrows(BeanInstantiationException.class, () -> DOM_CONTEXT.validateId(emptyString));
        assertThrows(BeanInstantiationException.class, () -> SAX_CONTEXT.validateId(emptyString));

        assertThrows(BeanInstantiationException.class, () -> DOM_CONTEXT.validateId(null));
        assertThrows(BeanInstantiationException.class, () -> SAX_CONTEXT.validateId(null));
    }

    @Test
    public void testValidateClass() {
        assertThrows(BeanInstantiationException.class, () -> DOM_CONTEXT.validateClass(null));
        assertThrows(BeanInstantiationException.class, () -> SAX_CONTEXT.validateClass(null));
    }
}
