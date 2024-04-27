using System;
using System.Collections.Generic;
using model;

namespace services
{
    public interface ISwimmingCompetitionServices
    {
        void login(Organizer user, ISwimmingCompetitionObserver client);
        Organizer findAccount(String username, String password);
        ChallengeDTO[] findAllChallenges();
        // int getNumberOfParticipantsByChallenge(Challenge challenge);
        IList<ParticipantDTO> findParticipantsByChallenge(Challenge challenge);
        // IList<Challenge> findChallengesByParticipant(Participant participant);
        Participant addParticipant(String name, int age);
        void addParticipantToMoreChallenges(Participant participant, List<Challenge> challenges);
        void logout(Organizer user, ISwimmingCompetitionObserver client);
    }
}