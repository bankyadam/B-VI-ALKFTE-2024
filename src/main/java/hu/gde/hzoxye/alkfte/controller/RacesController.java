package hu.gde.hzoxye.alkfte.controller;

import hu.gde.hzoxye.alkfte.controller.dto.RaceDto;
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

    @PostMapping("/addRace")
    ResponseEntity<Race> addRace(@RequestBody RaceDto newRace) {
        try {
            Race race = raceRepository.save(new Race(newRace.getName(), newRace.getDistance()));
            return new ResponseEntity<>(race, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/updateRace")
    ResponseEntity<Race> updateRace(@RequestBody RaceDto existingRace) {
        Optional<Race> race = raceRepository.findById(existingRace.getId());
        if (race.isPresent()) {
            race.get().setName(existingRace.getName());
            race.get().setDistance(existingRace.getDistance());
            Race updatedRace = raceRepository.save(race.get());
            return new ResponseEntity<>(updatedRace, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

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
