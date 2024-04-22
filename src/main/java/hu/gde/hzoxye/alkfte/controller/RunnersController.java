package hu.gde.hzoxye.alkfte.controller;

import hu.gde.hzoxye.alkfte.model.Runner;
import hu.gde.hzoxye.alkfte.repository.RunnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(path = "/", produces = "application/json")
public class RunnersController {

    @Autowired
    RunnerRepository runnerRepository;

    @GetMapping("/getRunners")
    public @ResponseBody ResponseEntity<List<Runner>> getRunners() {
        List<Runner> runners = new ArrayList<Runner>();

        runnerRepository.findAll().forEach(runners::add);

        if (runners.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(runners, HttpStatus.OK);

    }

}
