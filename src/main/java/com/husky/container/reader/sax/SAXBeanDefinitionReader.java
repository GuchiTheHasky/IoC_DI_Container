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
    private static final SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    private static final SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
    private final static String XSD_SCHEMA = "xsd/schema.xsd";
    private final SAXParser SAX_PARSER;
    private final SAXHandler SAX_HANDLER;
    private final String[] paths;

    public SAXBeanDefinitionReader(String... paths) {
        this.paths = paths;
        this.SAX_HANDLER = new SAXHandler();

        try {
            Schema schema = schemaFactory.newSchema(getClass().getResource("/" + XSD_SCHEMA));
            saxParserFactory.setSchema(schema);
            SAX_PARSER = saxParserFactory.newSAXParser();
        } catch (Exception e) {
            log.error("Failed to create SAXParser", e);
            throw new XMLReaderException("Error occurred while creating SAXParser.");
        }
    }

    @Override
    public List<BeanDefinition> readBeanDefinition() {
        List<BeanDefinition> beanDefinitions = new ArrayList<>();

        for (String path : paths) {
            try (InputStream content = getResourceAsStream(path)) {
                SAX_HANDLER.reset();
                SAX_PARSER.parse(content, SAX_HANDLER);
                beanDefinitions.addAll(SAX_HANDLER.getBeanDefinitions());
            } catch (Exception e) {
                log.error("Failed to read bean definition from path: {}", path, e);
                throw new XMLReaderException("Error occurred while reading bean definitions.");
            }
        }
        return beanDefinitions;
    }

    private InputStream getResourceAsStream(String path) {
        return getClass().getResourceAsStream(path);
    }
}
