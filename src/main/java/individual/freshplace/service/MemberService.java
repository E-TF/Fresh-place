package individual.freshplace.service;

import individual.freshplace.dto.profile.ProfileUpdateRequest;
import individual.freshplace.dto.profile.ProfileResponse;
import individual.freshplace.dto.signup.SignupRequest;
import individual.freshplace.entity.DiscountByGrade;
import individual.freshplace.entity.Member;
import individual.freshplace.repository.DiscountByGradeRepository;
import individual.freshplace.repository.MemberRepository;
import individual.freshplace.util.ErrorCode;
import individual.freshplace.util.constant.GradeCode;
import individual.freshplace.util.exception.DuplicationException;
import individual.freshplace.util.exception.WrongValueException;
import individual.freshplace.util.lock.UserLevelLock;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final UserLevelLock userLevelLock;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final DiscountByGradeRepository discountByGradeRepository;
    private final ObjectProvider<MemberService> memberServiceObjectProvider;

    public void signup(SignupRequest signupRequest) {

        userLevelLock.lockProcess("signup" + signupRequest.getMemberId(), () -> {
            signupInner(signupRequest);
        });
    }

    @Transactional
    public void signupInner(SignupRequest signupRequest) {

        duplicateIdChecking(signupRequest.getMemberId());

        DiscountByGrade grade = discountByGradeRepository.findById(GradeCode.CODE_GRADE_GENERAL)
                .orElseThrow(() -> new WrongValueException(ErrorCode.BAD_CODE, GradeCode.CODE_GRADE_GENERAL));

        Member member = signupRequest.toMember(grade, passwordEncoder.encode(signupRequest.getPassword()));

        memberRepository.save(member);
    }

    @Transactional
    public ProfileResponse getProfile(String memberId) {

        Member member = findMember(memberId);

        return ProfileResponse.from(member);
    }

    public ProfileResponse updateMember(String memberId, ProfileUpdateRequest profileUpdateRequest) {

        return userLevelLock.lockProcess("updateMember" + profileUpdateRequest.getMemberId(), () ->
                ProfileResponse.from(memberServiceObjectProvider.getObject().updateMemberInner(memberId, profileUpdateRequest)));
    }

    @Transactional
    public Member updateMemberInner(String memberId, ProfileUpdateRequest profileUpdateRequest) {

        Member member = findMember(memberId);

        duplicateIdChecking(profileUpdateRequest.getMemberId());

        member.updateProfile(profileUpdateRequest);

        return member;
    }

    @Transactional
    public void withdrawal(String memberId) {

        if (!memberRepository.existsByMemberId(memberId)) {
            throw new WrongValueException(ErrorCode.USERNAME_NOT_FOUND, memberId);
        }

        memberRepository.deleteByMemberId(memberId);
    }

    private void duplicateIdChecking(String memberId) {

        if (memberRepository.existsByMemberId(memberId)) {
            throw new DuplicationException(ErrorCode.ID_DUPLICATE_PREVENTION, memberId);
        }
    }

    private Member findMember(String memberId) {
        return memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new WrongValueException(ErrorCode.USERNAME_NOT_FOUND, memberId));
    }
}
