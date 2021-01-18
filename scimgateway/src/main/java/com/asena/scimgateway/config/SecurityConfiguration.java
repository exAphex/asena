package com.asena.scimgateway.config;

import com.asena.scimgateway.security.service.AUserDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private AUserDetailsService userDetailsService;
    
    @Configuration
    @Order(1)
    public static class ApiWebSecurityConfig extends WebSecurityConfigurerAdapter{

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .antMatcher("/gateway/**")
                    .httpBasic()
                        .realmName("API")
                        .and()
                    .csrf().disable()
                    .authorizeRequests()
                    .antMatchers("/gateway/**").authenticated();
        }
    }

    @Configuration
    @Order(2)
    public static class ApiTokenSecurityConfig extends WebSecurityConfigurerAdapter{

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable();
            http.requestMatcher(new NegatedRequestMatcher(new AntPathRequestMatcher("/gateway/**"))).authorizeRequests()
                .anyRequest().authenticated()
                .and().formLogin().loginPage("/login.html")
                .loginProcessingUrl("/perform_login")
                .defaultSuccessUrl("/index.html", true)
                .failureUrl("/login.html?error=true").permitAll()
                .and().logout().permitAll();
        }

    }

    @Autowired
    public void globalSecurityConfiguration(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Bean
    public static NoOpPasswordEncoder passwordEncoder() {
    return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }

    @Bean
public CommonsRequestLoggingFilter requestLoggingFilter() {
    CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter();
    loggingFilter.setIncludeClientInfo(true);
    loggingFilter.setIncludeQueryString(true);
    loggingFilter.setIncludePayload(true);
    loggingFilter.setIncludeHeaders(false);
    return loggingFilter;
}
}