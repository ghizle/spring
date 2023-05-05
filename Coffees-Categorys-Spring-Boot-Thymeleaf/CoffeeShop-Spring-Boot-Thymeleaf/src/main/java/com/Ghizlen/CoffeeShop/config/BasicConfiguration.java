package com.Ghizlen.CoffeeShop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class BasicConfiguration {

    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails user = User.withUsername("user")
                .password(passwordEncoder.encode("password"))
                .roles("USER")
                .username("user")
                .build();

        UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder.encode("admin"))
                .roles("USER", "ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests((authorize) ->
                        authorize.requestMatchers("/addCoffee").hasRole("ADMIN")
                                .requestMatchers("/saveCoffee").hasRole("ADMIN")
                                .requestMatchers("/updateCoffee").hasRole("ADMIN")
                                .requestMatchers("/updateCoffee/**").hasRole("ADMIN")
                                .requestMatchers("/coffeesTemplate/delete/**").hasRole("ADMIN")

                                .requestMatchers("/addCategory").hasRole("ADMIN")
                                .requestMatchers("/saveCategory").hasRole("ADMIN")
                                .requestMatchers("/update").hasRole("ADMIN")
                                .requestMatchers("/updateCategory").hasRole("ADMIN")
                                .requestMatchers("/categorysTemplate/delete/**").hasRole("ADMIN")

                                .anyRequest().authenticated()

                ).formLogin(
                        form -> form
                                .loginPage("/login") // set login page to /login
                                .loginProcessingUrl("/login") // set the URL to submit the login form
                                .defaultSuccessUrl("/", true) // set the default URL to redirect after successful login
                                .permitAll()

                ).logout(
                        logout -> logout
                                .clearAuthentication(true)
                                .deleteCookies("JSESSIONID")
                                .invalidateHttpSession(true)
                                .logoutSuccessUrl("/login")
                ).exceptionHandling(
                        e -> e.accessDeniedHandler((request, response, accessDeniedException) -> {
                            String redirectUrl = "";
                            String requestPath = request.getServletPath();
                            if (requestPath.startsWith("/updateCoffee")) redirectUrl = "/coffees-list";
                            else if(requestPath.equals("/addCategory")) redirectUrl = "/categorys-list";
                            else if (requestPath.startsWith("/saveCategory")) redirectUrl = "/categorys-list";
                            else if (requestPath.startsWith("/update")) redirectUrl = "/categorys-list";
                            else if (requestPath.startsWith("/categorysTemplate/delete/")) redirectUrl = "/categorys-list";
                            else redirectUrl = "/coffees-list";

                            response.sendRedirect(redirectUrl);
                        })
                );
        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return encoder;
    }
}
