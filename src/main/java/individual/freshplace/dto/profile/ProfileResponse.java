package individual.freshplace.dto.profile;

import individual.freshplace.entity.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ProfileResponse {

    private String memberId;

    private String memberName;

    private String phoneNumber;

    private String email;

    private LocalDate memberBirth;

    private String gradeName;

    public static ProfileResponse from(final Member member) {
        return new ProfileResponse(
                member.getMemberId(),
                member.getMemberName(),
                member.getPhoneNumber(),
                member.getEmail(),
                member.getMemberBirth(),
                member.getGradeCode().getGradeName());
    }
}