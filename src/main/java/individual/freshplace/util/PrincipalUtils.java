package individual.freshplace.util;

import individual.freshplace.config.auth.PrincipalDetails;
import individual.freshplace.util.constant.ErrorCode;
import individual.freshplace.util.exception.CustomAuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

public class PrincipalUtils {

    public static String getUsername() {
        return getPrincipalDetails().getUsername();
    }

    public static String getPassword() {
        return getPrincipalDetails().getPassword();
    }

    private static PrincipalDetails getPrincipalDetails() {
        try {
            return (PrincipalDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (ClassCastException e) {
            throw new CustomAuthenticationException(ErrorCode.INVALID_TOKEN);
        }
    }
}
