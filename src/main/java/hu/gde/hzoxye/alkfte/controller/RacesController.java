package hu.gde.hzoxye.alkfte.controller;

import hu.gde.hzoxye.alkfte.controller.dto.RaceDto;
import hu.gde.hzoxye.alkfte.controller.responseItem.RaceRunnerResponseItem;
import hu.gde.hzoxye.alkfte.model.Race;
import hu.gde.hzoxye.alkfte.model.Result;
import hu.gde.hzoxye.alkfte.repository.RaceRepository;
import hu.gde.hzoxye.alkfte.repository.ResultRepository;
import hu.gde.hzoxye.alkfte.repository.RunnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
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

    @GetMapping("/getRaces")
    ResponseEntity<ArrayList<Race>> getRaces() {
        ArrayList<Race> races = new ArrayList<>(raceRepository.findAll());

        if (races.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(races, HttpStatus.OK);
    }

    @GetMapping("/getRaceRunners/{id}")
    ResponseEntity<ArrayList<RaceRunnerResponseItem>> getRaceRunners(@PathVariable(value = "id") Long raceId) {
        Optional<Race> race = raceRepository.findById(raceId);
        if (race.isPresent()) {
            ArrayList<Result> results = new ArrayList<>(resultRepository.findByRaceId(race.get().getId()));

            if (results.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            ArrayList<RaceRunnerResponseItem> runners = new ArrayList<>();

            results.forEach(result -> {
                runners.add(new RaceRunnerResponseItem(result.getRunner().getName(), result.getResult()));
            });

            runners.sort(Comparator.comparingInt(RaceRunnerResponseItem::getResult));

            return new ResponseEntity<>(runners, HttpStatus.OK);
        }

        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/getAverageTime/{id}")
    ResponseEntity<Double> getAverageTime(@PathVariable(value = "id") Long raceId) {
        Optional<Race> race = raceRepository.findById(raceId);
        if (race.isPresent()) {
            ArrayList<Result> results = new ArrayList<>(resultRepository.findByRaceId(race.get().getId()));

            if (results.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            Double average = results.stream()
                    .mapToInt(Result::getResult)
                    .average()
                    .orElse(0);

            return new ResponseEntity<>(average, HttpStatus.OK);
        }

        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

}
