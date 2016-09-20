package org.yeahka.config;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@ComponentScan(basePackages="org.yeahka")
@ImportResource(value = {"classpath:spring-mybatis-mall.xml","classpath:spring.xml"})
@EnableWebMvc
public class MvcConfiguration extends WebMvcConfigurerAdapter{

	@Bean
	public ViewResolver getViewResolver(){
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setPrefix("/WEB-INF/views/");
		resolver.setSuffix(".jsp");
		return resolver;
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		// TODO Auto-generated method stub
		StringHttpMessageConverter mc = new StringHttpMessageConverter();
		List<MediaType> list=new ArrayList<MediaType>();
		list.add(new MediaType("application", "json",Charset.forName("UTF-8")));
		list.add(new MediaType("text", "plain",Charset.forName("UTF-8")));
		list.add(new MediaType("text", "x-c",Charset.forName("UTF-8")));
		list.add(new MediaType("text", "html",Charset.forName("UTF-8")));
		mc.setSupportedMediaTypes(list);
		converters.add(mc);
		super.configureMessageConverters(converters);

	}




}
