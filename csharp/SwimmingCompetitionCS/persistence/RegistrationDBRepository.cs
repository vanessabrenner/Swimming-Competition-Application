using System;
using System.Collections.Generic;
using System.Data;
using log4net;
using model;

namespace persistence
{
    public class RegistrationDBRepository : RegistrationRepository
    {
        private static readonly ILog log = LogManager.GetLogger(typeof(RegistrationDBRepository));

        IDictionary<String, string> props;

        public RegistrationDBRepository(IDictionary<string, string> props)
        {
            this.props = props;
        }
        
        public IList<Participant> FindParticipantsByChallenge(Challenge challenge)
        {
            log.InfoFormat("Finding all participants at the challenge {0}", challenge);
            IDbConnection connection = DBUtils.getConnection(props);
            List<Participant> participants = new List<Participant>();
            using (var comm = connection.CreateCommand())
            {
                comm.CommandText = "select * from participant as p inner join registration as r on p.id=r.idparticipant where r.idchallenge = @idchallenge";
                var idChallengeParam = comm.CreateParameter();
                idChallengeParam.ParameterName = "@idchallenge";
                idChallengeParam.Value = challenge.Id;
                comm.Parameters.Add(idChallengeParam);
                
                using (var dataR = comm.ExecuteReader())
                {
                    while (dataR.Read())
                    {
                        long id = dataR.GetInt32(0);
                        String name = dataR.GetString(1);
                        int age = dataR.GetInt16(2);
                        Participant participant = new Participant(name, age);
                        participant.Id = id;
                        participants.Add(participant);
                    }
                }
            }
            log.Info(participants);
            return participants;
        }

        public IList<Challenge> FindChallengesByParticipant(Participant participant)
        {
            log.InfoFormat("Finding all challenges of a participant {0}", participant);
            IDbConnection connection = DBUtils.getConnection(props);
            List<Challenge> challenges = new List<Challenge>();
            using (var comm = connection.CreateCommand())
            {
                comm.CommandText = "select * from challenge as c inner join registration as r on c.id=r.idchallenge where r.idparticipant = @idparticipant";
                var idParticipantParam = comm.CreateParameter();
                idParticipantParam.ParameterName = "@idparticipant";
                idParticipantParam.Value = participant.Id;
                comm.Parameters.Add(idParticipantParam);
                
                using (var dataR = comm.ExecuteReader())
                {
                    while (dataR.Read())
                    {
                        long id = dataR.GetInt32(0);
                        Style style = (Style)Enum.Parse(typeof(Style), dataR.GetString(1));
                        Distance distance = (Distance)dataR.GetInt16(2);
                        Challenge challenge = new Challenge(distance, style);
                        challenge.Id = id;
                        challenges.Add(challenge);
                    }
                }
            }
            log.Info(challenges);
            return challenges;
        }
        
        public void AddParticipantToMoreChallenges(Participant participant, List<Challenge> challenges)
        {
            log.InfoFormat("Saving participants {0} to challenges {1}", participant, challenges);
            var connection = DBUtils.getConnection(props);
            using (var comm = connection.CreateCommand())
            {
                comm.CommandText = "insert into registration(idparticipant, idchallenge) values(@idparticipant,@idchallenge)";
                foreach (var challenge in challenges)
                {
                    comm.Parameters.Clear();
                    var paramIdParticipant = comm.CreateParameter();
                    paramIdParticipant.ParameterName = "@idparticipant";
                    paramIdParticipant.Value = participant.Id;
                    comm.Parameters.Add(paramIdParticipant);

                    var paramIdChallenge = comm.CreateParameter();
                    paramIdChallenge.ParameterName = "@idchallenge";
                    paramIdChallenge.Value = challenge.Id;
                    comm.Parameters.Add(paramIdChallenge);

                    comm.ExecuteNonQuery();
                }
            }
        }
        
        public int CountByChallenge(Challenge challenge)
        {
            log.InfoFormat("Count participants by challenge {0}", challenge);
            IDbConnection connection = DBUtils.getConnection(props);
            using (var comm = connection.CreateCommand())
            {
                comm.CommandText = "select count(*) as count from registration where idchallenge = @idchallenge";
                var idChallengeParam = comm.CreateParameter();
                idChallengeParam.ParameterName = "@idchallenge";
                idChallengeParam.Value = challenge.Id;
                comm.Parameters.Add(idChallengeParam);
                
                using (var dataR = comm.ExecuteReader())
                {
                    while (dataR.Read())
                    {
                        int count = dataR.GetInt16(0);
                        log.InfoFormat("Count {0} participants by challenge {1}", count, challenge);
                        return count;
                    }
                }
            }
            log.InfoFormat("Count 0 participants by challenge {0}", challenge);
            return 0;
        }
        
