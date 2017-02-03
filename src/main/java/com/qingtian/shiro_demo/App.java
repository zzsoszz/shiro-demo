package com.qingtian.shiro_demo;

import org.apache.shiro.web.servlet.AdviceFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainer;
import org.springframework.context.ConfigurableApplicationContext;
import com.qingtian.shiro_demo.entity.Customer;
import com.qingtian.shiro_demo.repository.CustomerRepository;


/**
 * Hello world!
 *
 */
@SpringBootApplication
public class App 
{
	TomcatEmbeddedServletContainer ss;
	EmbeddedServletContainerFactory ssss;
    public static void main( String[] args )
    {
		ConfigurableApplicationContext context = SpringApplication.run(App.class, args);
		CustomerRepository customerRepository = context.getBean(CustomerRepository.class);  
	    // 内存数据库操作  
		Customer customer = new Customer();
		customer.setUsername("admin");
		customer.setPwd("admin");
		customer.setNickName("zrk1000");
		customer.setTel("18888888888");
		customer.setEmail("zrk1000@163.com");
		customer.setHeadImg("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=266158537,114847377&fm=116&gp=0.jpg");
		customer.setUseable(true);
		customerRepository.save(customer);  
		customerRepository.findAll().stream().forEach(System.out::println);
        System.out.println( "Hello World!" );
    }
}
