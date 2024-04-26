package hu.gde.hzoxye.alkfte.controller;

import hu.gde.hzoxye.alkfte.controller.dto.RunnerDto;
import hu.gde.hzoxye.alkfte.model.Runner;
import hu.gde.hzoxye.alkfte.repository.RunnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/", produces = "application/json")
public class RunnersController {

    @Autowired
    RunnerRepository runnerRepository;

    @GetMapping("/getRunners")
    ResponseEntity<List<Runner>> getRunners() {
        List<Runner> runners = new ArrayList<>(runnerRepository.findAll());

        if (runners.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(runners, HttpStatus.OK);
    }

    @PostMapping("/addRunner")
    ResponseEntity<Runner> addRunner(@RequestBody RunnerDto newRunner) {
        try {
            Runner runner = runnerRepository.save(new Runner(newRunner.getName(), newRunner.getAge(), newRunner.getGender()));
            return new ResponseEntity<>(runner, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
