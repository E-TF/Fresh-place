package individual.freshplace.service;

import individual.freshplace.dto.signup.SignupRequest;
import individual.freshplace.entity.DiscountByGrade;
import individual.freshplace.entity.Member;
import individual.freshplace.util.constant.ErrorCode;
import individual.freshplace.util.constant.GradeCode;
import individual.freshplace.util.constant.LockPrefix;
import individual.freshplace.util.exception.DuplicationException;
import individual.freshplace.util.lock.UserLevelLock;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FSignupService {

    private final UserLevelLock userLevelLock;
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final DiscountByGradeService discountByGradeService;

    public void signup(final SignupRequest signupRequest) {

        userLevelLock.lockProcess(LockPrefix.SIGNUP.getMethodName() + signupRequest.getMemberId(), () -> {
            signupInner(signupRequest);
        });
    }

    @Transactional
    protected void signupInner(final SignupRequest signupRequest) {

        if (memberService.existsByMemberId(signupRequest.getMemberId())) {
            throw new DuplicationException(ErrorCode.ID_DUPLICATE_PREVENTION, signupRequest.getMemberId());
        }

        DiscountByGrade discountByGrade = discountByGradeService.findById(GradeCode.GENERAL.getCodeValue());

        Member member = signupRequest.toMember(discountByGrade, passwordEncoder.encode(signupRequest.getPassword()));

        memberService.save(member);
    }

}
