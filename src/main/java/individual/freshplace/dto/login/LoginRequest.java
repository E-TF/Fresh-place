package individual.freshplace.dto.login;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
public class LoginRequest {

    @NotBlank
    private String memberId;

    @NotBlank
    private String password;
}
