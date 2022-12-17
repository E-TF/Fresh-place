package individual.freshplace.controller;

import individual.freshplace.config.auth.PrincipalDetails;
import individual.freshplace.dto.profile.ProfileUpdateRequest;
import individual.freshplace.dto.profile.ProfileResponse;
import individual.freshplace.service.FProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class ProfileController {

    private final FProfileService fProfileService;

    @GetMapping("/members/profile")
    public ProfileResponse getProfile(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return fProfileService.getProfile(principalDetails.getUsername());
    }

    @PutMapping("/members/profile")
    public ProfileResponse updateProfile(@AuthenticationPrincipal PrincipalDetails principalDetails, @Valid @RequestBody ProfileUpdateRequest profileUpdateRequest) {
        return fProfileService.updateProfile(principalDetails.getUsername(), profileUpdateRequest);
    }
}
