package com.example.carrental.security;

import com.example.carrental.config.ConfigUsers;
import com.example.carrental.domain.User.CarRentalUser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private static InMemoryUserDetailsManager inMemoryUserDetailsManager;
    private ConfigUsers configUsers;
    public SecurityConfig(ConfigUsers configUsers) {
        this.inMemoryUserDetailsManager = new InMemoryUserDetailsManager();
        this.configUsers = configUsers;
    }
    public static void addUserSecurity(CarRentalUser user) {
        inMemoryUserDetailsManager.createUser(User.withDefaultPasswordEncoder()
                .username(user.getUserLogin())
                .password(user.getUserPassword())
                .roles(user.getRole())
                .build());
    }
    @Bean
    public UserDetailsService userDetailsService() {
        for (ConfigUsers.User u : configUsers.getUser()) {
            UserDetails user = User.withDefaultPasswordEncoder()
                    .username(u.getName())
                    .password(u.getPassword())
                    .roles(u.getRole())
                    .build();
            inMemoryUserDetailsManager.createUser(user);
        }
        return inMemoryUserDetailsManager;
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().and().authorizeRequests()
                //actuator
                .antMatchers(HttpMethod.GET, "/actuator/**").hasRole("ADMIN")

                .and()
                .formLogin().permitAll()
                .and()
                .logout().permitAll()
                .and()
                .csrf().disable();
    }
}
