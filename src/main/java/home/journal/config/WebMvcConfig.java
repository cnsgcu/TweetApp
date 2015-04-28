package home.journal.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

@EnableWebMvc
@Configuration
@ComponentScan(basePackages = {"home.journal"})
public class WebMvcConfig extends WebMvcConfigurerAdapter
{
    final static private Logger LOGGER = LoggerFactory.getLogger(WebMvcConfig.class);

    /**
     * Configure static resources handler
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
        LOGGER.info("Configure static resources resolver.");

        registry.addResourceHandler("/resources/**").addResourceLocations("/WEB-INF/resources/");
    }

    /**
     * Configure view resolver
     *
     * @return an instance of ViewResolver
     */
    @Bean
    public ViewResolver viewResolver()
    {
        LOGGER.info("Configure Thymeleaf view resolver.");

        ThymeleafViewResolver thymeleafViewResolver = new ThymeleafViewResolver();

        thymeleafViewResolver.setTemplateEngine(templateEngine());
        thymeleafViewResolver.setOrder(1);

        return thymeleafViewResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine()
    {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();

        templateEngine.setTemplateResolver(templateResolver());

        return templateEngine;
    }

    @Bean
    public ServletContextTemplateResolver templateResolver()
    {
        ServletContextTemplateResolver resolver = new ServletContextTemplateResolver();

        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML5");
        resolver.setCacheable(false);

        return resolver;
    }
}