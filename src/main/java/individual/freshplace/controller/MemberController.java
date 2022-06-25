package individual.freshplace.controller;

import individual.freshplace.config.auth.PrincipalDetails;
import individual.freshplace.dto.profile.ProfileUpdateRequest;
import individual.freshplace.dto.profile.ProfileResponse;
import individual.freshplace.dto.signup.SignupRequest;
import individual.freshplace.service.MemberService;
import individual.freshplace.util.ErrorResponse;
import individual.freshplace.util.exception.StringLockException;
import individual.freshplace.util.lock.UserLevelLockTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final UserLevelLockTemplate userLevelLockTemplate;

    @PostMapping("/public/signup")
    public ResponseEntity signup(@Valid @RequestBody SignupRequest signupRequest) {
        userLevelLockTemplate.LockProcess(signupRequest.getMemberId(), () ->
                memberService.signup(signupRequest));

        return ResponseEntity.ok().build();
    }

    @GetMapping("/members/profile")
    public ResponseEntity<ProfileResponse> getProfile(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ResponseEntity.ok().body(memberService.getProfile(principalDetails.getUsername()));
    }

    @DeleteMapping("/members/withdrawal")
    public ResponseEntity withdrawal(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        memberService.withdrawal(principalDetails.getUsername());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/members/profile")
    public ResponseEntity<ProfileResponse> updateProfile(@AuthenticationPrincipal PrincipalDetails principalDetails,  @Valid @RequestBody ProfileUpdateRequest profileUpdateRequest) {
        return ResponseEntity.ok().body(userLevelLockTemplate.LockProcess(profileUpdateRequest.getMemberId(), () ->
                memberService.updateMember(principalDetails.getUsername(), profileUpdateRequest)));
    }

    @ExceptionHandler(StringLockException.class)
    public ResponseEntity<ErrorResponse> StringLockExceptionHandler(final StringLockException e) {
        return ErrorResponse.errorResponse(e.getErrorCode(), e.getValue());
    }
}
