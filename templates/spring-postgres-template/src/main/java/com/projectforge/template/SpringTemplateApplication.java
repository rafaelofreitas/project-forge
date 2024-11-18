package com.projectforge.template;

import com.projectforge.template.infrastructure.config.WebServerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringTemplateApplication {

  public static void main(String[] args) {
    SpringApplication.run(WebServerConfig.class, args);
  }
}
