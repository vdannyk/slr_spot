package com.dkwasniak.slrspotbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class SlrSpotBackendApplication {

  public static void main(String[] args) {
    SpringApplication.run(SlrSpotBackendApplication.class, args);
  }

}
