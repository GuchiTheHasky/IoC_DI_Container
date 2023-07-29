package com.husky.container.reader.sax;

import com.husky.container.entity.BeanDefinition;
import com.husky.container.exception.XMLReaderException;
import com.husky.container.reader.BeanDefinitionReader;
import lombok.extern.slf4j.Slf4j;

import javax.xml.XMLConstants;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SAXBeanDefinitionReader implements BeanDefinitionReader {
    private final static String XSD_SCHEMA = "xsd/schema.xsd";
    private final String[] paths;
    private final SAXParser saxParser;

    public SAXBeanDefinitionReader(String... paths) {
        this.paths = paths;

        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        try {
            Schema schema = schemaFactory.newSchema(getClass().getResource("/" + XSD_SCHEMA));
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            saxParserFactory.setSchema(schema);
            saxParser = saxParserFactory.newSAXParser();
        } catch (Exception e) {
            log.error("Failed to create SAXParser", e);
            throw new XMLReaderException("Error occurred while creating SAXParser.");
        }
    }

    @Override
    public List<BeanDefinition> readBeanDefinition() {
        List<BeanDefinition> beanDefinitions = new ArrayList<>();
        SAXHandler handler = new SAXHandler();
        for (String path : paths) {
            try (InputStream content = getResourceAsStream(path)) {
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
        return getClass().getResourceAsStream("/context/" + path);
    }
}
