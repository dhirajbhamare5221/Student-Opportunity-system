package com.platform.opportunity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Entry Point for the Student Opportunity Discovery Platform.
 * 
 * @SpringBootApplication enables:
 * 1. @Configuration: Tags the class as a source of bean definitions.
 * 2. @EnableAutoConfiguration: Tells Spring Boot to start adding beans based on classpath settings.
 * 3. @ComponentScan: Scans this package and sub-packages for @Component, @Service, @Repository, @Controller.
 */
@SpringBootApplication
public class OpportunityPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpportunityPlatformApplication.class, args);
        System.out.println("=================================================");
        System.out.println("🚀 OpportunityHub Application started successfully!");
        System.out.println("🌐 Open in browser: http://localhost:8080");
        System.out.println("💾 Database Console: http://localhost:8080/h2-console");
        System.out.println("=================================================");
    }
}
