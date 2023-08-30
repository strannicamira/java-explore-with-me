package ru.practicum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StatClientMain {
    public static void main(String[] args) {
        System.out.println("Hello world from stat-client!");
        SpringApplication.run(StatClientMain.class, args);

    }
}