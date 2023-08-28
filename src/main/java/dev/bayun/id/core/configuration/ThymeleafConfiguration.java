package dev.bayun.id.core.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Configuration
public class ThymeleafConfiguration {

    @Bean
    public ClassLoaderTemplateResolver uiTemplateResolver() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setCacheable(false);
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setCheckExistence(true);
        templateResolver.setName("ui");
        templateResolver.setOrder(0);
        templateResolver.setPrefix("ui/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML5");

        return templateResolver;
    }

    @Bean
    public ClassLoaderTemplateResolver emailTemplateResolver() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setCacheable(false);
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setCheckExistence(true);
        templateResolver.setName("email");
        templateResolver.setOrder(1);
        templateResolver.setPrefix("email/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML5");

        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.addTemplateResolver(uiTemplateResolver());
        templateEngine.addTemplateResolver(emailTemplateResolver());

        return templateEngine;
    }

    @Bean
    public ViewResolver viewResolver() {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine());
        viewResolver.setCharacterEncoding("UTF-8");

        return viewResolver;
    }
}
