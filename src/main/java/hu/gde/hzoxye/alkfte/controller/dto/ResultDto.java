package hu.gde.hzoxye.alkfte.controller.dto;

public class ResultDto {
    private Long raceId;
    private Long runnerId;
    private Integer result;

    public Long getRaceId() { return raceId; }
    public void setRaceId(Long id) { this.raceId = id; }

    public Long getRunnerId() { return runnerId; }
    public void setRunnerId(Long id) { this.runnerId = id; }

    public Integer getResult() { return result; }
    public void setResult(Integer result) { this.result = result; }
}
