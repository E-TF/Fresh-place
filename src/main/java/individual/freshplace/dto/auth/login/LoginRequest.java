package individual.freshplace.dto.auth.login;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class LoginRequest {

    @NotBlank
    private String memberId;

    @NotBlank
    private String password;
}
