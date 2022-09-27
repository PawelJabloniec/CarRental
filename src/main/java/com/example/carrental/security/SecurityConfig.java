package com.example.carrental.security;

import com.example.carrental.config.ConfigUsers;
import com.example.carrental.domain.User.CarRentalUser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static InMemoryUserDetailsManager inMemoryUserDetailsManager;

    private ConfigUsers configUsers;

    public SecurityConfig(ConfigUsers configUsers) {
        this.inMemoryUserDetailsManager = new InMemoryUserDetailsManager();
        this.configUsers = configUsers;
    }

    public static void addUserSecurity(CarRentalUser user){
        inMemoryUserDetailsManager.createUser(User.withDefaultPasswordEncoder()
                .username(user.getUserLogin())
                .password(user.getUserPassword())
                .roles(user.getRole())
                .build());
    }

    @Bean
    public UserDetailsService userDetailsService(){
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
                //Car permissions
                .antMatchers(HttpMethod.POST,"/car/{id}").hasAnyRole("MODERATOR", "ADMIN")
                .antMatchers(HttpMethod.PUT,"/car/{id}").hasAnyRole("MODERATOR", "ADMIN")
                .antMatchers(HttpMethod.GET,"/car").permitAll()
                .antMatchers(HttpMethod.GET,"/car/{id}").permitAll()
                .antMatchers(HttpMethod.GET,"/car/filterByCarStatus/{carStatus}").permitAll()
                .antMatchers(HttpMethod.GET,"/car/filterByBodyType/{bodyType}").permitAll()
                .antMatchers(HttpMethod.GET,"/car/filterByPrice").permitAll()
                .antMatchers(HttpMethod.DELETE,"/car/{id}").hasRole("ADMIN")
                //User permissions
                .antMatchers(HttpMethod.POST,"/user").permitAll()
                .antMatchers(HttpMethod.PUT,"/user/{id}").hasAnyRole("MODERATOR", "ADMIN")
                .antMatchers(HttpMethod.GET,"/user").hasAnyRole("MODERATOR", "ADMIN")
                .antMatchers(HttpMethod.GET,"/user/{id}").hasAnyRole("MODERATOR", "ADMIN")
                .antMatchers(HttpMethod.GET,"/user/userEmail/{email}").hasAnyRole("MODERATOR", "ADMIN")
                .antMatchers(HttpMethod.GET,"/user/userLogin/{login}").hasAnyRole("MODERATOR", "ADMIN")
                .antMatchers(HttpMethod.DELETE,"/user/{id}").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST,"/user/log/in").permitAll()
                //CarRentalOffice permissions
                .antMatchers(HttpMethod.GET,"/carRentalOffice").hasAnyRole("MODERATOR", "ADMIN")
                .antMatchers(HttpMethod.GET,"/carRentalOffice/{id}").hasAnyRole("MODERATOR", "ADMIN")
                .antMatchers(HttpMethod.GET,"/carRentalOffice/dateTime/{localDateTime}").hasAnyRole("MODERATOR", "ADMIN")
                .antMatchers(HttpMethod.PUT,"/carRentalOffice/rentCar/{userId}/{carId}").hasAnyRole("MODERATOR", "ADMIN", "USER")
                .antMatchers(HttpMethod.PUT,"/carRentalOffice/returnCar/{carRentalOfficeId}").hasAnyRole("MODERATOR", "ADMIN", "USER")
                //Income permissions
                .antMatchers(HttpMethod.GET,"/income").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET,"/income/{id}").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET,"/income/month/{month}").hasRole("ADMIN")
                //Rental branch permissions
                .antMatchers(HttpMethod.GET,"/rentalBranch").permitAll()
                .antMatchers(HttpMethod.GET,"/rentalBranch/{id}").permitAll()
                .antMatchers(HttpMethod.POST,"/rentalBranch").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE,"/rentalBranch/{id}").hasRole("ADMIN")
                //actuator
                .antMatchers(HttpMethod.GET,"/actuator/**").hasRole("ADMIN")

                .and()
                .formLogin().permitAll()
                .and()
                .logout().permitAll()
                .and()
                .csrf().disable();
    }
}
