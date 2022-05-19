package individual.freshplace.controller;

import individual.freshplace.dto.SignupRequest;
import individual.freshplace.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity join(@Valid @RequestBody SignupRequest signupRequest) {
        memberService.signup(signupRequest);
        return new ResponseEntity(HttpStatus.OK);
    }
}