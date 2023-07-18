package com.husky.container;

import com.husky.container.entity.BeanDefinition;
import com.husky.container.entity.MailService;
import com.husky.container.entity.PaymentService;
import com.husky.container.entity.UserService;
import com.husky.container.util.BeanInstantiationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ClassPathApplicationContextITest {
    private final ClassPathApplicationContext appContext =
            new ClassPathApplicationContext("default_content_test.xml", "default_import_content_test.xml");

    @Test
    @DisplayName("Test, create bean with value and ref dependencies, used id and class")
    public void testCreateBeansWithValueAndRefDependenciesUsedIdAndClass() {
        PaymentService paymentService = appContext.getBean("paymentService", PaymentService.class);
        MailService mailService = appContext.getBean("mailService", MailService.class);
        UserService userService = appContext.getBean("userService", UserService.class);

        String expectedPaymentType = "visa";
        String expectedMailServiceProtocol = "POP3";
        int expectedMailServiceTimeout = 2000;

        assertNotNull(paymentService);
        assertNotNull(paymentService.getMailService());
        assertNotNull(mailService);
        assertNotNull(userService);
        assertNotNull(userService.getMailService());

        assertEquals(expectedPaymentType, paymentService.getPaymentType());
        assertEquals(expectedMailServiceProtocol, mailService.getProtocol());
        assertEquals(expectedMailServiceTimeout, mailService.getTimeout());
    }

    @Test
    @DisplayName("Test, create bean with value and ref dependencies, used class")
    public void testCreateBeansWithValueAndRefDependenciesUsedClass() {
        PaymentService paymentService = appContext.getBean(PaymentService.class);
        MailService mailService = appContext.getBean(MailService.class);
        UserService userService = appContext.getBean(UserService.class);

        String expectedPaymentType = "visa";
        String expectedMailServiceProtocol = "POP3";
        int expectedMailServiceTimeout = 2000;

        assertNotNull(paymentService);
        assertNotNull(paymentService.getMailService());
        assertNotNull(mailService);
        assertNotNull(userService);
        assertNotNull(userService.getMailService());

        assertEquals(expectedPaymentType, paymentService.getPaymentType());
        assertEquals(expectedMailServiceProtocol, mailService.getProtocol());
        assertEquals(expectedMailServiceTimeout, mailService.getTimeout());
    }

    @Test
    @DisplayName("Test, create bean with value and ref dependencies, used id")
    public void testCreateBeansWithValueAndRefDependenciesUsedId() {
        PaymentService paymentService = (PaymentService) appContext.getBean("paymentService");
        MailService mailService = (MailService) appContext.getBean("mailService");
        UserService userService = (UserService) appContext.getBean("userService");

        String expectedPaymentType = "visa";
        String expectedMailServiceProtocol = "POP3";
        int expectedMailServiceTimeout = 2000;

        assertNotNull(paymentService);
        assertNotNull(paymentService.getMailService());
        assertNotNull(mailService);
        assertNotNull(userService);
        assertNotNull(userService.getMailService());

        assertEquals(expectedPaymentType, paymentService.getPaymentType());
        assertEquals(expectedMailServiceProtocol, mailService.getProtocol());
        assertEquals(expectedMailServiceTimeout, mailService.getTimeout());
    }

    @Test
    @DisplayName("Test, create bean with value dependencies, used id and class")
    public void testGetListOfBeans() {
        List<String> beans = appContext.getBeans();
        String expectedFirstBean = "paymentService";
        String expectedSecondBean = "mailService";
        String expectedThirdBean = "userService";
        int expectedListSize = 3;

        assertNotNull(beans);
        assertEquals(expectedListSize, beans.size());
        assertEquals(expectedFirstBean, beans.get(0));
        assertEquals(expectedSecondBean, beans.get(1));
        assertEquals(expectedThirdBean, beans.get(2));
    }

    @Test
    @DisplayName("Test, convert value.")
    public void testConvertValue() {
        String expectedStrValue = "Guchi";
        String actualStrValue = (String) appContext.convertValue("Guchi", String.class);

        int expectedIntValue = 2000;
        int actualIntValue = Integer.parseInt((String) appContext.convertValue("2000", String.class));

        String actualNullValue = (String) appContext.convertValue(null, String.class);

        assertEquals(expectedStrValue, actualStrValue);
        assertEquals(expectedIntValue, actualIntValue);
        assertNull(actualNullValue);
    }

    @Test
    @DisplayName("Test, create bean from bean definition.")
    public void testCreateBeanFromBeanDefinition() {
        BeanDefinition beanDefinition = mock(BeanDefinition.class);
        when(beanDefinition.getBeanClassName()).thenReturn("com.husky.container.entity.MailService");

        Object testBean = appContext.createBeanFromBeanDefinition(beanDefinition);

        assertNotNull(testBean);
        assertEquals(MailService.class, testBean.getClass());
    }

    @Test
    @DisplayName("Test, create bean from bean definition throw exception.")
    public void testCreateBeanFromBeanDefinitionThrowException() {
        BeanDefinition beanDefinition = mock(BeanDefinition.class);
        when(beanDefinition.getBeanClassName()).thenReturn("com.husky.container.entity.TestService");

        assertThrows(BeanInstantiationException.class, () -> appContext.createBeanFromBeanDefinition(beanDefinition));
    }
}
