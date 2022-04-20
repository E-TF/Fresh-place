package individual.freshplace.controller;

import individual.freshplace.entity.DiscountByGrade;
import individual.freshplace.entity.Member;
import individual.freshplace.repository.DiscountByGradeRepository;
import individual.freshplace.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RestApiController {

    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private MemberRepository memberRepository;
    private DiscountByGradeRepository discountByGradeRepository;

    @PostMapping("/join")
    public String join(@RequestBody Member member) {

        DiscountByGrade gradeCode = discountByGradeRepository.getById("01");

        member.setPassword(bCryptPasswordEncoder.encode(member.getPassword()));
        member.setGradeCode(gradeCode);
        memberRepository.save(member);
        return "회원가입완료";
    }

    @GetMapping("/member")
    public String list() {

        System.out.println("리스트");
        return "<h1>list</h1>";
    }


}
