package individual.freshplace.entity;

import individual.freshplace.dto.profile.ProfileUpdateRequest;
import individual.freshplace.util.constant.Authority;
import individual.freshplace.util.constant.Membership;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberSeq;

    private String memberId;

    private String password;

    private String memberName;

    private String phoneNumber;

    private String email;

    private LocalDate memberBirth;

    private Membership gradeCode;

    @Enumerated(EnumType.STRING)
    private Authority role;

    @OneToMany(mappedBy = "member")
    private List<DeliverAddress> addressList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Order> orderList = new ArrayList<>();

    @Builder
    public Member(String memberId, String password, String memberName, String phoneNumber, String email, LocalDate memberBirth, Membership gradeCode) {
        this.memberId = memberId;
        this.password = password;
        this.memberName = memberName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.memberBirth = memberBirth;
        this.gradeCode = gradeCode;
        this.role = Authority.USER;
    }

    public void updateProfile(final ProfileUpdateRequest profileUpdateRequest) {
        this.memberId = profileUpdateRequest.getMemberId();
        this.memberName = profileUpdateRequest.getMemberName();
        this.phoneNumber = profileUpdateRequest.getPhoneNumber();
        this.email = profileUpdateRequest.getEmail();
        this.memberBirth = profileUpdateRequest.getMemberBirth();
    }
}
