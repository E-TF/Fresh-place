package individual.freshplace.service;

import individual.freshplace.dto.SignupRequest;
import individual.freshplace.entity.DiscountByGrade;
import individual.freshplace.entity.Member;
import individual.freshplace.repository.DiscountByGradeRepository;
import individual.freshplace.repository.MemberRepository;
import individual.freshplace.util.ErrorCode;
import individual.freshplace.util.constant.GradeCode;
import individual.freshplace.util.exception.DuplicationException;
import individual.freshplace.util.exception.WrongValueException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final DiscountByGradeRepository discountByGradeRepository;
    private final static WrongValueException wrongValueException = new WrongValueException(ErrorCode.BAD_CODE, GradeCode.CODE_GRADE_GENERAL);

    @Transactional
    public void signup(SignupRequest signupRequest) {

        if (!duplicateChecking(signupRequest)) {
            throw new DuplicationException(ErrorCode.ID_DUPLICATE_PREVENTION, signupRequest.getMemberId());
        }

        DiscountByGrade grade = discountByGradeRepository.findById(GradeCode.CODE_GRADE_GENERAL)
                .orElseThrow(() -> wrongValueException);

        Member member = signupRequest.toEntity(grade);

        memberRepository.save(member);
    }

    private boolean duplicateChecking(SignupRequest signupRequest) {

        if (memberRepository.existsByMemberId(signupRequest.getMemberId())) {
            return false;
        }

        return true;
    }
}