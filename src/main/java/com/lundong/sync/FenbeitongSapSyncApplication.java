package com.lundong.sync;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FenbeitongSapSyncApplication {

    public static void main(String[] args) {
        SpringApplication.run(FenbeitongSapSyncApplication.class, args);
    }

}
