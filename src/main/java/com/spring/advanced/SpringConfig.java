package com.spring.advanced;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SpringConfig extends WebSecurityConfigurerAdapter {
        // To provide our own user name and password
        @Override
        protected void configure(AuthenticationManagerBuilder auth)
                throws Exception {
            auth.inMemoryAuthentication().passwordEncoder(org.springframework.security.crypto.password.
                    NoOpPasswordEncoder.getInstance()).withUser("user1").password("secret1")
                    .roles("USER").and().withUser("admin1").password("secret1")
                    .roles("ADMIN");
        }
        // To describe roles
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.httpBasic().and().authorizeRequests().
                    antMatchers("/surveys/**")
                    .hasRole("USER").antMatchers("/users/**").hasRole("USER")
                    .antMatchers("/**").hasRole("ADMIN").and().csrf().disable()
                    .headers().frameOptions().disable();
        }
}

