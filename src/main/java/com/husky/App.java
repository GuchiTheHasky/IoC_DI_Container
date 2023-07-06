package com.husky;

import com.husky.container.ClassPathApplicationContext;
import com.husky.container.entity.Bean;

import java.util.List;

public class App
{
    public static void main( String[] args )
    {
        ClassPathApplicationContext<Bean> context = new ClassPathApplicationContext<>("context.xml", "config.xml");

        Bean firstBean = context.getBean(Bean.class);
        firstBean.yes();
        Bean bean1 = (Bean) context.getBean("mailService");
        bean1.yes();
        Bean bean2 = (Bean) context.getBean("userService");
        bean2.yes();
        Bean bean3 = (Bean) context.getBean("paymentService");
        bean3.yes();
        Bean bean11 = context.getBean("mailService", Bean.class);
        bean11.yes();
        Bean bean22 = context.getBean("userService", Bean.class);
        bean22.yes();
        Bean bean33 = context.getBean("paymentService", Bean.class);
        bean33.yes();

        List<String> beans = context.getBeans();
        System.out.println(beans);
    }

}

