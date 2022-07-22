package individual.freshplace.controller;

import individual.freshplace.config.auth.PrincipalDetails;
import individual.freshplace.dto.profile.ProfileUpdateRequest;
import individual.freshplace.dto.profile.ProfileResponse;
import individual.freshplace.service.ProfileFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileFacade profileFacade;

    @GetMapping("/members/profile")
    public ResponseEntity<ProfileResponse> getProfile(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ResponseEntity.ok().body(profileFacade.getProfile(principalDetails.getUsername()));
    }

    @PutMapping("/members/profile")
    public ResponseEntity<ProfileResponse> updateProfile(@Valid @RequestBody ProfileUpdateRequest profileUpdateRequest, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ResponseEntity.ok().body(profileFacade.updateProfile(principalDetails.getUsername(), profileUpdateRequest));
    }

}
