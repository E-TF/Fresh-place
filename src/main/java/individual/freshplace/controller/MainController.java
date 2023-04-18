package individual.freshplace.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController implements ErrorController {

    @GetMapping({"/", "/error"})
    public String get() {
        return "index.html";
    }
}
