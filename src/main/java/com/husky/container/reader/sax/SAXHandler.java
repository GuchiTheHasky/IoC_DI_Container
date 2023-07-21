package com.husky.container.reader.sax;

import com.husky.container.entity.BeanDefinition;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
@Slf4j
public class SAXHandler extends DefaultHandler {
    private List<BeanDefinition> beanDefinitions = new ArrayList<>();
    private BeanDefinition currentBeanDefinition;
    private StringBuilder textBuffer;

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        textBuffer.append(ch, start, length);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equalsIgnoreCase("bean")) {
            String id = attributes.getValue("id");
            String className = attributes.getValue("class");
            currentBeanDefinition = buildBeanDefinition(id, className);
        } else if (qName.equalsIgnoreCase("property")) {
            String propertyName = attributes.getValue("name");
            String propertyValue = attributes.getValue("value");
            String propertyRef = attributes.getValue("ref");
            if (propertyRef == null) {
                currentBeanDefinition.getDependencies().put(propertyName, propertyValue);
            } else {
                currentBeanDefinition.getRefDependencies().put(propertyName, propertyRef);
            }
        }
        textBuffer = new StringBuilder();
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("bean")) {
            beanDefinitions.add(currentBeanDefinition);
            currentBeanDefinition = null;
        }
    }

    private BeanDefinition buildBeanDefinition(String id, String className) {
        return BeanDefinition.builder()
                .id(id)
                .beanClassName(className)
                .dependencies(new HashMap<>())
                .refDependencies(new HashMap<>())
                .build();
    }
}


//{
//        beanDefinitions.add(currentBeanDefinition);
//        } else if (qName.equalsIgnoreCase("id")) {
//        currentBeanDefinition.setId(textBuffer.toString());
//        } else if (qName.equalsIgnoreCase("class")) {
//        currentBeanDefinition.setBeanClassName(textBuffer.toString());
//        } else if (qName.equalsIgnoreCase("property")) {
//        String name = textBuffer.toString();
//        String ref = textBuffer.toString();
//        currentBeanDefinition.getRefDependencies().put(name, ref);
//        } else if (qName.equalsIgnoreCase("value")) {
//        String name = textBuffer.toString();
//        String value = textBuffer.toString();
//        currentBeanDefinition.getDependencies().put(name, value);
//        }