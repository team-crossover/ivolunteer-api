package com.crossover.ivolunteer.security;

import com.crossover.ivolunteer.business.enums.TipoUsuarioEnum;
import com.crossover.ivolunteer.business.service.SessaoService;
import com.crossover.ivolunteer.business.service.UsuarioService;
import com.crossover.ivolunteer.presentation.constants.ApiPaths;
import com.crossover.ivolunteer.presentation.dto.RespostaSimplesDto;
import com.crossover.ivolunteer.security.jwt.JWTAuthenticationCheckingFilter;
import com.crossover.ivolunteer.security.jwt.JWTAuthenticationProcessingFilter;
import com.crossover.ivolunteer.security.jwt.JWTHttpService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@Log4j2
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private SessaoService sessaoService;

    @Autowired
    private JWTHttpService jwtHttpService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // TODO: Use CSRF when we start saving JWT on cookies
                .csrf().disable()
                .cors().and()
                .authorizeRequests()

                // Allows the home page (and static resources) for everyone
                .antMatchers("/").permitAll()
                .antMatchers("/index.html").permitAll()
                .antMatchers("/favicon.ico").permitAll()
                .antMatchers("/static/**").permitAll()

                // Allows our API endpoints based on roles
                .antMatchers(ApiPaths.V1.ADMIN_PREFIX + "/**").hasRole(TipoUsuarioEnum.ADMIN.name())
                .antMatchers(ApiPaths.V1.ONG_PREFIX + "/**").hasRole(TipoUsuarioEnum.ONG.name())
                .antMatchers(ApiPaths.V1.VOLUNTARIO_PREFIX + "/**").hasRole(TipoUsuarioEnum.VOLUNTARIO.name())
                .antMatchers(ApiPaths.V1.PUBLIC_PREFIX + "/**").permitAll()

                // Everything else requires admin
                .anyRequest().hasRole(TipoUsuarioEnum.ADMIN.name())

                // Add the authentication filters.
                .and()
                .addFilterBefore(
                        authenticationProcessingFilter(),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(
                        authenticationCheckingFilter(),
                        UsernamePasswordAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // NOTE: Deauthentication is handled by the AuthenticationController.

                .and()
                .exceptionHandling()
                .accessDeniedHandler(this::accessDeniedHandler);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSources() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.applyPermitDefaultValues();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "authorization", "Content-Type", "content-type", "x-requested-with", "Access-Control-Allow-Origin", "Access-Control-Allow-Headers", "x-auth-token", "x-app-id", "Origin", "Accept", "X-Requested-With", "Access-Control-Request-Method", "Access-Control-Request-Headers"));
        configuration.setExposedHeaders(Arrays.asList("Authorization", "authorization", "Content-Type", "content-type", "x-requested-with", "Access-Control-Allow-Origin", "Access-Control-Allow-Headers", "x-auth-token", "x-app-id", "Origin", "Accept", "X-Requested-With", "Access-Control-Request-Method", "Access-Control-Request-Headers"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder authBuilder) throws Exception {
        authBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }

    private JWTAuthenticationProcessingFilter authenticationProcessingFilter() throws Exception {
        AntPathRequestMatcher loginRequestMatcher = new AntPathRequestMatcher(
                ApiPaths.V1.AUTH_AUTHENTICATE, "POST");
        JWTAuthenticationProcessingFilter filter = new JWTAuthenticationProcessingFilter(
                loginRequestMatcher,
                authenticationManager(),
                jwtHttpService,
                usuarioService,
                sessaoService);
        filter.setRequiresAuthenticationRequestMatcher(loginRequestMatcher);
        filter.setAuthenticationManager(authenticationManagerBean());
        filter.setAuthenticationSuccessHandler(this::authenticationSuccessHandler);
        filter.setAuthenticationFailureHandler(this::authenticationFailureHandler);
        return filter;
    }

    private JWTAuthenticationCheckingFilter authenticationCheckingFilter() throws Exception {
        return new JWTAuthenticationCheckingFilter(
                authenticationManager(),
                jwtHttpService,
                sessaoService);
    }

    private void authenticationSuccessHandler(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException {

        String path = request.getServletPath();
        RespostaSimplesDto result = new RespostaSimplesDto(
                HttpStatus.OK, path);
        response.setStatus(HttpStatus.OK.value());
        objectMapper.writeValue(response.getWriter(), result);
    }

    private void authenticationFailureHandler(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException e) throws IOException {

        String path = request.getServletPath();
        RespostaSimplesDto result = new RespostaSimplesDto(
                HttpStatus.UNAUTHORIZED, path, e.getMessage());
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        objectMapper.writeValue(response.getWriter(), result);
    }

    private void accessDeniedHandler(HttpServletRequest request,
                                     HttpServletResponse response,
                                     AccessDeniedException e) throws IOException, ServletException {
        String path = request.getServletPath();
        RespostaSimplesDto result = new RespostaSimplesDto(HttpStatus.FORBIDDEN, path);
        response.setStatus(HttpStatus.FORBIDDEN.value());
        objectMapper.writeValue(response.getWriter(), result);
    }

}
