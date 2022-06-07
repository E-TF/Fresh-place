package individual.freshplace.dto.profile;

import individual.freshplace.entity.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProfileResponse {

    private String memberId;

    private String memberName;

    private String phoneNumber;

    private String email;

    private LocalDate memberBirth;

    private String gradeName;

    public ProfileResponse(Member member) {
        this.memberId = member.getMemberId();
        this.memberName = member.getMemberName();
        this.phoneNumber = member.getPhoneNumber();
        this.email = member.getEmail();
        this.memberBirth = member.getMemberBirth();
        this.gradeName = member.getGradeCode().getGradeName();
    }
}