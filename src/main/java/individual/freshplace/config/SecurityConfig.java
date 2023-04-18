package individual.freshplace.config;

import individual.freshplace.util.auth.jwt.*;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

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
                .antMatchers("/static/**")
                .antMatchers("/**")
                .antMatchers("/manifest.json")
                .antMatchers("/favicon.ico");
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
                .mvcMatchers(HttpMethod.POST, "/api/login").permitAll()
                .mvcMatchers(HttpMethod.PATCH, "/api/reissue").permitAll()
                .mvcMatchers(HttpMethod.DELETE, "/api/members/logout").permitAll()

                .mvcMatchers(HttpMethod.GET, "/api/public/**").permitAll()
                .mvcMatchers(HttpMethod.POST, "/api/public/**").permitAll()
                .mvcMatchers(HttpMethod.DELETE, "/api/public/**").permitAll()

                .mvcMatchers(HttpMethod.GET, "/").permitAll()

                .mvcMatchers(HttpMethod.GET, "/api/image/**").permitAll()

                .mvcMatchers(HttpMethod.GET, "/api/admin/**").hasAuthority(List.of(Authority.ADMIN.name()).toString())
                .mvcMatchers(HttpMethod.POST, "/api/admin/**").hasAuthority(List.of(Authority.ADMIN.name()).toString())
                .mvcMatchers(HttpMethod.PUT, "/api/admin/**").hasAuthority(List.of(Authority.ADMIN.name()).toString())

                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new JwtFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler);
    }
}
