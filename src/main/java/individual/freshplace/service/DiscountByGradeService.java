package individual.freshplace.service;

import individual.freshplace.entity.DiscountByGrade;
import individual.freshplace.repository.DiscountByGradeRepository;
import individual.freshplace.util.ErrorCode;
import individual.freshplace.util.exception.WrongValueException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DiscountByGradeService {

    private final DiscountByGradeRepository discountByGradeRepository;

    public DiscountByGrade findById(final String id) {
        return discountByGradeRepository.findById(id)
                .orElseThrow(() -> new WrongValueException(ErrorCode.BAD_CODE, id));
    }
}
