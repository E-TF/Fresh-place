package individual.freshplace.controller;

import individual.freshplace.dto.SignUpDto;
import individual.freshplace.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/public/signup")
    public String join(@Valid @RequestBody SignUpDto signUpDto) {

        try {
            memberService.signup(signUpDto);
            return "회원가입완료";
        }catch (RuntimeException e) {
            return "회원가입실패";
        }
    }
}