package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by linseb on 05/08/16.
 */
@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan
public class CfClientTestMain {

    public static void main(String [] args) {
        SpringApplication.run(CfClientTestMain.class, args);
    }
}
