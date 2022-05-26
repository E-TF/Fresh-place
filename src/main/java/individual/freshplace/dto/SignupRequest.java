package individual.freshplace.dto;

import individual.freshplace.entity.DiscountByGrade;
import individual.freshplace.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
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

    public SignupRequest toServiceDto(String password) {
        return new SignupRequest(this.memberId, password, this.memberName, this.phoneNumber, this.email, this.memberBirth);
    }

    public Member toEntity(DiscountByGrade grade) {
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