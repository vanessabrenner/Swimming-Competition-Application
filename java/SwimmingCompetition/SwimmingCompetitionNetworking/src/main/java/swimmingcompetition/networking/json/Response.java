package swimmingcompetition.networking.json;

import swimmingcompetition.model.Challenge;
import swimmingcompetition.model.Organizer;
import swimmingcompetition.model.Participant;
import swimmingcompetition.model.Registration;
import swimmingcompetition.model.dto.ChallengeDTO;
import swimmingcompetition.model.dto.ParticipantDTO;

import java.io.Serializable;
import java.util.List;


public class Response implements Serializable {
    private ResponseType type;
    private String errorMessage;
    private Organizer account;
    private ChallengeDTO[] allChallenges;
    private List<ParticipantDTO> paricipantsByChallenge;
    private Challenge challenge;
    private Participant addedParticipant;


    public Response() {
    }

    public ResponseType getType() {
        return type;
    }

    public void setType(ResponseType type) {
        this.type = type;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Organizer getAccount() {
        return account;
    }

    public void setAccount(Organizer account) {
        this.account = account;
    }

    public ChallengeDTO[] getAllChallenges() {
        return allChallenges;
    }

    public void setAllChallenges(ChallengeDTO[] allChallenges) {
        this.allChallenges = allChallenges;
    }

    public List<ParticipantDTO> getParicipantsByChallenge() {
        return paricipantsByChallenge;
    }

    public void setParicipantsByChallenge(List<ParticipantDTO> paricipantsByChallenge) {
        this.paricipantsByChallenge = paricipantsByChallenge;
    }

    public Challenge getChallenge() {
        return challenge;
    }

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }


    public Participant getAddedParticipant() {
        return addedParticipant;
    }

    public void setAddedParticipant(Participant addedParticipant) {
        this.addedParticipant = addedParticipant;
    }

    @Override
    public String toString() {
        return "Response{" +
                "type=" + type +
                ", account=" + account +
                ", allChallenges=" + allChallenges +
                ", paricipantsByChallenge=" + paricipantsByChallenge +
                ", challenge=" + challenge +
                ", addedParticipant=" + addedParticipant +
                '}';
    }
}
