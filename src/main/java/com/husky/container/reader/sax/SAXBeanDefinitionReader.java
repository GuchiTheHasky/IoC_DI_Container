package com.husky.container.reader.sax;

import com.husky.container.entity.BeanDefinition;
import com.husky.container.reader.BeanDefinitionReader;
import lombok.extern.slf4j.Slf4j;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SAXBeanDefinitionReader implements BeanDefinitionReader {
    private final String[] paths;
    private final SAXParser saxParser;

    public SAXBeanDefinitionReader(String... paths) {
        this.paths = paths;
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        try {
            saxParser = saxParserFactory.newSAXParser();
        } catch (ParserConfigurationException | SAXException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public List<BeanDefinition> readBeanDefinition() {
        List<BeanDefinition> beanDefinitions = new ArrayList<>();
        for (String path : paths) {
            try (InputStream inputStream = getResourceAsStream(path)) {
                SAXHandler handler = new SAXHandler();
                saxParser.parse(inputStream, handler);
                beanDefinitions.addAll(handler.getBeanDefinitions());
            } catch (SAXException | IOException e) {
                throw new RuntimeException(e);
            }
        }
        return beanDefinitions;
    }

    InputStream getResourceAsStream(String path) {
        return getClass().getResourceAsStream("/" + path);
    }
}
