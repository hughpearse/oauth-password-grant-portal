package usrportal.config;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.StringTemplateResolver;

@Configuration
public class ThymeleafConfig implements ApplicationContextAware, EnvironmentAware {

  @Autowired
  private ApplicationContext applicationContext;

  @Autowired
  private Environment environment;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    applicationContext = this.applicationContext;
  }

  @Override
  public void setEnvironment(Environment environment) {
    environment = this.environment;
  }

  @Bean
  public ResourceBundleMessageSource messageSource() {
    final ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    messageSource.setBasename("i18n/messages");
    return messageSource;
  }

  @Bean
  public TemplateEngine appTemplateEngine() {
    SpringTemplateEngine engine = new SpringTemplateEngine();
    engine.addTemplateResolver(textTemplateResolver());
    engine.addTemplateResolver(htmlTemplateResolver());
    engine.addTemplateResolver(stringTemplateResolver());
    engine.setTemplateEngineMessageSource(messageSource());
    return engine;
  }

  private ITemplateResolver textTemplateResolver() {
    final ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
    resolver.setOrder(Integer.valueOf(1));
    resolver.setResolvablePatterns(Collections.singleton("text/*"));
    resolver.setPrefix("templates/");
    resolver.setSuffix(".txt");
    resolver.setTemplateMode(TemplateMode.TEXT);
    resolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
    resolver.setCacheable(false);
    return resolver;
  }

  private ITemplateResolver htmlTemplateResolver() {
    ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
    resolver.setOrder(Integer.valueOf(2));
    resolver.setResolvablePatterns(Collections.singleton("html/*"));
    resolver.setPrefix("/templates/");
    resolver.setSuffix(".html");
    resolver.setTemplateMode(TemplateMode.HTML);
    resolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
    resolver.setCacheable(false);
    return resolver;
  }

  private ITemplateResolver stringTemplateResolver() {
    final StringTemplateResolver resolver = new StringTemplateResolver();
    resolver.setOrder(Integer.valueOf(3));
    resolver.setTemplateMode(TemplateMode.HTML);
    resolver.setCacheable(false);
    return resolver;
  }
}
