package ru.practicum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EwmMain {
    public static void main(String[] args) {

        System.out.println("Hello world from ewm main service!");
        SpringApplication.run(EwmMain.class, args);

    }
}