package individual.freshplace.controller;

import individual.freshplace.dto.SignUpDto;
import individual.freshplace.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/public/signup")
    public String join(@Valid @RequestBody SignUpDto signUpDto) {

        memberService.signup(signUpDto);
        return "회원가입완료";
    }
}