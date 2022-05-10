package individual.freshplace.controller;

import individual.freshplace.entity.DiscountByGrade;
import individual.freshplace.entity.Member;
import individual.freshplace.repository.DiscountByGradeRepository;
import individual.freshplace.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JwtAuthenticationController {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MemberRepository memberRepository;
    private final DiscountByGradeRepository discountByGradeRepository;

    @PostMapping("/public/join")
    public String join(@RequestBody Member member) {

        DiscountByGrade gradeCode = discountByGradeRepository.findByGradeCode("01");

        member.setPassword(bCryptPasswordEncoder.encode(member.getPassword()));
        member.setGradeCode(gradeCode);
        memberRepository.save(member);
        return "회원가입완료";
    }
}