package swimmingcompetition.persistence;



import swimmingcompetition.model.Challenge;
import swimmingcompetition.model.Participant;
import swimmingcompetition.model.Registration;

import java.util.List;

public interface RegistrationRepository extends Repository<Long, Registration> {
    /**
     * Cauta participantii ce participa la o anumita proba
     * @param challenge
     * @return Lista de participanti ce participa la o anumita proba
     */
    List<Participant> findParticipantsByChallenge(Challenge challenge);

    /**
     * Cauta probele la care participa un anumit participant
     * @param participant
     * @return Lista de probe la care participa un anumit participant
     */
    List<Challenge> findChallengesByParticipant(Participant participant);

    /**
     * Aduga un participant la mai multe probe
     * @param challenges
     * @return null, daca participantul nu s-a adaugat cu succes si
     *         participantul respectiv, altfel
     */
    void addParticipantToMoreChallenges(Participant participant, List<Challenge> challenges);

    /**
     * Afla numarul de participanti inscrisi la o proba
     * @param challenge
     * @return numarul de participanti inscrisi la o proba
     */
    int countByChallenge(Challenge challenge);
}
