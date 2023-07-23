package com.husky.container.reader.dom;

import com.husky.container.entity.BeanDefinition;
import com.husky.container.exception.XMLReaderException;
import com.husky.container.reader.BeanDefinitionReader;
import com.husky.container.reader.xsd.XSDValidator;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Setter
@Slf4j
public class DOMBeanDefinitionReader implements BeanDefinitionReader {
    private final static String XSD_SCHEMA = "xsd/schema.xsd";
    private String[] paths;
    private DocumentBuilderFactory documentBuilderFactory;
    private DocumentBuilder documentBuilder;

    public DOMBeanDefinitionReader(String... paths) {
        this.paths = paths;
        documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
        } catch (Exception e) {
            throw new XMLReaderException("Failed to create DocumentBuilder", e);
        }
    }

    @Override
    public List<BeanDefinition> readBeanDefinition() {
        List<BeanDefinition> beanDefinitions = new ArrayList<>();
        for (String path : paths) {
            NodeList beanList = getBeanList(path);
            for (int i = 0; i < beanList.getLength(); i++) {
                Element beanElement = (Element) beanList.item(i);
                String id = beanElement.getAttribute("id");
                String className = beanElement.getAttribute("class");

                BeanDefinition beanDefinition = BeanDefinition.buildBeanDefinition(id, className);
                fillDependency(beanElement, beanDefinition);
                beanDefinitions.add(beanDefinition);
            }
        }
        return beanDefinitions;
    }

    void fillDependency(Element beanElement, BeanDefinition beanDefinition) {
        NodeList propertyNodes = beanElement.getElementsByTagName("property");
        for (int j = 0; j < propertyNodes.getLength(); j++) {
            Element propertyElement = (Element) propertyNodes.item(j);
            String propertyName = propertyElement.getAttribute("name");
            String propertyValue = propertyElement.getAttribute("value");
            String propertyRef = propertyElement.getAttribute("ref");

            if (!propertyRef.isEmpty()) {
                beanDefinition.getRefDependencies().put(propertyName, propertyRef);
            } else {
                beanDefinition.getDependencies().put(propertyName, propertyValue);
            }
        }
    }

    InputStream getResourceAsStream(String path) {
        return getClass().getResourceAsStream("/context/" + path);
    }

    NodeList getBeanList(String path) {
        try (InputStream inputStream = getResourceAsStream(path)) {
            ByteArrayInputStream content = new ByteArrayInputStream(inputStream.readAllBytes());
            XSDValidator.validate(content, XSD_SCHEMA);
            content.reset();
            Document document = documentBuilder.parse(content);
            Element root = document.getDocumentElement();
            return root.getElementsByTagName("bean");
        } catch (IOException | SAXException e) {
            log.error("Failed to parse XML content.", e);
            throw new XMLReaderException("Application initialization failed.", e);
        }
    }
}
