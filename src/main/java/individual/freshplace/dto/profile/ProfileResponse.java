package individual.freshplace.dto.profile;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import individual.freshplace.entity.Member;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;

@Getter
public class ProfileResponse {

    private final String memberId;

    private final String memberName;

    private final String phoneNumber;

    private final String email;

    @CreatedDate
    private final LocalDate memberBirth;

    private final String membership;

    private ProfileResponse(@JsonProperty("memberId") String memberId, @JsonProperty("memberName") String memberName,
                            @JsonProperty("phoneNumber") String phoneNumber, @JsonProperty("email") String email,
                            @JsonSerialize(using = LocalDateSerializer.class) @JsonDeserialize(using = LocalDateDeserializer.class)
                            @JsonProperty("memberBirth") LocalDate memberBirth, @JsonProperty("membership") String membership) {

        this.memberId = memberId;
        this.memberName = memberName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.memberBirth = memberBirth;
        this.membership = membership;
    }

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
