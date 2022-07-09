package individual.freshplace.dto.profile;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ProfileUpdateRequest {

    @NotBlank
    @Length(min = 7, message = "아이디는 7글자 이상입니다.")
    private String memberId;

    @NotBlank
    @Length(min = 2, message = "이름은 2글자 이상입니다.")
    private String memberName;

    private String phoneNumber;

    @Email(message = "이메일 형식을 지켜주세요.")
    @NotBlank
    private String email;

    @Past
    @NotNull
    private LocalDate memberBirth;
}
