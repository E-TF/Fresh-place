package individual.freshplace.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/public/main")
    public String getMainPage() {
        return "main";
    }
}
