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
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path = "/", produces = "application/json")
public class RacesController {

    @Autowired
    RunnerRepository runnerRepository;
    @Autowired
    RaceRepository raceRepository;
    @Autowired
    private ResultRepository resultRepository;

    @PostMapping("/addResult")
    ResponseEntity<Race> addResult(@RequestBody ResultDto result) {
        try {
            Optional<Race> race = raceRepository.findById(result.getRaceId());
            Optional<Runner> runner = runnerRepository.findById(result.getRunnerId());
            if (race.isPresent() && runner.isPresent()) {
                resultRepository.save(new Result(race.get(), runner.get(), result.getResult()));
                return new ResponseEntity<>(null, HttpStatus.CREATED);
            }
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
