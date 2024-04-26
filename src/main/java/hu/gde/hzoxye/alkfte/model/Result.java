package hu.gde.hzoxye.alkfte.model;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "results")
public class Result extends AbstractPersistable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "race_id",
            nullable = false
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Race race;

    @OneToOne
    @JoinColumn(
            name = "runner_id",
            referencedColumnName = "id"
    )
    private Runner runner;

    @Column(name = "result", nullable = false)
    private Integer result;

    public Result() {
    }

    public Result(Race race, Runner runner, Integer result) {
        this.race = race;
        this.runner = runner;
        this.result = result;
    }

    public Long getId() {
        return id;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public Runner getRunner() {
        return runner;
    }

    public void setRunner(Runner runner) {
        this.runner = runner;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

}