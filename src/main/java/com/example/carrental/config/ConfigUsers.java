package com.example.carrental.config;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties("carrental")
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ConfigUsers {

    private List<User> user = new ArrayList<>();

    @Data
    public static class User {
        private String name;
        private String password;
        private String role;
    }


}
