package com.ironlady.model;

public class Program {

    private int id;
    private String name;
    private String target;
    private String interest;
    private String duration;
    private String outcome;

    public Program() {}

    public Program(int id, String name, String target, String interest, String duration, String outcome) {
        this.id = id;
        this.name = name;
        this.target = target;
        this.interest = interest;
        this.duration = duration;
        this.outcome = outcome;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) { // ✅ Fixed
        this.duration = duration;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }
}
