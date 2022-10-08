package individual.freshplace.dto.profile;

import individual.freshplace.entity.Member;
import lombok.*;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class ProfileResponse {

    private final String memberId;

    private final String memberName;

    private final String phoneNumber;

    private final String email;

    private final LocalDate memberBirth;

    private final String gradeName;

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
