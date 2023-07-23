package com.husky.container.reader.xsd;

import com.husky.container.exception.BeanInstantiationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class XSDValidatorITest {
    private final static String XSD_SCHEMA = "xsd/schema.xsd";
    @Test
    @DisplayName("Test validate XML schema throw exception")
    public void testValidateXMLSchemaThrowException() {
        assertThrows(BeanInstantiationException.class, () -> XSDValidator.validate(invalidXML(), XSD_SCHEMA));
    }

    @Test
    @DisplayName("Test validate XML schema")
    public void testValidateXMLSchema() {
        assertDoesNotThrow(() -> XSDValidator.validate(validXML(), XSD_SCHEMA));
    }

    private InputStream invalidXML() {
        return new ByteArrayInputStream("Incorrect XML".getBytes());
    }

    private InputStream validXML() {
        return new ByteArrayInputStream("""
                <?xml version="1.0" encoding="UTF-8" ?>
                <beans>
                    <import resource="classpath:default_import_content_test.xml" />
                            
                    <bean id="paymentService" class="com.husky.container.entity.PaymentService">
                        <property name="mailService" ref="mailService" />
                        <property name="paymentType" value="visa" />
                    </bean>
                            
                    <bean id="mailService" class="com.husky.container.entity.MailService">
                        <property name="protocol" value="POP3" />
                        <property name="timeout" value="2000" />
                    </bean>
                </beans>""".getBytes());
    }
}
