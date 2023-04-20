package individual.freshplace.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@Controller
public class ViewController implements ErrorController {

    @GetMapping("/error")
    public String redirectIndex(HttpServletResponse httpServletResponse) {
        log.error(httpServletResponse.getStatus() + " error");
        return "index.html";
    }
}
