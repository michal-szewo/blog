package pl.edu.pw.blog.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

/**
 *  Customization of the Java-based configuration for Spring MVC.
 * 
 * @author Michal
 *
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

  
  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
    registry.addViewController("/").setViewName("articles");
    registry.addViewController("/login");
  }
  
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
     registry
             .addResourceHandler("/images/**")
             .addResourceLocations("file:///D:/Michal/images/")
             .resourceChain(true)
             .addResolver(new PathResourceResolver());
  }

}