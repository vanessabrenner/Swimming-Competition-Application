using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using model;
using persistence;
using services;

namespace Server
{
    public class SwimmingCompetitionServicesImpl: ISwimmingCompetitionServices
    {
        private ChallengeDBRepository challengeDBRepository;
        private OrganizerDBRepository organizerDBRepository;
        private ParticipantDBRepository participantDBRepository;
        private RegistrationDBRepository registrationDBRepository;
        private IDictionary<long, ISwimmingCompetitionObserver> loggedOrganizers;

        public SwimmingCompetitionServicesImpl(ChallengeDBRepository challengeDbRepository, OrganizerDBRepository organizerDbRepository, ParticipantDBRepository participantDbRepository, RegistrationDBRepository registrationDbRepository)
        {
            challengeDBRepository = challengeDbRepository;
            organizerDBRepository = organizerDbRepository;
            participantDBRepository = participantDbRepository;
            registrationDBRepository = registrationDbRepository;
            loggedOrganizers = new Dictionary<long, ISwimmingCompetitionObserver>();
        }

        public void login(model.Organizer user, ISwimmingCompetitionObserver client)
        {
            if(loggedOrganizers.ContainsKey(user.Id))
                throw new SwimmingCompetitionException("Organizer already logged in.");
            loggedOrganizers[user.Id] =  client;
        }

        public model.Organizer findAccount(string username, string password)
        {
            model.Organizer organizer = this.organizerDBRepository.FindAccount(username, password);
            if(organizer != null){
                if(loggedOrganizers.ContainsKey(organizer.Id)){
                    throw new SwimmingCompetitionException("Organizer already logged.");
                }
            }
            else{
                throw new SwimmingCompetitionException("Organizer not found.");
            }
            return organizer;
        }

        public model.ChallengeDTO[] findAllChallenges()
        {
            List<model.ChallengeDTO> result = new List<model.ChallengeDTO>();
            IEnumerable<model.Challenge> challenges = this.challengeDBRepository.FindAll();
            if(challenges == null){
                throw new SwimmingCompetitionException("Challenges not found");
            }

            foreach (var challenge in challenges)
            {
                int no = this.getNumberOfParticipantsByChallenge(challenge);
                model.ChallengeDTO challengeDto = new model.ChallengeDTO(challenge.Id, challenge.Distance, challenge.Style, no);
                result.Add(challengeDto);
            }
            return result.ToArray();
        }

        private int getNumberOfParticipantsByChallenge(model.Challenge challenge)
        { 
            return this.registrationDBRepository.CountByChallenge(challenge);
        }

        public IList<model.ParticipantDTO> findParticipantsByChallenge(model.Challenge challenge)
        {
            IList<model.ParticipantDTO> result = new List<model.ParticipantDTO>();
            IList<model.Participant> participants = this.registrationDBRepository.FindParticipantsByChallenge(challenge);
            if(participants == null){
                throw new SwimmingCompetitionException("Participants not found.");
            }

            foreach (var participant in participants)
            {
                model.ParticipantDTO participantDto = new model.ParticipantDTO(participant.Id, participant.Name, participant.Age,
                    this.findChallengesByParticipant(participant).ToList());
                result.Add(participantDto);
            }
            return result;
        }

        private IList<model.Challenge> findChallengesByParticipant(model.Participant participant)
        {
            IList<model.Challenge> challenges = this.registrationDBRepository.FindChallengesByParticipant(participant);
            if(challenges == null){
                throw new SwimmingCompetitionException("Challenges not found.");
            }
            return challenges;
        }

        public model.Participant addParticipant(string name, int age)
        {
            model.Participant participant = new model.Participant(name, age);

            model.Participant participant1 = this.participantDBRepository.Save(participant);
            return participant1;
        }

        public void addParticipantToMoreChallenges(model.Participant participant, List<model.Challenge> challenges)
        {
            this.registrationDBRepository.AddParticipantToMoreChallenges(participant, challenges);
            // TO DO
            notifyOrganizators(this.challengeDBRepository.FindAll().ToArray());
        }

        private void notifyOrganizators(model.Challenge[] challenges)
        {
            IEnumerable<model.Organizer> organizers = organizerDBRepository.FindAll();
            List<model.ChallengeDTO> result = new List<model.ChallengeDTO>();
            if(challenges == null){
                throw new SwimmingCompetitionException("Challenges not found");
            }
            foreach (var challenge in challenges)
            {
                int no = this.getNumberOfParticipantsByChallenge(challenge);
                model.ChallengeDTO challengeDto = new model.ChallengeDTO(challenge.Id, challenge.Distance, challenge.Style, no);
                result.Add(challengeDto);
            }
            foreach (var organizer in organizers)
            {
                if (loggedOrganizers.ContainsKey(organizer.Id))
                {
                    ISwimmingCompetitionObserver observer = loggedOrganizers[organizer.Id];
                    Task.Run(() => observer.updateTables(result.ToArray()));
                }
            }
        }

        public void logout(model.Organizer user, ISwimmingCompetitionObserver client)
        {
            ISwimmingCompetitionObserver localClient = loggedOrganizers[user.Id];
            if(localClient == null){
                throw new SwimmingCompetitionException("Client not logged in.");
            }

            loggedOrganizers.Remove(user.Id);
        }
    }
}