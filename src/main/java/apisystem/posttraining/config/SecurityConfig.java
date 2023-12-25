package apisystem.posttraining.config;

import apisystem.posttraining.exception.AuthEntryPointJwt;
import apisystem.posttraining.exception.CustomAccessDeniedHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final AuthenticationProvider authenticationProvider;
    @Autowired
    AuthEntryPointJwt authEntryPointJwt;
    @Autowired
    CustomAccessDeniedHandler customAccessDeniedHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private static final String[] WHITE_LIST_URL = {"/api/v1/auth/**",
            "api/test/**",
            "/api/commons/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html"};


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception.authenticationEntryPoint(authEntryPointJwt))
                .exceptionHandling(exception -> exception.accessDeniedHandler(customAccessDeniedHandler))
                .authorizeHttpRequests(req ->
                        req.requestMatchers(WHITE_LIST_URL)
                                .permitAll()
                                .requestMatchers("api/commons-roles/**").hasAnyAuthority("student","AcademicAdvisor","lecturer","TQA","StudentAffairs")
                                .requestMatchers("api/behaviors/**").hasAnyAuthority("AcademicAdvisor","lecturer","StudentAffairs")
                                .requestMatchers("api/scores/**").hasAnyAuthority("AcademicAdvisor","lecturer","TQA")
                                .requestMatchers("api/exam-plan/**").hasAnyAuthority("AcademicAdvisor","lecturer","TQA")
                                .requestMatchers("api/exam-scheduling/**").hasAnyAuthority("AcademicAdvisor","lecturer","TQA","student")
                                .requestMatchers("api/class-credit/**").hasAnyAuthority("AcademicAdvisor","lecturer","TQA")
                                .requestMatchers("api/reward/**").hasAnyAuthority("StudentAffairs","student","TQA")
                                .anyRequest()
                                .authenticated()
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
//                .logout(logout ->
//                        logout.logoutUrl("/api/v1/auth/logout")
//                                .addLogoutHandler(logoutHandler)
//                                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
//                )
        ;

        return http.build();
    }

        private CorsConfigurationSource corsConfigurationSource() {
            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            var corsConfiguration = new CorsConfiguration();
            corsConfiguration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
            corsConfiguration.setAllowedOrigins(List.of("http://localhost:3000"));
            corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PUT","OPTIONS","PATCH", "DELETE"));
            corsConfiguration.setAllowCredentials(true);
            corsConfiguration.setExposedHeaders(List.of("Authorization"));
            source.registerCorsConfiguration("/**", corsConfiguration);
            return source;
        }

        @Bean
        public FilterRegistrationBean<CorsFilter> corsFilterRegistrationBean() {
            FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(
                    new CorsFilter(corsConfigurationSource()));
            bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
            return bean;
        }

}
