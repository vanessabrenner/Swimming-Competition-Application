using System;
using System.Collections.Generic;
using model;
using services;

namespace Client
{
    public class SwimmingCompetitionCtrl: ISwimmingCompetitionObserver
    {
        public event EventHandler<SwimmingCompetitionEventArgs> updateEvent; //ctrl calls it when it has received an update
        private readonly ISwimmingCompetitionServices server;
        private Organizer currentOrganizer;

        public SwimmingCompetitionCtrl(ISwimmingCompetitionServices server)
        {
            this.server = server;
            currentOrganizer = null;
        }

        public Organizer FindAccount(string username, string password)
        {
            return this.server.findAccount(username, password);
            
        }

        public void login(Organizer organizer)
        {
            this.server.login(organizer, this);
            this.currentOrganizer = organizer;
        }

        public ChallengeDTO[] FindAllChallenges()
        {
            return this.server.findAllChallenges();
        }

        public Participant AddParticipant(string name, int age)
        {
            return this.server.addParticipant(name, age);
        }

        public void AddParticipantToMoreChallenges(Participant participant, List<Challenge> challenges)
        {
            this.server.addParticipantToMoreChallenges(participant, challenges);
        }

        public IList<ParticipantDTO> FindParticipantsByChallenge(Challenge challenge)
        {
            return this.server.findParticipantsByChallenge(challenge);
        }

        public void logout()
        {
            this.server.logout(currentOrganizer, this);
            currentOrganizer = null;
        }
        protected virtual void onOrganizerEvent(SwimmingCompetitionEventArgs e)
        {
            if (updateEvent == null) return;
            updateEvent(this, e);
            Console.WriteLine("Update Event called");
        }
        public void updateTables(ChallengeDTO[] challenges)
        {
            SwimmingCompetitionEventArgs eventArgs =
                new SwimmingCompetitionEventArgs(SwimmingCompetitionEvent.UPDATE_TABLES, challenges);
            onOrganizerEvent(eventArgs);
        }
    }
}