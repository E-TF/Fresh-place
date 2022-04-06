package individual.freshplace.entity;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
public class DiscountByGrade {

    @Id
    @Column(name = "grade_code")
    private String id;

    @Column(name = "grade_name")
    private String name;

    private Long discountRate;
}
