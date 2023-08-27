package dev.bayun.id.core.configuration;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.FormContentFilter;
import org.springframework.web.servlet.config.annotation.*;

@EnableWebMvc
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/login/**").setViewName("login");
        registry.addViewController("/signup/**").setViewName("signup");
        registry.setOrder(1);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/assets/**").addResourceLocations("classpath:/ui/assets/");
        registry.setOrder(0);
    }

    @Bean
    public FilterRegistrationBean<FormContentFilter> formContentFilterRegistrationBean() {
        FilterRegistrationBean<FormContentFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new FormContentFilter());

        return registrationBean;
    }
}
