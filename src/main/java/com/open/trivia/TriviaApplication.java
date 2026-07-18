package com.open.trivia;

import com.open.trivia.service.TriviaQuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.util.List;

@SpringBootApplication
@EnableFeignClients
public class TriviaApplication {
    static void main(String[] args) {
        SpringApplication.run(TriviaApplication.class, args);
    }
}
