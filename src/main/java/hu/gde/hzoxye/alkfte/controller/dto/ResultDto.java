package hu.gde.hzoxye.alkfte.controller.dto;

public class ResultDto {
    private Long raceId;
    private Long runnerId;
    private Number result;

    public Long getRaceId() { return raceId; }
    public void setRaceId(Long id) { this.raceId = id; }

    public Long getRunnerId() { return runnerId; }
    public void setRunnerId(Long id) { this.runnerId = id; }

    public Number getResult() { return result; }
    public void setResult(Number result) { this.result = result; }
}
