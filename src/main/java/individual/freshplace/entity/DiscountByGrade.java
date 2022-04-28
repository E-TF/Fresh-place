package individual.freshplace.entity;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
public class DiscountByGrade {

    @Id
    private String gradeCode;

    private String gradeName;

    private long discountRate;
}
