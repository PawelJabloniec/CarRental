package com.example.carrental.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public UserDetailsService userDetailsService(){
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("user")
                .roles("USER")
                .build();
        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("admin")
                .roles("ADMIN")
                .build();
        UserDetails moderator = User.withDefaultPasswordEncoder()
                .username("moderator")
                .password("moderator")
                .roles("MODERATOR")
                .build();
        return new InMemoryUserDetailsManager(admin, user, moderator);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().and().authorizeRequests()
                //Car permissions
                .antMatchers(HttpMethod.GET,"/car").permitAll()
                .antMatchers(HttpMethod.GET,"/car/filterByCarStatus/{carStatus}").permitAll()
                .antMatchers(HttpMethod.GET,"/car/filterByBodyType/{bodyType}").permitAll()
                .antMatchers(HttpMethod.GET,"/car/filterByPrice").permitAll()
                .antMatchers(HttpMethod.POST,"/car/{id}").hasAnyRole("MODERATOR", "ADMIN")
                .antMatchers(HttpMethod.PUT,"/car/{id}").hasAnyRole("MODERATOR", "ADMIN")
                .antMatchers(HttpMethod.DELETE,"/car/{id}").hasRole("ADMIN")
                //User permissions
                .antMatchers(HttpMethod.GET,"/user").hasAnyRole("MODERATOR", "ADMIN")
                .antMatchers(HttpMethod.POST,"/user").hasAnyRole("MODERATOR", "ADMIN")
                .antMatchers(HttpMethod.PUT,"/user").hasAnyRole("MODERATOR", "ADMIN")
                .antMatchers(HttpMethod.DELETE,"/user").hasAnyRole("MODERATOR", "ADMIN")
                //CarRentalOffice permissions
                .antMatchers(HttpMethod.GET,"/carRentalOffice").hasAnyRole("MODERATOR", "ADMIN")
                .antMatchers(HttpMethod.PUT,"/carRentalOffice").hasAnyRole("MODERATOR", "ADMIN")
                .antMatchers(HttpMethod.DELETE,"/carRentalOffice").hasRole("ADMIN")
                //Income permissions
                .antMatchers(HttpMethod.POST,"/carRentalOffice").hasRole("MODERATOR")
                //Rental branch permissions
                .antMatchers(HttpMethod.GET,"/rentalBranch").permitAll()
                .antMatchers(HttpMethod.POST,"/rentalBranch").hasAnyRole("MODERATOR", "ADMIN")
                .antMatchers(HttpMethod.DELETE,"/rentalBranch").hasRole("ADMIN")

                .and()
                .formLogin().permitAll()
                .and()
                .logout().permitAll()
                .and()
                .csrf().disable();
    }
}
