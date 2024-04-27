package swimmingcompetition.model;

public class Registration extends Entity<Long>{
    // TO DO
    private Participant participant;
    private Challenge challenge;

    public Registration(Participant participant, Challenge challenge) {
        this.participant = participant;
        this.challenge = challenge;
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public Challenge getChallenge() {
        return challenge;
    }

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }

    @Override
    public String toString() {
        return "Registration{" +
                "id: " + id +
                ", Participant=" + participant +
                ", Challenge=" + challenge +
                '}';
    }
}
