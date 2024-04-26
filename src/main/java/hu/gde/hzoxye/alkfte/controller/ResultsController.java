package hu.gde.hzoxye.alkfte.controller;

import hu.gde.hzoxye.alkfte.controller.dto.ResultDto;
import hu.gde.hzoxye.alkfte.model.Race;
import hu.gde.hzoxye.alkfte.model.Result;
import hu.gde.hzoxye.alkfte.model.Runner;
import hu.gde.hzoxye.alkfte.repository.RaceRepository;
import hu.gde.hzoxye.alkfte.repository.ResultRepository;
import hu.gde.hzoxye.alkfte.repository.RunnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping(path = "/", produces = "application/json")
public class ResultsController {

    @Autowired
    RunnerRepository runnerRepository;
    @Autowired
    RaceRepository raceRepository;
    @Autowired
    ResultRepository resultRepository;

    @PostMapping("/addResult")
    ResponseEntity<Result> addResult(@RequestBody ResultDto newResult) {
        try {
            Optional<Race> race = raceRepository.findById(newResult.getRaceId());
            Optional<Runner> runner = runnerRepository.findById(newResult.getRunnerId());
            if (race.isPresent() && runner.isPresent()) {
                Result result = resultRepository.save(new Result(race.get(), runner.get(), newResult.getResult()));
                return new ResponseEntity<>(result, HttpStatus.CREATED);
            }
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
