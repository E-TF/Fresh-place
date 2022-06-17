package individual.freshplace.controller;

import individual.freshplace.config.auth.PrincipalDetails;
import individual.freshplace.dto.profile.ProfileUpdateRequest;
import individual.freshplace.dto.profile.ProfileResponse;
import individual.freshplace.dto.signup.SignupRequest;
import individual.freshplace.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/public/signup")
    public ResponseEntity signup(@Valid @RequestBody SignupRequest signupRequest) {
        memberService.signup(signupRequest);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/members/profile")
    public ResponseEntity<ProfileResponse> getProfile(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ResponseEntity.ok().body(memberService.getProfile(principalDetails.getUsername()));
    }

    @DeleteMapping("/members/withdrawal")
    public ResponseEntity withdrawal(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        memberService.withdrawal(principalDetails.getUsername());
        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping("/members/profile")
    public ResponseEntity<ProfileResponse> updateProfile(@AuthenticationPrincipal PrincipalDetails principalDetails,  @Valid @RequestBody ProfileUpdateRequest profileUpdateRequest) {
        return ResponseEntity.ok().body(memberService.updateMember(principalDetails.getUsername(), profileUpdateRequest));
    }
}
