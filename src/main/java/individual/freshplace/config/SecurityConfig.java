package individual.freshplace.config;

import individual.freshplace.config.jwt.JwtAuthorizationFilter;
import individual.freshplace.filter.MyFilter3;
import individual.freshplace.config.jwt.JwtAuthenticationFilter;
import individual.freshplace.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

//@Configuration
//@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CorsFilter corsFilter;
    private final MemberRepository memberRepository;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.addFilterBefore(new MyFilter3(), BasicAuthenticationFilter.class);

        http.
                csrf().
                disable();

        http.
                sessionManagement().
                sessionCreationPolicy(SessionCreationPolicy.STATELESS)    //세션 사용 하지 않는다.

                .and()
                .addFilter(corsFilter)
                .formLogin().disable()  //폼로그인 안함
                .httpBasic().disable()  //기본적인 http 로그인 안함
                .authorizeRequests()
                .antMatchers("/member/**").authenticated()
                .anyRequest().permitAll();

        http.
                addFilter(new JwtAuthenticationFilter(authenticationManager())); //AuthenticationManager
//                .addFilter(new JwtAuthorizationFilter(authenticationManager(), memberRepository))   //AuthenticationManager
    }
}
