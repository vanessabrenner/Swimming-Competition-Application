package swimmingcompetition.model.dto;

import swimmingcompetition.model.Distance;
import swimmingcompetition.model.Style;

public class ChallengeDTO {
    private long id;
    private Distance distance;
    private Style style;
    private int NoParticipants;

    public ChallengeDTO(long id, Distance distance, Style style, int noParticipants) {
        this.id = id;
        this.distance = distance;
        this.style = style;
        NoParticipants = noParticipants;
    }

    public Distance getDistance() {
        return distance;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getNoParticipants() {
        return NoParticipants;
    }

    public void setNoParticipants(int noParticipants) {
        NoParticipants = noParticipants;
    }

    @Override
    public String toString() {
        return "Challenge{" +
                "id=" + id +
                ", distance=" + distance.get() +
                ", style=" + style +
                '}';
    }
}
