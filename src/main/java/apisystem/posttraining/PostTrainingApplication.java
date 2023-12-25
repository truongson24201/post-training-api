package apisystem.posttraining;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;


@SpringBootApplication
//@EnableRedisHttpSession
//@EnableCaching
public class PostTrainingApplication {

    public static void main(String[] args) {
        SpringApplication.run(PostTrainingApplication.class, args);
    }

}
