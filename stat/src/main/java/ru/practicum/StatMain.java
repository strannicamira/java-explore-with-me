package ru.practicum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StatMain {
    public static void main(String[] args) {
        System.out.println("Hello from stat module!");
        SpringApplication.run(StatMain.class, args);
    }
}