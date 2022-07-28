package individual.freshplace.controller;

import individual.freshplace.config.auth.PrincipalDetails;
import individual.freshplace.dto.profile.ProfileUpdateRequest;
import individual.freshplace.dto.profile.ProfileResponse;
import individual.freshplace.service.FProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class ProfileController {

    private final FProfileService fProfileService;

    @GetMapping("/members/profile")
    public ResponseEntity<ProfileResponse> getProfile(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ResponseEntity.ok().body(fProfileService.getProfile(principalDetails.getUsername()));
    }

    @PutMapping("/members/profile")
    public ResponseEntity<ProfileResponse> updateProfile(@AuthenticationPrincipal PrincipalDetails principalDetails, @Valid @RequestBody ProfileUpdateRequest profileUpdateRequest) {
        return ResponseEntity.ok().body(fProfileService.updateProfile(principalDetails.getUsername(), profileUpdateRequest));
    }
}
