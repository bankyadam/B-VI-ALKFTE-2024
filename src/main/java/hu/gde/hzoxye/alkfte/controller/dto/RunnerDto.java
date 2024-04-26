package hu.gde.hzoxye.alkfte.controller.dto;

import hu.gde.hzoxye.alkfte.types.Gender;

public class RunnerDto {

    private String name;
    private Integer age;
    private Gender gender;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public Gender getGender() { return gender; }
    public void setGender(Gender gender) { this.gender = gender; }
}
