package individual.freshplace.dto.signup;

import individual.freshplace.entity.DiscountByGrade;
import individual.freshplace.entity.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class SignupRequest {

    @NotBlank
    @Length(min = 7, message = "아이디는 7글자 이상입니다.")
    private String memberId;

    @NotBlank
    private String password;

    @NotBlank
    @Length(min = 2, message = "이름은 2글자 이상입니다.")
    private String memberName;

    @NotBlank
    @Pattern(regexp = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$", message = "01*-****-**** 형식을 지켜주세요.")
    private String phoneNumber;

    @Email(message = "이메일 형식을 지켜주세요.")
    @NotBlank
    private String email;

    @Past
    @NotNull
    private LocalDate memberBirth;

    public Member toMember(final DiscountByGrade grade, final String password) {
        return Member.builder()
                .memberId(memberId)
                .password(password)
                .memberName(memberName)
                .phoneNumber(phoneNumber)
                .email(email)
                .memberBirth(memberBirth)
                .gradeCode(grade)
                .build();
    }
}
