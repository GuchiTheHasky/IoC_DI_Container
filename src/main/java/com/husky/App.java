package com.husky;

import com.husky.container.ClassPathApplicationContext;
import com.husky.container.entity.Bean;
import com.husky.container.reader.XMLBeanDefinitionReader;

import java.util.List;

public class App
{
    public static void main( String[] args )
    {
        ClassPathApplicationContext<Bean> context = new ClassPathApplicationContext<>();
        context.setBeanDefinitionReader(new XMLBeanDefinitionReader("context.xml", "config.xml"));
        context.loadBeanDefinitions();
        context.createBeansFromBeanDefinition();
        context.injectDependencies();

        Bean firstBean = context.getBean(Bean.class);
        firstBean.yes();
        Bean bean1 = (Bean) context.getBean("first");
        bean1.yes();
        Bean bean2 = (Bean) context.getBean("second");
        bean2.yes();
        Bean bean3 = (Bean) context.getBean("third");
        bean3.yes();
        Bean bean11 = context.getBean("first", Bean.class);
        bean11.yes();
        Bean bean22 = context.getBean("second", Bean.class);
        bean22.yes();
        Bean bean33 = context.getBean("third", Bean.class);
        bean33.yes();
        Bean bean4 = (Bean) context.getBean("fourth");
        bean4.yes();
        Bean bean44 = context.getBean("fourth", Bean.class);
        bean44.yes();
        Bean bean5 = (Bean) context.getBean("fifth");
        bean5.yes();
        Bean bean55 = context.getBean("fifth", Bean.class);
        bean55.yes();

        List<String> beans = context.getBeans();
        System.out.println(beans);
    }

}

