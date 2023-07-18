package com.husky.container.reader;

import com.husky.container.entity.BeanDefinition;
import lombok.Cleanup;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class XMLBeanDefinitionReaderTest {
    private final XMLBeanDefinitionReader reader = new XMLBeanDefinitionReader();

    @BeforeEach
    public void init() {
        reader.setPaths(new String[]{"default_content_test.xml"});
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
        InputStream inputStream = reader.getResourceAsStream("default_content_test.xml");
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
        @Cleanup InputStream inputStream = reader.getResourceAsStream("default_content_test.xml");
        String expectedContent = reader.getXMLContent(inputStream);
        String actualContent = getTestXMLContent().replaceAll("\n", "\r\n");
        assertEquals(expectedContent, actualContent);
    }

    @Test
    @SneakyThrows
    @DisplayName("Test, get bean list.")
    public void testGetBeanList() {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(new InputSource(new StringReader(getTestXMLContent())));

        NodeList expectedNodeList = document.getElementsByTagName("bean");
        NodeList actualNodeList = reader.getBeanList("default_content_test.xml");

        assertEquals(expectedNodeList.getLength(), actualNodeList.getLength());
        assertEquals(expectedNodeList.item(0).getNodeName(), actualNodeList.item(0).getNodeName());
        assertEquals(expectedNodeList.item(1).getNodeName(), actualNodeList.item(1).getNodeName());
    }

    @Test
    @SneakyThrows
    @DisplayName("Test, fill dependencies in BeanDefinition.")
    public void testFillAllDependenciesInBeanDefinition() {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(new InputSource(new StringReader(getTestXMLContent())));

        Element beanElement = (Element) document.getElementsByTagName("bean").item(0);

        BeanDefinition beanDefinition = new BeanDefinition();
        beanDefinition.setDependencies(new HashMap<>());
        beanDefinition.setRefDependencies(new HashMap<>());

        reader.fillDependency(beanElement, beanDefinition);

        int expectedDependenciesCount = 1;
        int expectedRefDependenciesCount = 1;
        assertEquals(expectedDependenciesCount, beanDefinition.getDependencies().size());
        assertEquals(expectedRefDependenciesCount, beanDefinition.getRefDependencies().size());

        String expectedDependencyValue = "visa";
        String expectedDependencyId = "paymentType";
        assertEquals(expectedDependencyValue, beanDefinition.getDependencies().get("paymentType"));
        assertEquals(expectedDependencyId, beanDefinition.getDependencies().keySet().toArray()[0]);

        String expectedRefDependencyValue = "mailService";
        String expectedRefDependencyId = "mailService";
        assertEquals(expectedRefDependencyValue, beanDefinition.getRefDependencies().get("mailService"));
        assertEquals(expectedRefDependencyId, beanDefinition.getRefDependencies().keySet().toArray()[0]);
    }

    private String getTestXMLContent() {
        return """
                <?xml version="1.0" encoding="UTF-8" ?>
                <beans>
                    <import resource="classpath:default_import_content_test.xml" />
                 
                    <bean id="paymentService" class="com.husky.container.entity.PaymentService">
                        <property name="mailService" ref="mailService" />
                        <property name="paymentType" value="visa" />
                    </bean>
                 
                    <bean id="mailService" class="com.husky.container.entity.MailService">
                        <property name="protocol" value="POP3" />
                        <property name="timeout" value="2000" />
                    </bean>
                </beans>""";
    }
}