        private Participant FindParticipant(long along)
        {
            log.InfoFormat("Finding participant with id {0}", along);
            IDbConnection connection = DBUtils.getConnection(props);
            using (var comm = connection.CreateCommand())
            {
                comm.CommandText = "select * from participant where id = @id";
                IDbDataParameter paramId = comm.CreateParameter();
                paramId.ParameterName = "@id";
                paramId.Value = along;
                comm.Parameters.Add(paramId);

                using (var dataR = comm.ExecuteReader())
                {
                    if (dataR.Read())
                    {
                        long id = dataR.GetInt32(0);
                        String name = dataR.GetString(1);
                        int age = dataR.GetInt16(2);
                        Participant participant = new Participant(name, age);
                        participant.Id = id;
                        return participant;
                    }
                }
            }
            log.InfoFormat("Finding NO participant with id {0}", along);
            return null;
        }
        
        private Challenge FindChallenge(long along)
        {  
            log.InfoFormat("Finding a challenge with id {0}", along);
            IDbConnection connection = DBUtils.getConnection(props);
            using (var comm = connection.CreateCommand())
            {
                comm.CommandText = "select * from challenge where id = @id";
                IDbDataParameter paramId = comm.CreateParameter();
                paramId.ParameterName = "@id";
                paramId.Value = along;
                comm.Parameters.Add(paramId);

                using (var dataR = comm.ExecuteReader())
                {
                    if (dataR.Read())
                    {
                        long id = dataR.GetInt32(0);
                        Style style = (Style)Enum.Parse(typeof(Style), dataR.GetString(1));
                        Distance distance = (Distance)dataR.GetInt16(2);
                        Challenge challenge = new Challenge(distance, style);
                        challenge.Id = id;
                        return challenge;
                    }
                }
            }
            log.InfoFormat("Finding NO challenge with id {0}", along);
            return null;
        }
        
        public Registration FindOne(long along)
        {
            log.InfoFormat("Finding registration with id {0}", along);
            IDbConnection connection = DBUtils.getConnection(props);
            using (var comm = connection.CreateCommand())
            {
                comm.CommandText = "select * from registration where id = @id";
                IDbDataParameter paramId = comm.CreateParameter();
                paramId.ParameterName = "@id";
                paramId.Value = along;
                comm.Parameters.Add(paramId);

                using (var dataR = comm.ExecuteReader())
                {
                    if (dataR.Read())
                    {
                        long id = dataR.GetInt32(0);
                        Participant participant = this.FindParticipant(dataR.GetInt32(1));
                        Challenge challenge = this.FindChallenge(dataR.GetInt32(2));
                        Registration registration = new Registration(participant, challenge);
                        registration.Id = id;
                        return registration;
                    }
                }
            }
            log.InfoFormat("Finding NO registration with id {0}", along);
            return null;
        }

        public IEnumerable<Registration> FindAll()
        {
            log.Info("Finding all registrations");
            IDbConnection connection = DBUtils.getConnection(props);
            IList<Registration> registrations = new List<Registration>();
            using (var comm = connection.CreateCommand())
            {
                comm.CommandText = "select * from registration";
                using (var dataR = comm.ExecuteReader())
                {
                    while (dataR.Read())
                    {
                        long id = dataR.GetInt32(0);
                        Participant participant = this.FindParticipant(dataR.GetInt32(1));
                        Challenge challenge = this.FindChallenge(dataR.GetInt32(2));
                        Registration registration = new Registration(participant, challenge);
                        registration.Id = id;
                        registrations.Add(registration);
                    }
                }
            }
            log.Info(registrations);
            return registrations;
        }

        public Registration Save(Registration entity)
        {
            log.InfoFormat("Saving registration {0}", entity);
            var connection = DBUtils.getConnection(props);
            using (var comm = connection.CreateCommand())
            {
                comm.CommandText = "insert into registration(idparticipant, idchallenge) values(@idparticipant,@idchallenge) RETURNING id";
                var paramParticipant = comm.CreateParameter();
                paramParticipant.ParameterName = "@idparticipant";
                paramParticipant.Value = entity.Participant.Id;
                comm.Parameters.Add(paramParticipant);
                
                var paramIdChallenge = comm.CreateParameter();
                paramIdChallenge.ParameterName = "@idchallenge";
                paramIdChallenge.Value = entity.Challenge.Id;
                comm.Parameters.Add(paramIdChallenge);

                long generatedId = (long)comm.ExecuteScalar();
                entity.Id = generatedId;
                return entity;
            }
        }

        public void Delete(long id)
        {
            log.InfoFormat("Deleting registration with id {0}", id);
            IDbConnection connection = DBUtils.getConnection(props);
            using (var comm = connection.CreateCommand())
            {
                comm.CommandText = "delete from registration where id = @id";
                var idParam = comm.CreateParameter();
                idParam.ParameterName = "@id";
                idParam.Value = id;
                comm.Parameters.Add(idParam);
                var result = comm.ExecuteNonQuery();
                if(result == 1)
                    log.InfoFormat("Deleted registration with id {0}", id);
            }
        }

        public void Update(Registration entity)
        {
            //nu e nevoie
        }
    }
}