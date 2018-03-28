package com.cxk.producter;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 18/3/28
 *
 * @author chenxiaokai
 **/
@Component
public class ProducerMain {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"application-context.xml"});

        MyProducer contextBean = context.getBean(MyProducer.class);
        contextBean.sendMessage();
    }
}
