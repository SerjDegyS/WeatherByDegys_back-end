package com.weatherbydegys.backend.config;

import com.weatherbydegys.backend.model.User;
import com.weatherbydegys.backend.repos.UserRepo;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;

//@Configuration
//@EnableWebMvcSecurity
//@Order(-100)
//@EnableOAuth2Sso
public class OAuthSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .mvcMatcher("/**").authorizeRequests()
                .antMatchers("/", "/login**", "/webjars/**", "/error**").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .logout().logoutSuccessUrl("/").permitAll()
                .and()
                .csrf().disable();

//                .authorizeRequests()
//                .mvcMatchers("/img/**", "/static/**").permitAll()
//                .mvcMatchers("/", "/login", "/login/process", "/registration").anonymous()
//                .mvcMatchers( "/api/**").authenticated()
//                .and()
//                .logout().logoutSuccessUrl("/").permitAll()
//                .and()
//                .csrf().disable();
    }

    @Bean
    public PrincipalExtractor principalExtractor(UserRepo userRepo) {
        return map -> {
            return new User();
        };
    }
}
