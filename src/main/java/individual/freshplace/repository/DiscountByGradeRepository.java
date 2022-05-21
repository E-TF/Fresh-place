package individual.freshplace.repository;

import individual.freshplace.entity.DiscountByGrade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountByGradeRepository extends JpaRepository<DiscountByGrade, String> {
}
