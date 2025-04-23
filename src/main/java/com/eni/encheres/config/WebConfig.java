package com.eni.encheres.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.http.CacheControl;

import java.util.concurrent.TimeUnit;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Expose le dossier uploads/articles comme une ressource statique
        registry.addResourceHandler("/imgs/Articles/**")
                .addResourceLocations("file:" + System.getProperty("user.dir") + "/uploads/articles/");
    }

}
