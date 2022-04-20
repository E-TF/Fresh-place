package individual.freshplace.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import individual.freshplace.config.auth.PrincipalDetails;
import individual.freshplace.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

//스프링 시큐리티에서 UsernamePasswordAuthenticationFilter 가 있음.
//login 요청해서 username, password 전송하면 (post)
//UsernamePasswordAuthenticationFilter 동작 함.
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("JwtAuthenticationFilter: 로그인 시도중");

        //1. id와 pw를 받아서
        try {

            ObjectMapper om = new ObjectMapper();
            Member member = om.readValue(request.getInputStream(), Member.class);
            System.out.println(member.getMemberId());
            System.out.println(member.getPassword());

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(member.getMemberId(), member.getPassword());    //id만 받고 pw는 알아서 처리함.

            System.out.println("토큰 생성 완료");

            //PrincipalDetailsService 의 loadUserByUsername() 함수가 실행됨.
            //DB에 있는 id, pw가 일치한다.
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            System.out.println("아이디 검증 완료");

            // authentication 객체가 session 영역에 저장됨. => 로그인이 되었다는 뜻.
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            System.out.println(principalDetails.getUsername());

            //authentication 객체가 session 영역 에 저장을 해야 하고 그 방법이 return 해주면 됨.
            //리턴의 이유는 권한 관리를 security 가 대신 해주기 때문에 편리하려고 하는거임.
            //굳이 JWT 토큰을 사용하면서 세션을 만들 이유가 없음. 근데 단지 권한 처리때문에 session 넣어 줍니다.

            System.out.println("로그인 완료");

            return authentication;

        } catch (IOException e) {
            e.printStackTrace();
        }

        //2. 정상인지 로그인 시도를 해보는 거에요. authenticationManager 로 로그인 시도를 하면
        // PrincipleDetailsService 가 호출 loadUserByUsername() 함수 실행됨.

        //3. PrincipalDetails 를 세션에 담고 (권한 관리를 위해서,필요없으면 안해도 됨)
        return null;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        System.out.println("successfulAuthentication 실행됨:인증이 완료되었다는 뜻임");
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        //토큰 생성해서 response하면 응답 헤더에 Authorization 이 생기고 그 value에 토큰값이 들어감.
        String jwtToken = JWT.create()
                .withSubject("cos토큰")
                .withExpiresAt(new Date(System.currentTimeMillis() + (60000 * 10)))
                .withClaim("id", principalDetails.getMember().getMemberId())
                .withClaim("name", principalDetails.getMember().getMemberName())
                .sign(Algorithm.HMAC512("cos"));

        response.addHeader("Authorization", "Bearer" + jwtToken);

    }
}
