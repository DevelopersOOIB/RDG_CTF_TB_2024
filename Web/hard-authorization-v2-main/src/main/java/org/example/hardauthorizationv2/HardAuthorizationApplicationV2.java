package org.example.hardauthorizationv2;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HardAuthorizationApplicationV2 {
    @Value("${flag}")
    private static String flag;

    public static void main(String[] args) {
        SpringApplication.run(HardAuthorizationApplicationV2.class, args);
    }

}
