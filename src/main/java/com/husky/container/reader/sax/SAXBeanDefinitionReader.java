package com.husky.container.reader.sax;

import com.husky.container.entity.BeanDefinition;
import com.husky.container.exception.XMLReaderException;
import com.husky.container.reader.BeanDefinitionReader;
import com.husky.container.reader.xsd.XSDValidator;
import lombok.extern.slf4j.Slf4j;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SAXBeanDefinitionReader implements BeanDefinitionReader {
    private final static String XSD_SCHEMA = "schema.xsd";
    private final String[] paths;
    private final SAXParser saxParser;

    public SAXBeanDefinitionReader(String... paths) {
        this.paths = paths;
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        try {
            saxParser = saxParserFactory.newSAXParser();
        } catch (Exception e) {
            log.error("Failed to create SAXParser", e);
            throw new XMLReaderException("Error occurred while creating SAXParser.");
        }
    }

    @Override
    public List<BeanDefinition> readBeanDefinition() {
        List<BeanDefinition> beanDefinitions = new ArrayList<>();
        for (String path : paths) {
            try (InputStream streamContent = getResourceAsStream(path)) {
                ByteArrayInputStream content = new ByteArrayInputStream(streamContent.readAllBytes());
                XSDValidator.validate(content, XSD_SCHEMA);
                content.reset();
                SAXHandler handler = new SAXHandler();
                saxParser.parse(content, handler);
                beanDefinitions.addAll(handler.getBeanDefinitions());
            } catch (Exception e) {
                log.error("Failed to read bean definition from path: {}", path, e);
                throw new XMLReaderException("Error occurred while reading bean definitions.");
            }
        }
        return beanDefinitions;
    }

    private InputStream getResourceAsStream(String path) {
        return getClass().getResourceAsStream("/" + path);
    }
}
