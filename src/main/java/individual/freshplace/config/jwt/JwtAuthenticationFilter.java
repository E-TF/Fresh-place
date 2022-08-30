package individual.freshplace.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import individual.freshplace.config.JwtProperties;
import individual.freshplace.config.auth.PrincipalDetails;
import individual.freshplace.dto.login.LoginRequest;
import individual.freshplace.util.constant.ErrorCode;
import individual.freshplace.util.ReadableRequestBodyWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.http.MediaType;
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
import java.time.LocalDateTime;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtProperties jwtProperties;
    private final ObjectMapper objectMapper;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        LoginRequest loginDto = null;
        try {
            ReadableRequestBodyWrapper readableRequestBodyWrapper = new ReadableRequestBodyWrapper(request);
            loginDto = objectMapper.readValue(readableRequestBodyWrapper.getInputStream(), LoginRequest.class);
        } catch (IOException e) {
            log.error("로그인 요청 값 분석 실패");
        }

        return getLoginAuthentication(loginDto);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws ServletException, IOException {

        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        log.info("로그인 한 사용자는 " + principalDetails.getUsername() + "입니다.");

        String jwtToken = createToken(principalDetails);
        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + jwtToken);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {

        ErrorCode failedLogin = ErrorCode.FAILED_LOGIN;

        response.setStatus(failedLogin.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("date", LocalDateTime.now());
        jsonObject.put("status", failedLogin.getHttpStatus().value());
        jsonObject.put("error", failedLogin.getHttpStatus().name());
        jsonObject.put("code", failedLogin.name());
        jsonObject.put("message", failedLogin.getMessage());

        response.getWriter().println(jsonObject);
    }

    private Authentication getLoginAuthentication(LoginRequest loginRequest) {

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginRequest.getMemberId(), loginRequest.getPassword());
        Authentication authentication = authenticationManager.authenticate(token);

        return authentication;
    }

    private String createToken(PrincipalDetails principalDetails) {

        return  JWT.create()
                .withSubject(principalDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME.toMillis()))
                .withClaim(jwtProperties.getClaim(), principalDetails.getUsername())
                .sign(Algorithm.HMAC512(jwtProperties.getSecret()));
    }
}
