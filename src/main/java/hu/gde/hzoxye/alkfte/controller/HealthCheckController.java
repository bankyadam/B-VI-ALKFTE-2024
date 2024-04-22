package hu.gde.hzoxye.alkfte.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HealthCheckController {

    @GetMapping("/healthcheck")
    public @ResponseBody String greeting() {
        return "OK";
    }

}