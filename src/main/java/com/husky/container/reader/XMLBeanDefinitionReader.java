package com.husky.container.reader;

//import com.husky.container.contract.BeanDefinitionReader;
//import com.husky.container.entity.BeanDefinition;
//import com.husky.container.util.BeanInstantiationException;
//import com.husky.container.util.XMLValidator;
//import lombok.Setter;
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//import org.w3c.dom.NodeList;
//import org.xml.sax.SAXException;
//
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.parsers.ParserConfigurationException;
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.nio.charset.StandardCharsets;
//import java.util.HashMap;
//import java.util.Map;
//
//@Setter
//public class XMLBeanDefinitionReader implements BeanDefinitionReader {
//    private String[] paths;
//    private DocumentBuilderFactory documentBuilderFactory;
//    private DocumentBuilder documentBuilder;
//
//    public XMLBeanDefinitionReader() {
//        documentBuilderFactory = DocumentBuilderFactory.newInstance();
//        try {
//            documentBuilder = documentBuilderFactory.newDocumentBuilder();
//        } catch (ParserConfigurationException e) {
//            throw new BeanInstantiationException("Failed to create DocumentBuilder", e);
//        }
//    }
//
//    public InputStream getResourceAsStream(String path) {
//        return getClass().getResourceAsStream("/" + path);
//    }
//
//    @Override
//    public Map<String, BeanDefinition> readBeanDefinition() {
//        Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();
//        for (String path : paths) {
//            try (InputStream inputStream = getResourceAsStream(path)) {
//                String content = content(inputStream);
////
////                XMLValidator xmlValidator = new XMLValidator();
////                xmlValidator.validate(content);
//
//                Document document = documentBuilder.parse(new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)));
//                Element root = document.getDocumentElement();
//                NodeList beanList = root.getElementsByTagName("bean");
//                for (int i = 0; i < beanList.getLength(); i++) {
//                    Element beanElement = (Element) beanList.item(i);
//                    String id = beanElement.getAttribute("id");
//                    String className = beanElement.getAttribute("class");
//
//                    BeanDefinition beanDefinition = buildBeanDefinition(id, className);
//
//                    NodeList propertyNodes = beanElement.getElementsByTagName("property");
//                    for (int j = 0; j < propertyNodes.getLength(); j++) {
//                        Element propertyElement = (Element) propertyNodes.item(j);
//                        String propertyName = propertyElement.getAttribute("name");
//                        String propertyValue = propertyElement.getAttribute("value");
//
//                        beanDefinition.getDependencies().put(propertyName, propertyValue);
//                    }
//                    beanDefinitionMap.put(id, beanDefinition);
//                }
//            } catch (IOException | SAXException e) {
//                throw new BeanInstantiationException("Error reading bean definition: " + e.getMessage(), e);
//            }
//        }
//        return beanDefinitionMap;
//    }
//
//    private BeanDefinition buildBeanDefinition(String id, String className) {
//        return BeanDefinition.builder()
//                .id(id)
//                .beanClassName(className)
//                .dependencies(new HashMap<>())
//                .build();
//    }
//
//    private String content(InputStream inputStream) throws IOException {
//        byte[] buffer = inputStream.readAllBytes();
//        return new String(buffer, StandardCharsets.UTF_8);
//    }
//}


import com.husky.container.contract.BeanDefinitionReader;
import com.husky.container.entity.BeanDefinition;
import com.husky.container.util.BeanInstantiationException;
import com.husky.container.util.XMLValidator;
import lombok.Setter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Setter
public class XMLBeanDefinitionReader implements BeanDefinitionReader {
    private String[] paths;
    private DocumentBuilderFactory documentBuilderFactory;
    private DocumentBuilder documentBuilder;

    public XMLBeanDefinitionReader() {
        documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new BeanInstantiationException("Failed to create DocumentBuilder", e);
        }
    }

    public InputStream getResourceAsStream(String path) {
        return getClass().getResourceAsStream("/" + path);
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

                NodeList propertyNodes = beanElement.getElementsByTagName("property");
                for (int j = 0; j < propertyNodes.getLength(); j++) {
                    Element propertyElement = (Element) propertyNodes.item(j);
                    String propertyName = propertyElement.getAttribute("name");
                    String propertyValue = propertyElement.getAttribute("value");

                    beanDefinition.getDependencies().put(propertyName, propertyValue);
                }
                beanDefinitionMap.put(id, beanDefinition);
            }
        }
        return beanDefinitionMap;
    }

    private BeanDefinition buildBeanDefinition(String id, String className) {
        return BeanDefinition.builder()
                .id(id)
                .beanClassName(className)
                .dependencies(new HashMap<>())
                .build();
    }

    private NodeList getBeanList(String path) {
        try (InputStream inputStream = getResourceAsStream(path)) {
            String con = content(inputStream);
            XMLValidator.validate(con);
            Document document = documentBuilder.parse(new ByteArrayInputStream(con.getBytes(StandardCharsets.UTF_8)));
            Element root = document.getDocumentElement();
            return root.getElementsByTagName("bean");
        } catch (IOException | SAXException e) {
            throw new BeanInstantiationException("", e);
        }
    }

    private String content(InputStream inputStream) throws IOException {
        byte[] buffer = inputStream.readAllBytes();
        return new String(buffer, StandardCharsets.UTF_8);
    }
}
