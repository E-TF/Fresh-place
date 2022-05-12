package individual.freshplace.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberSeq;

    private String memberId;

    private String password;

    private String memberName;

    private String phNum;

    private String email;

    private LocalDate memberBirth;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grade_code")
    private DiscountByGrade gradeCode;

    @OneToMany(mappedBy = "member")
    private List<DeliverAddress> addressList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Order> orderList = new ArrayList<>();

    @Builder
    public Member(String memberId, String password, String memberName, String phNum, String email, LocalDate memberBirth, DiscountByGrade gradeCode) {
        this.memberId = memberId;
        this.password = password;
        this.memberName = memberName;
        this.phNum = phNum;
        this.email = email;
        this.memberBirth = memberBirth;
        this.gradeCode = gradeCode;
    }
}
