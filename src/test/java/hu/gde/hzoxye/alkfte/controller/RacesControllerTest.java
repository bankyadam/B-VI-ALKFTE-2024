package hu.gde.hzoxye.alkfte.controller;

import hu.gde.hzoxye.alkfte.model.Race;
import hu.gde.hzoxye.alkfte.model.Result;
import hu.gde.hzoxye.alkfte.model.Runner;
import hu.gde.hzoxye.alkfte.repository.RaceRepository;
import hu.gde.hzoxye.alkfte.repository.ResultRepository;
import hu.gde.hzoxye.alkfte.repository.RunnerRepository;
import hu.gde.hzoxye.alkfte.types.Gender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RacesControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RunnerRepository runnerRepository;
    @Autowired
    private RaceRepository raceRepository;
    @Autowired
    private ResultRepository resultRepository;

    @AfterEach
    void cleanupDataBase() {
        raceRepository.deleteAll();
        resultRepository.deleteAll();
        runnerRepository.deleteAll();
    }

    @Test
    void addRace_RaceNotExists_Creates() throws Exception {
        this.mockMvc.perform(post("/addRace")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Race name\",\"distance\":42}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Race name"))
                .andExpect(jsonPath("$.distance").value(42));
    }

    @Test
    void updateRace_RaceExists_Updates() throws Exception {
        Race race = raceRepository.save(new Race("Race name", 40));
        this.mockMvc.perform(post("/updateRace")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":" + race.getId() + ",\"name\":\"New Race Name\",\"distance\":42}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(race.getId()))
                .andExpect(jsonPath("$.name").value("New Race Name"))
                .andExpect(jsonPath("$.distance").value(42));
    }

    @Test
    void getRaceRunners_RaceNotExists_ReturnsNotFound() throws Exception {
        this.mockMvc.perform(get("/getRaceRunners/1234"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getRaceRunners_RaceExistsWithNoRunners_ReturnsEmptyContent() throws Exception {
        Race race = raceRepository.save(new Race("Race name", 40));
        this.mockMvc.perform(get("/getRaceRunners/{id}", race.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void getRaceRunners_RaceExistsWithRunners_ReturnsRunners() throws Exception {
        Race race = raceRepository.save(new Race("Race name", 40));
        Runner runner1 = runnerRepository.save(new Runner("Adam", 42, Gender.MALE));
        Runner runner2 = runnerRepository.save(new Runner("Ben", 18, Gender.MALE));
        Runner runner3 = runnerRepository.save(new Runner("Chloe", 25, Gender.FEMALE));

        resultRepository.saveAll(List.of(
                new Result(race, runner1, 11),
                new Result(race, runner2, 22),
                new Result(race, runner3, 33)
        ));

        this.mockMvc.perform(get("/getRaceRunners/{id}", race.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*]", hasSize(3)))
                .andExpect(jsonPath("$[0].name").value("Adam"))
                .andExpect(jsonPath("$[1].name").value("Ben"))
                .andExpect(jsonPath("$[2].name").value("Chloe"));
    }

}
