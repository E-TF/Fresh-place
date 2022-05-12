package individual.freshplace.service;

import individual.freshplace.dto.SignUpDto;
import individual.freshplace.entity.DiscountByGrade;
import individual.freshplace.entity.Member;
import individual.freshplace.repository.DiscountByGradeRepository;
import individual.freshplace.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final DiscountByGradeRepository discountByGradeRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public void signup(SignUpDto signUpDto) {
        if (memberRepository.findByMemberId(signUpDto.getMemberId()).orElse(null) != null) {
            throw new NullPointerException("이미 가입된 회원입니다.");
        }

        DiscountByGrade grade = discountByGradeRepository.getById("01");

        Member member = Member.builder()
                .memberId(signUpDto.getMemberId())
                .password(bCryptPasswordEncoder.encode(signUpDto.getPassword()))
                .memberName(signUpDto.getMemberName())
                .phNum(signUpDto.getPhNum())
                .email(signUpDto.getEmail())
                .memberBirth(signUpDto.getMemberBirth())
                .gradeCode(grade)
                .build();

        memberRepository.save(member);

    }

}
