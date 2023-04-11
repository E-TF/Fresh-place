package individual.freshplace.controller;

import individual.freshplace.dto.auth.login.LoginRequest;
import individual.freshplace.dto.auth.login.LoginResponse;
import individual.freshplace.dto.auth.token.TokenReissueResponse;
import individual.freshplace.dto.signup.SignupRequest;
import individual.freshplace.service.AuthenticationService;
import individual.freshplace.service.SignupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    @ResponseStatus(HttpStatus.CREATED)
    public LoginResponse login(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse httpServletResponse) {
        return authenticationService.login(loginRequest, httpServletResponse);
    }

    @PatchMapping("/reissue")
    public TokenReissueResponse reissue(HttpServletRequest httpServletRequest) {
        return authenticationService.reissue(httpServletRequest);
    }

    @DeleteMapping("/members/logout")
    public void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        authenticationService.logout(httpServletRequest, httpServletResponse);
    }

}
