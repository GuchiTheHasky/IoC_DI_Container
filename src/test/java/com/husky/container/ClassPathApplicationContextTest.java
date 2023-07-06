package com.husky.container;

import com.husky.container.entity.Bean;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ClassPathApplicationContextTest {
    private final ClassPathApplicationContext applicationContext =
            new ClassPathApplicationContext<>("test_xml.xml");

    @Test
    @DisplayName("Test, get bean by class.")
    public void testGetBeanByClass() {
        String id = "paymentService";
        String value = "com.husky.container.entity.Bean";

        Bean expectedBean = Bean.builder()
                .value(value)
                .id(id)
                .build();

        Bean actualBean = (Bean) applicationContext.getBean(Bean.class);

        assertNotNull(actualBean);
        assertEquals(expectedBean.getId(), actualBean.getId());
        assertEquals(expectedBean.getValue(), actualBean.getValue());
        assertEquals(expectedBean.getClass(), actualBean.getClass());
    }

    @Test
    @DisplayName("Test, get bean by id & class.")
    public void testGetBeanByIdAndClass() {
        String id = "userService";
        String value = "com.husky.container.entity.Bean";

        Bean expectedBean = Bean.builder()
                .value(value)
                .id(id)
                .build();

        Bean actualBean = (Bean) applicationContext.getBean(id, Bean.class);

        assertNotNull(actualBean);
        assertEquals(expectedBean.getId(), actualBean.getId());
        assertEquals(expectedBean.getValue(), actualBean.getValue());
        assertEquals(expectedBean.getClass(), actualBean.getClass());
    }

    @Test
    @DisplayName("Test, get bean by id.")
    public void testGetBeanById() {
        String id = "userService";
        String value = "com.husky.container.entity.Bean";

        Bean expectedBean = Bean.builder()
                .value(value)
                .id(id)
                .build();

        Bean actualBean = (Bean) applicationContext.getBean(id);

        assertNotNull(actualBean);
        assertEquals(expectedBean.getId(), actualBean.getId());
        assertEquals(expectedBean.getValue(), actualBean.getValue());
        assertEquals(expectedBean.getClass(), actualBean.getClass());
    }

    @Test
    @DisplayName("Test, get beans id list.")
    public void testGetBeans() {
        List<String> beans = applicationContext.getBeans();
        int expectedCount = 2;
        int actualCount = beans.size();

        String expectedFirstBean = "paymentService";
        String actualFirstBean = beans.get(0);

        String expectedSecondBean = "userService";
        String actualSecondBean = beans.get(1);

        assertEquals(expectedCount, actualCount);
        assertEquals(expectedFirstBean, actualFirstBean);
        assertEquals(expectedSecondBean, actualSecondBean);
    }

    @Test
    @DisplayName("Test, get bean by class, return null.")
    public void testGetBeanReturnNull() {
        Bean bean = (Bean) applicationContext.getBean(String.class);
        assertNull(bean);
    }

    @Test
    @DisplayName("Test, get bean by id & class, return null.")
    public void testGetBeanByIdAndClassInvalidClassReturnNull() {
        String id = "userService";
        Bean actualBean = (Bean) applicationContext.getBean(id, String.class);

        assertNull(actualBean);
    }

    @Test
    @DisplayName("Test, get bean by id & class, return null.")
    public void testGetBeanByIdAndClassInvalidIdReturnNull() {
        String id = "invalidId";
        Bean actualBean = (Bean) applicationContext.getBean(id, Bean.class);

        assertNull(actualBean);
    }

    @Test
    @DisplayName("Test, get bean by id, return null.")
    public void testGetBeanByIdReturnNull() {
        String id = "invalidId";
        Bean actualBean = (Bean) applicationContext.getBean(id);

        assertNull(actualBean);
    }
}
