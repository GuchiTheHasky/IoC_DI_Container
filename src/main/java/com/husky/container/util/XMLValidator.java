package com.husky.container.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringReader;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

public class XMLValidator {

    public static void validate(String xmlString) {
        String xsdFileName = "schema.xsd";
        ClassLoader classLoader = XMLValidator.class.getClassLoader();
        try (InputStream xsdInputStream = classLoader.getResourceAsStream(xsdFileName)) {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new StreamSource(xsdInputStream));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new StringReader(xmlString)));
        } catch (Exception e) {
            throw new BeanInstantiationException("XML is invalid: " + e.getMessage());
        }
    }
}



