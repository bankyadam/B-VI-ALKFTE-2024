package hu.gde.hzoxye.alkfte.controller;

import hu.gde.hzoxye.alkfte.model.Runner;
import hu.gde.hzoxye.alkfte.repository.RunnerRepository;
import hu.gde.hzoxye.alkfte.types.Gender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RunnersControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RunnerRepository runnerRepository;

    @AfterEach
    void cleanupDataBase() {
        runnerRepository.deleteAll();
    }

    @Test
    void getRunners_NobodyInDB_ReturnsNull() throws Exception {
        this.mockMvc.perform(get("/getRunners"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getRunners_OneRunnerInDB_ReturnsRunnerData() throws Exception {
        runnerRepository.save(new Runner("Adam", 18, Gender.MALE));

        this.mockMvc.perform(get("/getRunners"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*]", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Adam"))
                .andExpect(jsonPath("$[0].age").value(18))
                .andExpect(jsonPath("$[0].gender").value(Gender.MALE.toString()));
    }

    @Test
    void getRunners_MoreRunnerInDB_ReturnsAllRunnerData() throws Exception {
        runnerRepository.save(new Runner("Adam", 42, Gender.MALE));
        runnerRepository.save(new Runner("Adrienn", 39, Gender.FEMALE));

        this.mockMvc.perform(get("/getRunners"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*]", hasSize(2)));
    }

    @Test
    void addRunner_NotExists_ReturnsNewRunner() throws Exception {
        this.mockMvc.perform(post("/addRunner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Adam\",\"age\":42,\"gender\":\"MALE\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Adam"))
                .andExpect(jsonPath("$.age").value(42))
                .andExpect(jsonPath("$.gender").value(Gender.MALE.toString()));
    }

}
