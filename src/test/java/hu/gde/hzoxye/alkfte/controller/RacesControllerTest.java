package hu.gde.hzoxye.alkfte.controller;

import hu.gde.hzoxye.alkfte.model.Race;
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

import static org.junit.jupiter.api.Assertions.*;
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
    void addResult_RaceExistsAndRunnerNotAdded_AddsResult() throws Exception {
        Race race = raceRepository.save(new Race("Race name", 12));
        Runner runner = runnerRepository.save(new Runner("Runner name", 42, Gender.MALE));

        String jsonContent = String.format(
                "{\"raceId\":%d,\"runnerId\":%d,\"result\":%d}",
                race.getId(),
                runner.getId(),
                1234
        );
        this.mockMvc.perform(post("/addResult")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        assertNotNull(resultRepository.findAll().get(0));
    }

    @Test
    void addResult_RaceNotExists_ReturnsNotFound() throws Exception {
        Runner runner = runnerRepository.save(new Runner("Adam", 42, Gender.MALE));
        String jsonContent = String.format(
                "{\"raceId\":%d,\"runnerId\":%d,\"result\":%d}",
                1,
                runner.getId(),
                1234
        );
        this.mockMvc.perform(post("/addResult")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        assertEquals(resultRepository.findAll().size(), 0);
    }

    @Test
    void addResult_RunnerNotExists_ReturnsNotFound() throws Exception {
        Race race = raceRepository.save(new Race("Race name", 15));
        String jsonContent = String.format(
                "{\"raceId\":%d,\"runnerId\":%d,\"result\":%d}",
                race.getId(),
                1,
                1234
        );
        this.mockMvc.perform(post("/addResult")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        assertEquals(resultRepository.findAll().size(), 0);
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
}
