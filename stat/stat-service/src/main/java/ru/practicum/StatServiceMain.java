package ru.practicum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StatServiceMain {
    public static void main(String[] args) {
        System.out.println("Hello from stat-service!");
        SpringApplication.run(StatServiceMain.class, args);
    }
}