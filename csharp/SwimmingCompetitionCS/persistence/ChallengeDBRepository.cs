using System;
using System.Collections.Generic;
using System.Data;
using log4net;
using model;

namespace persistence
{   
    public class ChallengeDBRepository : ChallengeRepository
    {
        private static readonly ILog log = LogManager.GetLogger(typeof(ChallengeDBRepository));

        IDictionary<String, string> props;

        public ChallengeDBRepository(IDictionary<string, string> props)
        {
            this.props = props;
        }

        public Challenge FindOne(long along)
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

        public IEnumerable<Challenge> FindAll()
        {
            log.InfoFormat("Finding all challenges");
            IDbConnection connection = DBUtils.getConnection(props);
            IList<Challenge> challenges = new List<Challenge>();
            using (var comm = connection.CreateCommand())
            {
                comm.CommandText = "select * from challenge";
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

        public Challenge Save(Challenge entity)
        {
            log.InfoFormat("Save a challenge {0}", entity);
            var connection = DBUtils.getConnection(props);
            using (var comm = connection.CreateCommand())
            {
                comm.CommandText = "insert into challenge(distance, style) values(@distance,@style) RETURNING id";
                var paramDistance = comm.CreateParameter();
                paramDistance.ParameterName = "@distance";
                paramDistance.Value = (int)entity.Distance;
                comm.Parameters.Add(paramDistance);
                
                var paramStyle = comm.CreateParameter();
                paramStyle.ParameterName = "@style";
                paramStyle.Value = entity.Style.ToString();
                comm.Parameters.Add(paramStyle);

                long generatedId = (long)comm.ExecuteScalar();
                entity.Id = generatedId;

                return entity;
            }
        }

        public void Delete(long id)
        {
            log.InfoFormat("Delete challenge with id {0}", id);
            IDbConnection connection = DBUtils.getConnection(props);
            using (var comm = connection.CreateCommand())
            {
                comm.CommandText = "delete from challenge where id = @id";
                var idParam = comm.CreateParameter();
                idParam.ParameterName = "@id";
                idParam.Value = id;
                comm.Parameters.Add(idParam);
                var result = comm.ExecuteNonQuery();
                if(result == 1)
                    log.InfoFormat("Deleted challenge with id {0}", id);
            }
            
        }

        public void Update(Challenge entity)
        {
            // nu e nevoie
        }
    }
}