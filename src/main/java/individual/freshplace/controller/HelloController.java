package individual.freshplace.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class HelloController {

    @GetMapping("/home")
    public ResponseEntity<String> home() {
        return ResponseEntity.ok("home");
    }

    @PostMapping("/token")
    public ResponseEntity<String> token() {
        return ResponseEntity.ok("token");
    }


}
