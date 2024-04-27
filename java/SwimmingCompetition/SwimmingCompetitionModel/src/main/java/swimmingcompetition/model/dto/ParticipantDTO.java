package swimmingcompetition.model.dto;

import swimmingcompetition.model.Challenge;

import java.util.List;

public class ParticipantDTO {
    private long id;
    private String name;
    private int age;
    private List<Challenge> challenges;

    public ParticipantDTO(long id, String name, int age, List<Challenge> challenges) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.challenges = challenges;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Challenge> getChallenges() {
        return challenges;
    }

    public void setChallenges(List<Challenge> challenges) {
        this.challenges = challenges;
    }

    @Override
    public String toString() {
        return "Participant{" +
                "id=" + id +
                ", nume='" + name + '\'' +
                ", varsta=" + Integer.toString(age) +
                '}';
    }
}
