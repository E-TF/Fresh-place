package individual.freshplace.config.jwt;

import individual.freshplace.util.constant.ErrorCode;
import org.json.simple.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {

        String error = (String) request.getAttribute(JwtProperties.EXCEPTION);

        if (ErrorCode.EXPIRED_TOKEN.name().equals(error)) {
            responseError(response, ErrorCode.EXPIRED_TOKEN);
            return;
        }

        if (ErrorCode.INVALID_TOKEN.name().equals(error)) {
            responseError(response, ErrorCode.INVALID_TOKEN);
            return;
        }

        if (ErrorCode.NON_HEADER_AUTHORIZATION.name().equals(error)) {
            responseError(response, ErrorCode.NON_HEADER_AUTHORIZATION);
            return;
        }

        if (ErrorCode.RE_REQUEST_LOGIN.name().equals(error)) {
            responseError(response, ErrorCode.RE_REQUEST_LOGIN);
        }
    }

    private void responseError(HttpServletResponse response, ErrorCode errorCode) throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("date", LocalDateTime.now());
        jsonObject.put("status", errorCode.getHttpStatus().value());
        jsonObject.put("error", errorCode.getHttpStatus().name());
        jsonObject.put("code", errorCode.name());
        jsonObject.put("message", errorCode.getMessage());

        response.getWriter().println(jsonObject);
    }
}
