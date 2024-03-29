package individual.freshplace.controller;

import individual.freshplace.util.auth.PrincipalDetails;
import individual.freshplace.dto.profile.ProfileUpdateRequest;
import individual.freshplace.dto.profile.ProfileResponse;
import individual.freshplace.service.ProfileService;
import individual.freshplace.util.constant.Cache;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members/profile")
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    @Cacheable(cacheNames = Cache.PROFILE, key = "#principalDetails.getUsername()")
    public ProfileResponse getProfile(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return profileService.getProfile(principalDetails.getUsername());
    }

    @PutMapping
    @CachePut(cacheNames = Cache.ITEM, key = "#profileUpdateRequest.getMemberId()")
    public ProfileResponse updateProfile(@AuthenticationPrincipal PrincipalDetails principalDetails, @Valid @RequestBody ProfileUpdateRequest profileUpdateRequest) {
        return profileService.updateProfile(principalDetails.getUsername(), profileUpdateRequest);
    }
}
