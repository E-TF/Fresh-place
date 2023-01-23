package individual.freshplace.dto.profile;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import individual.freshplace.entity.Member;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class ProfileResponse {

    private final String memberId;

    private final String memberName;

    private final String phoneNumber;

    private final String email;

    @CreatedDate
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private final LocalDate memberBirth;

    private final String membership;

    public static ProfileResponse from(final Member member) {
        return new ProfileResponse(
                member.getMemberId(),
                member.getMemberName(),
                member.getPhoneNumber(),
                member.getEmail(),
                member.getMemberBirth(),
                member.getGradeCode().getCodeName());
    }

}
