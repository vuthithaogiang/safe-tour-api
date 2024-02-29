package com.project.shopapp.configs;


import com.project.shopapp.filters.JwtTokenFilter;
import com.project.shopapp.models.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private  final JwtTokenFilter jwtTokenFilter;

    @Value("${api.prefix}")
    private  String apiPrefix;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
        throws  Exception{
         http
                 .csrf(AbstractHttpConfigurer::disable)
                 .addFilterBefore(jwtTokenFilter,
                         UsernamePasswordAuthenticationFilter.class)
                 .authorizeHttpRequests(requests -> {
                     requests.
                             //pass by
                             requestMatchers(
                                     String.format("%s/users/register", apiPrefix),
                                     String.format("%s/users/login", apiPrefix)
                             )
                             .permitAll()

                             //categories
                             .requestMatchers(HttpMethod.GET,
                                     String.format("%s/categories**", apiPrefix)).hasAnyRole(Role.USER, Role.ADMIN)
                             .requestMatchers(HttpMethod.POST,
                                     String.format("%s/categories", apiPrefix)).hasRole(Role.ADMIN)
                             .requestMatchers(HttpMethod.PUT,
                                     String.format("%s/categories/**", apiPrefix)).hasRole(Role.ADMIN)
                             .requestMatchers(HttpMethod.DELETE,
                                     String.format("%s/categories/**", apiPrefix)).hasRole(Role.ADMIN)

                             //products
                             .requestMatchers(HttpMethod.GET,
                                     String.format("%s/products**", apiPrefix)).hasAnyRole(Role.ADMIN, Role.USER)
                             .requestMatchers(HttpMethod.GET,
                                     String.format("%s/products/**", apiPrefix)).hasAnyRole(Role.USER, Role.ADMIN)
                             .requestMatchers(HttpMethod.POST,
                                     String.format("%s/products", apiPrefix)).hasRole( Role.ADMIN)
                             .requestMatchers(HttpMethod.POST,
                                     String.format("%s/products/upload/**", apiPrefix)).hasRole(Role.ADMIN)
                             .requestMatchers(HttpMethod.PUT,
                                     String.format("%s/products/**", apiPrefix)).hasRole(Role.ADMIN)
                             .requestMatchers(HttpMethod.DELETE,
                                     String.format("%s/products/**", apiPrefix)).hasRole(Role.ADMIN)



                             //orders
                             .requestMatchers(HttpMethod.POST,
                                     String.format("%s/orders", apiPrefix)).hasRole(Role.USER)
                             .requestMatchers(HttpMethod.GET,
                                     String.format("%s/orders/**", apiPrefix)).hasAnyRole(Role.USER, Role.ADMIN)
                             .requestMatchers(HttpMethod.PUT,
                                     String.format("%s/orders/**", apiPrefix)).hasRole(Role.ADMIN)
                             .requestMatchers(HttpMethod.DELETE,
                                     String.format("%s/orders/**", apiPrefix)).hasRole(Role.ADMIN)

                             //order-details
                             .requestMatchers(HttpMethod.GET,
                                     String.format("%s/order-details/**", apiPrefix)).hasAnyRole(Role.USER, Role.ADMIN)
                             .requestMatchers(HttpMethod.GET,
                                     String.format("%s/order-details/order/**", apiPrefix)).hasAnyRole(Role.USER, Role.ADMIN)
                             .requestMatchers(HttpMethod.POST,
                                     String.format("%s/order-details", apiPrefix)).hasRole(Role.USER)
                             .requestMatchers(HttpMethod.PUT,
                                     String.format("%s/order-details/**", apiPrefix)).hasAnyRole(Role.USER, Role.ADMIN)
                             .requestMatchers(HttpMethod.DELETE,
                                     String.format("%s/order-details/**" , apiPrefix)).hasRole(Role.ADMIN)

                             .anyRequest().authenticated()
                     ;


                 });

         http.cors(new Customizer<CorsConfigurer<HttpSecurity>>() {
             @Override
             public void customize(CorsConfigurer<HttpSecurity> httpSecurityCorsConfigurer) {
                 CorsConfiguration configuration = new CorsConfiguration();
                 configuration.setAllowedOrigins(List.of(("*")));
                 configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
                 configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
                 configuration.setExposedHeaders(List.of("x-auth-token"));

                 UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                 source.registerCorsConfiguration("/**", configuration);
                 httpSecurityCorsConfigurer.configurationSource(source);

             }
         });

         return http.build();
    }
}
