package com.husky.container.util;

import com.husky.container.entity.*;
import lombok.Data;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class BeanCreatorTest {

    @Test
    @DisplayName("Test, setting property values;")
    public void testSetPropertyValues() {
        ValuesContainer valuesContainer = new ValuesContainer();

        BeanCreator.setPropertyValue(valuesContainer, "byteValue", "1");
        BeanCreator.setPropertyValue(valuesContainer, "shortValue", "12");
        BeanCreator.setPropertyValue(valuesContainer, "intValue", "123");
        BeanCreator.setPropertyValue(valuesContainer, "longValue", "1234");
        BeanCreator.setPropertyValue(valuesContainer, "doubleValue", "12345.0");
        BeanCreator.setPropertyValue(valuesContainer, "floatValue", "123456.0");
        BeanCreator.setPropertyValue(valuesContainer, "charValue", "a");
        BeanCreator.setPropertyValue(valuesContainer, "booleanValue", "true");
        BeanCreator.setPropertyValue(valuesContainer, "stringValue", "1234567");

        assertEquals(1, valuesContainer.getByteValue());
        assertEquals(12, valuesContainer.getShortValue());
        assertEquals(123, valuesContainer.getIntValue());
        assertEquals(1234L, valuesContainer.getLongValue());
        assertEquals(12345.0, valuesContainer.getDoubleValue());
        assertEquals(123456.0f, valuesContainer.getFloatValue());
        assertEquals('a', valuesContainer.getCharValue());
        assertTrue(valuesContainer.isBooleanValue());
        assertEquals("1234567", valuesContainer.getStringValue());
    }

    @Test
    @DisplayName("Test, setting property values;")
    public void testObtainPropertyValue() {
        byte expectedByteValue = 1;
        short expectedShortValue = 12;
        int expectedIntValue = 123;
        long expectedLongValue = 1234L;
        double expectedDoubleValue = 12345.0;
        float expectedFloatValue = 123456.0f;
        char expectedCharValue = 'a';
        boolean expectedBooleanValue = true;
        String expectedStringValue = "1234567";

        Object actualByteValue = BeanCreator.obtainValue("1", byte.class);
        Object actualShortValue = BeanCreator.obtainValue("12", short.class);
        Object actualIntValue = BeanCreator.obtainValue("123", int.class);
        Object actualLongValue = BeanCreator.obtainValue("1234", long.class);
        Object actualDoubleValue = BeanCreator.obtainValue("12345.0", double.class);
        Object actualFloatValue = BeanCreator.obtainValue("123456.0", float.class);
        Object actualCharValue = BeanCreator.obtainValue("a", char.class);
        Object actualBooleanValue = BeanCreator.obtainValue("true", boolean.class);
        Object actualStringValue = BeanCreator.obtainValue("1234567", String.class);

        assertEquals(expectedByteValue, actualByteValue);
        assertEquals(expectedShortValue, actualShortValue);
        assertEquals(expectedIntValue, actualIntValue);
        assertEquals(expectedLongValue, actualLongValue);
        assertEquals(expectedDoubleValue, actualDoubleValue);
        assertEquals(expectedFloatValue, actualFloatValue);
        assertEquals(expectedCharValue, actualCharValue);
        assertEquals(expectedBooleanValue, actualBooleanValue);
        assertEquals(expectedStringValue, actualStringValue);
    }

    @Test
    @DisplayName("Test, inject dependency value from bean definitions;")
    public void testInjectValueDependencies() {
        Map<String, Bean> beans = new HashMap<>();
        List<BeanDefinition> list = new ArrayList<>();

        beans.put("mailService", new Bean("mailService", new MailService()));
        beans.put("paymentService", new Bean("paymentService", new PaymentService()));
        beans.put("userService", new Bean("userService", new UserService()));

        list.add(new BeanDefinition("paymentService", "com.husky.container.entity.PaymentService",
                Map.of("paymentType", "visa"), new HashMap<>()));
        list.add(new BeanDefinition("mailService", "com.husky.container.entity.MailService",
                Map.of("protocol", "POP3", "timeout", "2000"), new HashMap<>()));
        list.add(new BeanDefinition("userService", "com.husky.container.entity.UserService",
                new HashMap<>(), Map.of("mailService", "mailService")));

        BeanCreator.injectValueDependencies(beans, list);

        String expectedPaymentType = "visa";
        String expectedProtocol = "POP3";
        int expectedTimeout = 2000;

        String actualPaymentType = ((PaymentService) beans.get("paymentService").getValue()).getPaymentType();
        String actualProtocol = ((MailService) beans.get("mailService").getValue()).getProtocol();
        int actualTimeout = ((MailService) beans.get("mailService").getValue()).getTimeout();

        assertEquals(expectedPaymentType, actualPaymentType);
        assertEquals(expectedProtocol, actualProtocol);
        assertEquals(expectedTimeout, actualTimeout);
    }

    @Test
    @DisplayName("Test, inject reference dependency value from bean definitions;")
    public void testInjectReferenceDependencies() {
        Map<String, Bean> beans = new HashMap<>();
        List<BeanDefinition> beanDefinitions = new ArrayList<>();

        beans.put("mailService", new Bean("mailService", new MailService()));
        beans.put("paymentService", new Bean("paymentService", new PaymentService()));
        beans.put("userService", new Bean("userService", new UserService()));

        beanDefinitions.add(new BeanDefinition("paymentService",
                "com.husky.container.entity.PaymentService",
                new HashMap<>(), Map.of("mailService", "mailService")));
        beanDefinitions.add(new BeanDefinition("mailService", "com.husky.container.entity.MailService",
                new HashMap<>(), new HashMap<>()));
        beanDefinitions.add(new BeanDefinition("userService", "com.husky.container.entity.UserService",
                new HashMap<>(), Map.of("mailService", "mailService")));

        BeanCreator.injectRefDependencies(beans, beanDefinitions);

        UserService user = (UserService) beans.get("userService").getValue();
        PaymentService payment = (PaymentService) beans.get("paymentService").getValue();
        MailService mailService = (MailService) beans.get("mailService").getValue();

        MailService userMailService = user.getMailService();
        MailService paymentMailService = payment.getMailService();

        assertNotNull(userMailService);
        assertNotNull(paymentMailService);

        assertSame(userMailService, paymentMailService);
        assertSame(mailService, userMailService);
        assertSame(mailService, paymentMailService);
    }

    @Test
    @DisplayName("Test, initialize beans from bean definitions;")
    public void testInitializeBeansFromBeanDefinitions() {
        List<BeanDefinition> beanDefinitions = new ArrayList<>();
        beanDefinitions.add(new BeanDefinition("paymentService",
                "com.husky.container.entity.PaymentService",
                Map.of("paymentType","visa"), Map.of("mailService", "mailService")));
        beanDefinitions.add(new BeanDefinition("mailService", "com.husky.container.entity.MailService",
                new HashMap<>(), new HashMap<>()));
        beanDefinitions.add(new BeanDefinition("userService", "com.husky.container.entity.UserService",
                new HashMap<>(), new HashMap<>()));

        Map<String, Bean> beans = BeanCreator.initializeBeans(beanDefinitions);

        PaymentService paymentService = (PaymentService) beans.get("paymentService").getValue();
        MailService mailService = (MailService) beans.get("mailService").getValue();
        UserService userService = (UserService) beans.get("userService").getValue();

        assertNotNull(paymentService);
        assertNotNull(mailService);
        assertNotNull(userService);
    }

    @Data
    private static class ValuesContainer {
        private byte byteValue;
        private short shortValue;
        private int intValue;
        private long longValue;
        private double doubleValue;
        private float floatValue;
        private char charValue;
        private boolean booleanValue;
        private String stringValue;
    }
}

