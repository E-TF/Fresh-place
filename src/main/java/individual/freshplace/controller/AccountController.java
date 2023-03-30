package individual.freshplace.controller;

import individual.freshplace.dto.auth.login.LoginRequest;
import individual.freshplace.dto.auth.login.LoginResponse;
import individual.freshplace.dto.auth.token.TokenReissueResponse;
import individual.freshplace.dto.signup.SignupRequest;
import individual.freshplace.dto.auth.token.TokenReissueRequest;
import individual.freshplace.service.AuthenticationService;
import individual.freshplace.service.SignupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final SignupService signupService;
    private final AuthenticationService authenticationService;

    @PostMapping("/public/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public void signup(@Valid @RequestBody SignupRequest signupRequest) {
        signupService.signup(signupRequest);
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        return authenticationService.login(loginRequest);
    }

    @PatchMapping("/reissue")
    @ResponseStatus(HttpStatus.OK)
    public TokenReissueResponse reissue(@Valid @RequestBody TokenReissueRequest reissueTokenRequest) {
        return authenticationService.reissue(reissueTokenRequest);
    }

}
