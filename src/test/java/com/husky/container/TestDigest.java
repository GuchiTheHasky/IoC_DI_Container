package com.husky.container;

import com.husky.container.context.ClassPathApplicationContextITest;
import com.husky.container.reader.dom.DOMBeanDefinitionReaderTest;
import com.husky.container.reader.dom.XSDValidatorTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TestDigest {
    @Test
    @DisplayName("XMLValidatorTests")
    public void testXMLValidator() {
        XSDValidatorTest xmlValidatorTest = new XSDValidatorTest();
        xmlValidatorTest.testValidateXMLSchema();
        xmlValidatorTest.testValidateXMLSchemaThrowException();
    }

    @Test
    @DisplayName("ClassPathApplicationContextITests")
    public void testClassPathApplicationContext() {
        ClassPathApplicationContextITest contextITestTest = new ClassPathApplicationContextITest();
        contextITestTest.testCreateBeansWithValueAndRefDependenciesUsedIdAndClass();
        contextITestTest.testCreateBeansWithValueAndRefDependenciesUsedClass();
        contextITestTest.testCreateBeansWithValueAndRefDependenciesUsedId();
        contextITestTest.testGetListOfBeans();
        contextITestTest.testCreateBeanFromBeanDefinition();
        contextITestTest.testCreateBeanFromBeanDefinitionThrowException();
    }

    @Test
    @DisplayName("XMLBeanDefinitionReaderTest")
    public void testXMLBeanDefinitionReader() {
        DOMBeanDefinitionReaderTest xmlReaderTest = new DOMBeanDefinitionReaderTest();
        xmlReaderTest.init();
        xmlReaderTest.testBuildBeanDefinition();
        xmlReaderTest.testGetResourceAsStream();
        xmlReaderTest.testReadBeanDefinition();
        xmlReaderTest.testGetBeanList();
        xmlReaderTest.testFillAllDependenciesInBeanDefinition();
    }
}
