package individual.freshplace.dto.auth.token;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class TokenReissueRequest {

    @NotBlank
    private String accessToken;

    @NotBlank
    private String refreshToken;
}
