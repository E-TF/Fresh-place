package individual.freshplace.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
public class HomeController {

    @GetMapping("/public/main")
    public String getMainPage() {
        return "현재 시간은 " + LocalDate.now() + " 입니다.";
    }
}
