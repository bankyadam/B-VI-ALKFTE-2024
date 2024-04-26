package hu.gde.hzoxye.alkfte.repository;

import hu.gde.hzoxye.alkfte.model.Result;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResultRepository extends JpaRepository<Result, Long> {
    List<Result> findByRaceId(Long raceId);
}
