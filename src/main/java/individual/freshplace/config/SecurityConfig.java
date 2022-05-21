package individual.freshplace.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import individual.freshplace.config.jwt.JwtAuthenticationEntryPoint;
import individual.freshplace.config.jwt.JwtAuthenticationFilter;
import individual.freshplace.config.jwt.JwtAuthorizationFilter;
import individual.freshplace.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtProperties jwtProperties;
    private final MemberRepository memberRepository;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final ObjectMapper objectMapper;

    @Value("${cors.alloworigin}")
    private String localOrigin;

    @Bean
    public static PropertySourcesPlaceholderConfigurer Properties() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        configurer.setLocations();
        return configurer;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);
        config.addAllowedOrigin(localOrigin);
        config.addAllowedMethod(String.valueOf(Arrays.asList(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.PATCH, HttpMethod.OPTIONS)));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/resources/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .httpBasic().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .cors().configurationSource(corsConfigurationSource())


                .and()
                .authorizeRequests()
                .mvcMatchers(HttpMethod.POST, "/public/signup").permitAll()
                .anyRequest().authenticated()

                .and()
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), jwtProperties, objectMapper))
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), memberRepository, jwtProperties))
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint);
    }
}