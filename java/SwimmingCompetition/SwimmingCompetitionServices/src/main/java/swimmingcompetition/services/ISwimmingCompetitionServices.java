package swimmingcompetition.services;


import swimmingcompetition.model.Challenge;
import swimmingcompetition.model.Organizer;
import swimmingcompetition.model.Participant;
import swimmingcompetition.model.Registration;
import swimmingcompetition.model.dto.ChallengeDTO;
import swimmingcompetition.model.dto.ParticipantDTO;

import java.util.List;

public interface ISwimmingCompetitionServices {
    void login(Organizer user, ISwimmingCompetitionObserver client) throws SwimmingCompetitionException;
    Organizer findAccount(String username, String password) throws SwimmingCompetitionException;
    ChallengeDTO[] findAllChallenges() throws SwimmingCompetitionException;
//    int getNumberOfParticipantsByChallenge(Challenge challenge) throws SwimmingCompetitionException;
    List<ParticipantDTO> findParticipantsByChallenge(Challenge challenge) throws SwimmingCompetitionException;
//    List<Challenge> findChallengesByParticipant(Participant participant) throws SwimmingCompetitionException;
    Participant addParticipant(String name, int age) throws SwimmingCompetitionException;
    void addParticipantToMoreChallenges(Participant participant, List<Challenge> challenges) throws SwimmingCompetitionException;
    void logout(Organizer user, ISwimmingCompetitionObserver client) throws SwimmingCompetitionException;
}
