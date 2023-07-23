package com.husky.container.context;

import com.husky.container.entity.MailService;
import com.husky.container.entity.PaymentService;
import com.husky.container.entity.UserService;
import com.husky.container.exception.NoUniqueBeanDefinitionException;
import com.husky.container.reader.sax.SAXBeanDefinitionReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ClassPathApplicationContextITest {
    private final ClassPathApplicationContext DOM_CONTEXT =
            new ClassPathApplicationContext("default_content_test.xml", "default_import_content_test.xml");
    private final ClassPathApplicationContext SAX_CONTEXT =
            new ClassPathApplicationContext
                    (new SAXBeanDefinitionReader("default_content_test.xml", "default_import_content_test.xml"));
    private static final String MULTIPLY_BEAN_DEFINITION_PATH = "multiply_beans_content_test.xml";

    @Test
    @DisplayName("Test (DOM), create Beans, used Id & Clazz;")
    public void testDOMGetBeanWithValueAndRefDependenciesUsedIdAndClass() {
        PaymentService paymentService = DOM_CONTEXT.getBean("paymentService", PaymentService.class);
        MailService mailService = DOM_CONTEXT.getBean("mailService", MailService.class);
        UserService userService = DOM_CONTEXT.getBean("userService", UserService.class);

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

        assertSame(mailService, paymentService.getMailService());
        assertSame(mailService, userService.getMailService());
    }

    @Test
    @DisplayName("Test (SAX), create Beans, used Id & Clazz;")
    public void testSAXGetBeanWithValueAndRefDependenciesUsedIdAndClass() {
        PaymentService saxPaymentService = SAX_CONTEXT.getBean("paymentService", PaymentService.class);
        MailService saxMailService = SAX_CONTEXT.getBean("mailService", MailService.class);
        UserService saxUserService = SAX_CONTEXT.getBean("userService", UserService.class);

        String expectedPaymentType = "visa";
        String expectedMailServiceProtocol = "POP3";
        int expectedMailServiceTimeout = 2000;

        assertNotNull(saxPaymentService);
        assertNotNull(saxPaymentService.getMailService());
        assertNotNull(saxMailService);
        assertNotNull(saxUserService);
        assertNotNull(saxUserService.getMailService());

        assertEquals(expectedPaymentType, saxPaymentService.getPaymentType());
        assertEquals(expectedMailServiceProtocol, saxMailService.getProtocol());
        assertEquals(expectedMailServiceTimeout, saxMailService.getTimeout());

        assertSame(saxMailService, saxPaymentService.getMailService());
        assertSame(saxMailService, saxUserService.getMailService());
    }

    @Test
    @DisplayName("Test (DOM), create Beans, used Clazz;")
    public void testDOMGetBeanWithValueAndRefDependenciesUsedClass() {
        PaymentService paymentService = DOM_CONTEXT.getBean(PaymentService.class);
        MailService mailService = DOM_CONTEXT.getBean(MailService.class);
        UserService userService = DOM_CONTEXT.getBean(UserService.class);

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

        assertSame(mailService, paymentService.getMailService());
        assertSame(mailService, userService.getMailService());
    }

    @Test
    @DisplayName("Test (SAX), create Beans, used Clazz;")
    public void testSAXGetBeanWithValueAndRefDependenciesUsedClass() {
        PaymentService paymentService = SAX_CONTEXT.getBean(PaymentService.class);
        MailService mailService = SAX_CONTEXT.getBean(MailService.class);
        UserService userService = SAX_CONTEXT.getBean(UserService.class);

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

        assertSame(mailService, paymentService.getMailService());
        assertSame(mailService, userService.getMailService());
    }

    @Test
    @DisplayName("Test (DOM), create Beans, used Id;")
    public void testDOMGetBeansWithValueAndRefDependenciesUsedId() {
        PaymentService paymentService = (PaymentService) DOM_CONTEXT.getBean("paymentService");
        MailService mailService = (MailService) DOM_CONTEXT.getBean("mailService");
        UserService userService = (UserService) DOM_CONTEXT.getBean("userService");

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

        assertSame(mailService, paymentService.getMailService());
        assertSame(mailService, userService.getMailService());
    }

    @Test
    @DisplayName("Test (SAX), create Beans, used Id;")
    public void testSAXGetBeansWithValueAndRefDependenciesUsedId() {
        PaymentService paymentService = (PaymentService) SAX_CONTEXT.getBean("paymentService");
        MailService mailService = (MailService) SAX_CONTEXT.getBean("mailService");
        UserService userService = (UserService) SAX_CONTEXT.getBean("userService");

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

        assertSame(mailService, paymentService.getMailService());
        assertSame(mailService, userService.getMailService());
    }

    @Test
    @DisplayName("Test (DOM), get Bean names;")
    public void testDOMGetListOfBeans() {
        List<String> beans = DOM_CONTEXT.getBeansNames();
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
    @DisplayName("Test (SAX), get Bean names;")
    public void testSAXGetListOfBeans() {
        List<String> beans = SAX_CONTEXT.getBeansNames();
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
    @DisplayName("Test, validate multiply clazz;")
    public void testValidateMultiplyClassInstanceThrowException() {
        ClassPathApplicationContext domContext = new ClassPathApplicationContext(MULTIPLY_BEAN_DEFINITION_PATH);
        ClassPathApplicationContext saxContext = new ClassPathApplicationContext
                (new SAXBeanDefinitionReader(MULTIPLY_BEAN_DEFINITION_PATH));
        assertThrows(NoUniqueBeanDefinitionException.class, () -> domContext.validateClass(PaymentService.class));
        assertThrows(NoUniqueBeanDefinitionException.class, () -> saxContext.validateClass(PaymentService.class));
    }
}


