package com.tsenyurt.csdm.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.tsenyurt.csdm")
public class FeedReaderApplication {
  public static void main(String[] args) {
    SpringApplication.run(FeedReaderApplication.class, args);
  }
}
