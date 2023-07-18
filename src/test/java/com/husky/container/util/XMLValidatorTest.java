package com.husky.container.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class XMLValidatorTest {
    @Test
    @DisplayName("Test validate XML schema throw exception")
    public void testValidateXMLSchemaThrowException() {
        assertThrows(BeanInstantiationException.class, () -> XMLValidator.validate(noXML()));
    }

    @Test
    @DisplayName("Test validate XML schema")
    public void testValidateXMLSchema() {
        assertDoesNotThrow(() -> XMLValidator.validate(realXML()));
    }

    private String noXML() {
        return "Incorrect XML";
    }

    private String realXML() {
        return """
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
                </beans>""";
    }
}
