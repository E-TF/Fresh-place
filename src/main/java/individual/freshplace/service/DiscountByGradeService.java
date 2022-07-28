package individual.freshplace.service;

import individual.freshplace.entity.DiscountByGrade;
import individual.freshplace.repository.DiscountByGradeRepository;
import individual.freshplace.util.ErrorCode;
import individual.freshplace.util.exception.WrongValueException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiscountByGradeService {

    private final DiscountByGradeRepository discountByGradeRepository;

    @Transactional(readOnly = true)
    @Cacheable(value = "grade", key = "#id")
    public DiscountByGrade findById(final String id) {
        log.info("grade 캐시가 없는 경우");
        return discountByGradeRepository.findById(id)
                .orElseThrow(() -> new WrongValueException(ErrorCode.BAD_CODE, id));
    }
}
