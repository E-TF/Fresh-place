package individual.freshplace.service;

import individual.freshplace.dto.signup.SignupRequest;
import individual.freshplace.entity.Member;
import individual.freshplace.repository.MemberRepository;
import individual.freshplace.util.constant.ErrorCode;
import individual.freshplace.util.constant.LockPrefix;
import individual.freshplace.util.exception.DuplicationException;
import individual.freshplace.util.lock.UserLevelLock;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SignupService {

    private final UserLevelLock userLevelLock;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void signup(final SignupRequest signupRequest) {

        userLevelLock.lockProcess(LockPrefix.SIGNUP.getMethodName() + signupRequest.getMemberId(), () -> {
            signupInner(signupRequest);
        });
    }

    @Transactional
    protected void signupInner(final SignupRequest signupRequest) {

        if (memberRepository.existsByMemberId(signupRequest.getMemberId())) {
            throw new DuplicationException(ErrorCode.ID_DUPLICATE_PREVENTION, signupRequest.getMemberId());
        }

        final Member member = signupRequest.toMember(passwordEncoder.encode(signupRequest.getPassword()));
        memberRepository.save(member);
    }

}
