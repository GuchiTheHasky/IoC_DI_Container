package com.husky.container.reader.xsd;

import com.husky.container.exception.BeanInstantiationException;
import lombok.extern.slf4j.Slf4j;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.InputStream;

@Slf4j
public class XSDValidator {
    private static final SchemaFactory SCHEMA_FACTORY = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

    public static void validate(InputStream content, String schemaPath) {
        ClassLoader classLoader = XSDValidator.class.getClassLoader();
        try (InputStream xsdInputStream = classLoader.getResourceAsStream(schemaPath)) {
            Schema schema = SCHEMA_FACTORY.newSchema(new StreamSource(xsdInputStream));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(content));
        } catch (Exception e) {
            log.error("XML is invalid.", e);
            throw new BeanInstantiationException("Application initialization failed." + e);
        }
    }
}



