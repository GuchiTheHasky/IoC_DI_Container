package com.husky.container;

import com.husky.container.context.ClassPathApplicationContextITest;
import com.husky.container.reader.XMLBeanDefinitionReaderTest;
import com.husky.container.reader.XMLValidatorTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TestDigest {
    @Test
    @DisplayName("XMLValidatorTests")
    public void testXMLValidator() {
        XMLValidatorTest xmlValidatorTest = new XMLValidatorTest();
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
        contextITestTest.testInjectPropertyDependencies();
        contextITestTest.testInjectRefDependencies();
    }

    @Test
    @DisplayName("XMLBeanDefinitionReaderTest")
    public void testXMLBeanDefinitionReader() {
        XMLBeanDefinitionReaderTest xmlReaderTest = new XMLBeanDefinitionReaderTest();
        xmlReaderTest.init();
        xmlReaderTest.testBuildBeanDefinition();
        xmlReaderTest.testGetResourceAsStream();
        xmlReaderTest.testGetXMLContent();
        xmlReaderTest.testReadBeanDefinition();
        xmlReaderTest.testGetBeanList();
        xmlReaderTest.testFillAllDependenciesInBeanDefinition();
    }
}
