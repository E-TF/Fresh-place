package individual.freshplace.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
public class SignUpDto {

    @NotEmpty
    private String memberId;

    @NotEmpty
    private String password;

    @NotEmpty
    private String memberName;

    @NotEmpty
    private String phNum;

    @Email
    private String email;

    @Past
    private LocalDate memberBirth;
}
