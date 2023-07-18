package com.husky.container.reader;

import com.husky.container.entity.BeanDefinition;
import com.husky.container.exception.ReaderInstantiationException;
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
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Setter
@Slf4j
public class XMLBeanDefinitionReader implements BeanDefinitionReader {
    private String[] paths;
    private DocumentBuilderFactory documentBuilderFactory;
    private DocumentBuilder documentBuilder;

    public XMLBeanDefinitionReader(String... paths) {
        this.paths = paths;
        documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
        } catch (Exception e) {
            throw new ReaderInstantiationException("Failed to create DocumentBuilder", e);
        }
    }

    @Override
    public Map<String, BeanDefinition> readBeanDefinition() {
        Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();
        for (String path : paths) {
            NodeList beanList = getBeanList(path);
            for (int i = 0; i < beanList.getLength(); i++) {
                Element beanElement = (Element) beanList.item(i);
                String id = beanElement.getAttribute("id");
                String className = beanElement.getAttribute("class");

                BeanDefinition beanDefinition = buildBeanDefinition(id, className);
                fillDependencyCache(beanElement, beanDefinition);
                beanDefinitionMap.put(id, beanDefinition);
            }
        }
        return beanDefinitionMap;
    }

    void fillDependencyCache(Element beanElement, BeanDefinition beanDefinition) {
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
        return getClass().getResourceAsStream("/" + path);
    }

    BeanDefinition buildBeanDefinition(String id, String className) {
        return BeanDefinition.builder()
                .id(id)
                .beanClassName(className)
                .dependencies(new HashMap<>())
                .refDependencies(new HashMap<>())
                .build();
    }

    NodeList getBeanList(String path) {
        try (InputStream inputStream = getResourceAsStream(path)) {
            String xmlContent = getXMLContent(inputStream);
            XMLValidator.validate(xmlContent);
            Document document = documentBuilder
                    .parse(new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8)));
            Element root = document.getDocumentElement();
            return root.getElementsByTagName("bean");
        } catch (IOException | SAXException e) {
            log.error("Failed to parse XML content.", e);
            throw new ReaderInstantiationException("Application initialization failed.", e);
        }
    }

    String getXMLContent(InputStream inputStream) throws IOException {
        byte[] buffer = inputStream.readAllBytes();
        return new String(buffer, StandardCharsets.UTF_8);
    }
}
