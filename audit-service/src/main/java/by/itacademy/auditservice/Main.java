package by.itacademy.auditservice;

import by.itacademy.auditservice.config.properties.AppProperties;
import by.itacademy.auditservice.config.properties.JWTProperty;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableFeignClients
@EnableConfigurationProperties({JWTProperty.class, AppProperties.class})
@EnableTransactionManagement
public class    Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);

    }
}