package individual.freshplace.service;

import individual.freshplace.entity.DiscountByGrade;
import individual.freshplace.repository.DiscountByGradeRepository;
import individual.freshplace.util.constant.Cache;
import individual.freshplace.util.constant.ErrorCode;
import individual.freshplace.util.exception.WrongValueException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DiscountByGradeService {

    private final DiscountByGradeRepository discountByGradeRepository;

    @Transactional(readOnly = true)
    @Cacheable(value = Cache.GRADE, key = "#id")
    public DiscountByGrade findById(final String id) {
        return discountByGradeRepository.findById(id)
                .orElseThrow(() -> new WrongValueException(ErrorCode.BAD_CODE, id));
    }
}
