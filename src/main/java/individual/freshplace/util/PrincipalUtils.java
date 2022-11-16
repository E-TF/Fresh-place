package individual.freshplace.util;

import individual.freshplace.config.auth.PrincipalDetails;
import org.springframework.security.core.context.SecurityContextHolder;

public class PrincipalUtils {

    public static String getUsername() {
        return getPrincipalDetails().getUsername();
    }

    public static String getPassword() {
        return getPrincipalDetails().getPassword();
    }

    private static PrincipalDetails getPrincipalDetails() {
        return (PrincipalDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
