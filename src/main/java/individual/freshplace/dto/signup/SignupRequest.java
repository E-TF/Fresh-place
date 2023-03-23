package individual.freshplace.dto.signup;

import individual.freshplace.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {

    @NotBlank
    @Length(min = 7, max = 12, message = "아이디는 7글자 이상 12글자 이하 입니다.")
    private String memberId;

    @NotBlank
    private String password;

    @NotBlank
    @Length(min = 2, max = 5, message = "이름은 2글자 이상 5글자 이하 입니다.")
    private String memberName;

    @NotBlank
    @Pattern(regexp = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$", message = "01*-****-**** 형식을 지켜주세요.")
    private String phoneNumber;

    @Email(message = "이메일 형식을 지켜주세요.")
    @NotBlank(message = "공백일 수 없습니다.")
    private String email;

    @Past(message = "현재 날짜 이전으로 입력해주세요.")
    @NotNull(message = "공백일 수 없습니다.")
    private LocalDate memberBirth;

    public Member toMember(final String password) {
        return Member.builder()
                .memberId(memberId)
                .password(password)
                .memberName(memberName)
                .phoneNumber(phoneNumber)
                .email(email)
                .memberBirth(memberBirth)
                .build();
    }
}
