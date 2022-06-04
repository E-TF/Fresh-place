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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final DiscountByGradeRepository discountByGradeRepository;

    @Transactional
    public void saveMember(SignupRequest signupRequest) {

        duplicateIdChecking(signupRequest.getMemberId());

        DiscountByGrade grade = discountByGradeRepository.findById(GradeCode.CODE_GRADE_GENERAL)
                .orElseThrow(() -> new WrongValueException(ErrorCode.BAD_CODE, GradeCode.CODE_GRADE_GENERAL));

        Member member = signupRequest.toMember(grade);

        memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public ProfileResponse findMember(String memberId) {

        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new WrongValueException(ErrorCode.USERNAME_NOT_FOUND, memberId));

        return new ProfileResponse(member);
    }

    @Transactional
    public ProfileResponse updateMember(String memberId, ProfileUpdateRequest profileUpdateRequest) {

        Member member = memberRepository.findByMemberId(memberId).orElseThrow(() -> new WrongValueException(ErrorCode.USERNAME_NOT_FOUND, memberId));

        duplicateIdChecking(profileUpdateRequest.getMemberId());

        member.updateProfile(
                profileUpdateRequest.getMemberId(),
                profileUpdateRequest.getMemberName(),
                profileUpdateRequest.getPhoneNumber(),
                profileUpdateRequest.getEmail(),
                profileUpdateRequest.getMemberBirth());

        return new ProfileResponse(member);
    }

    @Transactional
    public void deleteMember(String memberId) {

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
}