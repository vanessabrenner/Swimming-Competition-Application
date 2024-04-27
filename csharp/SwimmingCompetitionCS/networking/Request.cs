using System;
using System.Collections.Generic;
using model;

namespace networking
{
    [Serializable]
    public class Request
    {
        private RequestType type;
        private string username;
        private string password;
        private Organizer organizer;
        private Participant participant;
        private Challenge challenge;
        private string nameParticipant;
        private int ageParticipant;
        private List<Challenge> challenges;

        public RequestType Type
        {
            get => type;
            set => type = value;
        }

        public string Username
        {
            get => username;
            set => username = value;
        }

        public string Password
        {
            get => password;
            set => password = value;
        }

        public Organizer Organizer
        {
            get => organizer;
            set => organizer = value;
        }

        public Participant Participant
        {
            get => participant;
            set => participant = value;
        }

        public Challenge Challenge
        {
            get => challenge;
            set => challenge = value;
        }

        public string NameParticipant
        {
            get => nameParticipant;
            set => nameParticipant = value;
        }

        public int AgeParticipant
        {
            get => ageParticipant;
            set => ageParticipant = value;
        }

        public List<Challenge> Challenges
        {
            get => challenges;
            set => challenges = value;
        }
    }
}