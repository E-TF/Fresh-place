package individual.freshplace.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class LoginRequest {

    @NotBlank
    private String memberId;

    @NotBlank
    private String password;
}