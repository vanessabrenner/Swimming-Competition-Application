package swimmingcompetition.model;

import java.io.Serializable;

public class Challenge extends Entity<Long> implements Serializable {
    private Distance distance;
    private Style style;

    public Challenge(Distance distance, Style style) {
        this.distance = distance;
        this.style = style;
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

    @Override
    public String toString() {
        return "Challenge{" +
                "id=" + id +
                ", distance=" + distance.get() +
                ", style=" + style +
                '}';
    }
}
