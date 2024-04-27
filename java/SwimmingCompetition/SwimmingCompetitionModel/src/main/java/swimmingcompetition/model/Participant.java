package swimmingcompetition.model;

import java.io.Serializable;

public class Participant extends Entity<Long> implements Serializable {
    private String name;
    private int age;

    public Participant(String name, int age) {
        this.name = name;
        this.age = age;
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

    @Override
    public String toString() {
        return "Participant{" +
                "id=" + id +
                ", nume='" + name + '\'' +
                ", varsta=" + Integer.toString(age) +
                '}';
    }
}
