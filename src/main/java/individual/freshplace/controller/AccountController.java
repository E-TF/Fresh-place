package individual.freshplace.controller;

import individual.freshplace.dto.signup.SignupRequest;
import individual.freshplace.service.FSignupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final FSignupService fSignupService;

    @PostMapping("/public/signup")
    public ResponseEntity signup(@Valid @RequestBody SignupRequest signupRequest) {
        fSignupService.signupFacade(signupRequest);
        return ResponseEntity.ok().build();
    }

}
