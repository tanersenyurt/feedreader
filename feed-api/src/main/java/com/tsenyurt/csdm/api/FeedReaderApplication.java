package com.tsenyurt.csdm.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.tsenyurt.csdm.**")
@EntityScan(basePackages = "com.tsenyurt.csdm.**")
@ComponentScan(basePackages = "com.tsenyurt.csdm.**")
public class FeedReaderApplication {
  public static void main(String[] args) {
    SpringApplication.run(FeedReaderApplication.class, args);
  }
}
