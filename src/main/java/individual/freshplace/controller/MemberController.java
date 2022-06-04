package individual.freshplace.controller;

import individual.freshplace.config.auth.PrincipalDetails;
import individual.freshplace.dto.profile.ProfileUpdateRequest;
import individual.freshplace.dto.profile.ProfileResponse;
import individual.freshplace.dto.signup.SignupRequest;
import individual.freshplace.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/public/signup")
    public ResponseEntity signup(@RequestBody @Valid SignupRequest signupRequest) {
        memberService.saveMember(signupRequest.toServiceDto(passwordEncoder.encode(signupRequest.getPassword())));
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/member/mypage/profile")
    public ResponseEntity<ProfileResponse> getProfile(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return new ResponseEntity<>(memberService.findMember(principalDetails.getUsername()), HttpStatus.OK);
    }

    @DeleteMapping("/member/mypage/withdrawal")
    public ResponseEntity withdrawal(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        memberService.deleteMember(principalDetails.getUsername());
        return new ResponseEntity(HttpStatus.OK);
    }

    @PatchMapping("/member/mypage/profile")
    public ResponseEntity<ProfileResponse> updateProfile(@AuthenticationPrincipal PrincipalDetails principalDetails,  @Valid @RequestBody ProfileUpdateRequest profileUpdateRequest) {
        return new ResponseEntity<>(memberService.updateMember(principalDetails.getUsername(), profileUpdateRequest), HttpStatus.OK);
    }
}