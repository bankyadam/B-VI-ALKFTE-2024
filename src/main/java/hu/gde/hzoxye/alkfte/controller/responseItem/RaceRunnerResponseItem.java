package hu.gde.hzoxye.alkfte.controller.responseItem;

public class RaceRunnerResponseItem {
    private String name;
    private Integer result;

    public RaceRunnerResponseItem(String name, Integer result) {
        this.name = name;
        this.result = result;
    }

    public String getName() {
        return name;
    }

    public Integer getResult() {
        return result;
    }
}
