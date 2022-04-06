package individual.freshplace.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter @Setter
public class Member extends BaseEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_seq")
    private Long id;

    @Column(name = "member_id")
    private String loginId;

    private String password;

    @Column(name = "member_name")
    private String name;

    private String phNum;

    private String email;

    @Column(name = "member_birth")
    @Temporal(TemporalType.DATE)
    private Date birth;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grade_code")
    private DiscountByGrade grade;

    @OneToMany(mappedBy = "member")
    private List<DeliverAddress> addressList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Order> orderList = new ArrayList<>();

}
