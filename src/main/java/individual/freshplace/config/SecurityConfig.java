package individual.freshplace.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import individual.freshplace.config.auth.PrincipalDetailsService;
import individual.freshplace.config.jwt.JwtAuthenticationEntryPoint;
import individual.freshplace.config.jwt.JwtAuthenticationFilter;
import individual.freshplace.config.jwt.JwtAuthorizationFilter;
import individual.freshplace.config.jwt.JwtProperties;
import individual.freshplace.util.constant.Authority;
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
    private final PrincipalDetailsService principalDetailsService;
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
    public void configure(WebSecurity web) {
        web
                .ignoring()
                .antMatchers("/resources/**")
                .antMatchers("/image.html")
                .antMatchers("/imageOrigin.html")
                .antMatchers("/Avif.html");
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
                .mvcMatchers(HttpMethod.GET, "/public/**").permitAll()
                .mvcMatchers(HttpMethod.POST, "/public/**").permitAll()
                .mvcMatchers(HttpMethod.DELETE, "/public/**").permitAll()

                .mvcMatchers(HttpMethod.GET, "/**").permitAll()

                .mvcMatchers(HttpMethod.GET, "/image/**").permitAll()

                .mvcMatchers(HttpMethod.POST, "/admin/**").hasAuthority(Authority.ADMIN.name())
                .mvcMatchers(HttpMethod.PUT, "/admin/**").hasAuthority(Authority.ADMIN.name())
                .mvcMatchers(HttpMethod.GET, "/admin/**").hasAuthority(Authority.ADMIN.name())

                .anyRequest().authenticated()

                .and()
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), jwtProperties, objectMapper))
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), jwtProperties, principalDetailsService))
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint);
    }
}
