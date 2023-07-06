package com.husky.container.reader;

import com.husky.container.contract.BeanDefinitionReader;
import com.husky.container.entity.BeanDefinition;
import com.husky.container.util.BeanInstantiationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class XMLBeanDefinitionReader implements BeanDefinitionReader {
    private String[] paths;

    public XMLBeanDefinitionReader(String... paths) {
        this.paths = paths;
    }

    public InputStream getResourceAsStream(String path) {
        return getClass().getResourceAsStream("/" + path);
    }

    @Override
    public Map<String, BeanDefinition> readBeanDefinition() {
        Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();

            for (String path : paths) {
                InputStream inputStream = getResourceAsStream(path);
                Document document = documentBuilder.parse(inputStream);
                Element root = document.getDocumentElement();

                NodeList beanNodes = root.getElementsByTagName("bean");

                for (int i = 0; i < beanNodes.getLength(); i++) {
                    Element beanElement = (Element) beanNodes.item(i);
                    String id = beanElement.getAttribute("id");
                    String className = beanElement.getAttribute("class");

                    BeanDefinition beanDefinition = new BeanDefinition();
                    beanDefinition.setId(id);
                    beanDefinition.setBeanClassName(className);

                    Map<String, String> dependencies = new HashMap<>();
                    NodeList propertyNodes = beanElement.getElementsByTagName("property");

                    for (int j = 0; j < propertyNodes.getLength(); j++) {
                        Element propertyElement = (Element) propertyNodes.item(j);
                        String propertyName = propertyElement.getAttribute("name");
                        String propertyValue = propertyElement.getAttribute("value");

                        dependencies.put(propertyName, propertyValue);
                    }

                    beanDefinition.setDependencies(dependencies);
                    beanDefinitionMap.put(id, beanDefinition);
                }
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new BeanInstantiationException("", e);
        }
        return beanDefinitionMap;
    }
}
