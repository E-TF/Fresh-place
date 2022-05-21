package individual.freshplace.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Getter
@Setter
public class SignupRequest {

    @NotBlank
    private String memberId;

    @NotBlank
    private String password;

    @NotBlank
    private String memberName;

    @NotBlank
    private String phoneNumber;

    @Email
    private String email;

    @Past
    private LocalDate memberBirth;
}
