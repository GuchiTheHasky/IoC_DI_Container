package com.husky.container.reader.sax;

import com.husky.container.entity.BeanDefinition;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
public class SAXHandler extends DefaultHandler {
    private final List<BeanDefinition> beanDefinitions = new ArrayList<>();
    private BeanDefinition currentBeanDefinition;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if (qName.equalsIgnoreCase("bean")) {
            String id = attributes.getValue("id");
            String className = attributes.getValue("class");
            currentBeanDefinition = BeanDefinition.buildBeanDefinition(id, className);
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
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if (qName.equalsIgnoreCase("bean")) {
            beanDefinitions.add(currentBeanDefinition);
            currentBeanDefinition = null;
        }
    }
}