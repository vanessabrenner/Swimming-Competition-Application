using System;
using System.Collections.Generic;
using System.Data;
using log4net;
using model;

namespace persistence
{
    public class ParticipantDBRepository : ParticipantRepository
    {
        private static readonly ILog log = LogManager.GetLogger(typeof(ParticipantDBRepository));

        IDictionary<String, string> props;

        public ParticipantDBRepository(IDictionary<string, string> props)
        {
            this.props = props;
        }

        public Participant FindOne(long along)
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

        public IEnumerable<Participant> FindAll()
        {
            log.Info("Finding all participants");
            IDbConnection connection = DBUtils.getConnection(props);
            IList<Participant> participants = new List<Participant>();
            using (var comm = connection.CreateCommand())
            {
                comm.CommandText = "select * from participant";
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

        public Participant Save(Participant entity)
        {
            log.InfoFormat("Saving participant {0}", entity);
            var connection = DBUtils.getConnection(props);
            using (var comm = connection.CreateCommand())
            {
                comm.CommandText = "insert into participant(name, age) values(@name,@age) RETURNING id";
                var paramName = comm.CreateParameter();
                paramName.ParameterName = "@name";
                paramName.Value = entity.Name;
                comm.Parameters.Add(paramName);
                
                var paramAge = comm.CreateParameter();
                paramAge.ParameterName = "@age";
                paramAge.Value = entity.Age;
                comm.Parameters.Add(paramAge);
                
                long generatedID = (long)comm.ExecuteScalar();

                entity.Id = generatedID;

                return entity;
            }
        }

        public void Delete(long id)
        {
            log.InfoFormat("Deleting participant with id {0}", id);
            IDbConnection connection = DBUtils.getConnection(props);
            using (var comm = connection.CreateCommand())
            {
                comm.CommandText = "delete from participant where id = @id";
                var idParam = comm.CreateParameter();
                idParam.ParameterName = "@id";
                idParam.Value = id;
                comm.Parameters.Add(idParam);
                var result = comm.ExecuteNonQuery();
                if(result == 1)
                    log.InfoFormat("Deleted participant with id {0}", id);
            }
        }

        public void Update(Participant entity)
        {
            // nu e nevoie
        }
    }
}