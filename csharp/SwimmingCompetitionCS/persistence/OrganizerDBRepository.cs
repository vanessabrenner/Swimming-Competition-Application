using System;
using System.Collections.Generic;
using System.Data;
using log4net;
using model;

namespace persistence
{
    public class OrganizerDBRepository : OrganizerRepository
    {
        private static readonly ILog log = LogManager.GetLogger(typeof(OrganizerDBRepository));

        IDictionary<String, string> props;

        public OrganizerDBRepository(IDictionary<string, string> props)
        {
            this.props = props;
        }

        public Organizer FindOne(long along)
        {
            log.InfoFormat("Finding organizer with id {0}", along);
            IDbConnection connection = DBUtils.getConnection(props);
            using (var comm = connection.CreateCommand())
            {
                comm.CommandText = "select * from organizer where id = @id";
                IDbDataParameter paramId = comm.CreateParameter();
                paramId.ParameterName = "@id";
                paramId.Value = along;
                comm.Parameters.Add(paramId);

                using (var dataR = comm.ExecuteReader())
                {
                    if (dataR.Read())
                    {
                        long id = dataR.GetInt32(0);
                        string username = dataR.GetString(1);
                        string password = dataR.GetString(2);
                        Organizer organizer = new Organizer(username, password);
                        organizer.Id = id;
                        return organizer;
                    }
                }
            }
            log.InfoFormat("Finding NO organizer with id {0}", along);
            return null;
        }

        public IEnumerable<Organizer> FindAll()
        {
            log.Info("Finding all organizers");
            IDbConnection connection = DBUtils.getConnection(props);
            IList<Organizer> organizers = new List<Organizer>();
            using (var comm = connection.CreateCommand())
            {
                comm.CommandText = "select * from organizer";
                using (var dataR = comm.ExecuteReader())
                {
                    while (dataR.Read())
                    {
                        long id = dataR.GetInt32(0);
                        string username = dataR.GetString(1);
                        string password = dataR.GetString(2);
                        Organizer organizer = new Organizer(username, password);
                        organizer.Id = id;
                        organizers.Add(organizer);
                    }
                }
            }
            log.Info(organizers);
            return organizers;
        }

        public Organizer Save(Organizer entity)
        {
            log.InfoFormat("Saving organizer {0}", entity);
            var connection = DBUtils.getConnection(props);
            using (var comm = connection.CreateCommand())
            {
                comm.CommandText = "insert into organizer(username, password) values(@username,@password) RETURNING id";
                var paramUsername = comm.CreateParameter();
                paramUsername.ParameterName = "@username";
                paramUsername.Value = entity.Username;
                comm.Parameters.Add(paramUsername);
                
                var paramPassword = comm.CreateParameter();
                paramPassword.ParameterName = "@password";
                paramPassword.Value = entity.Password.ToString();
                comm.Parameters.Add(paramPassword);

                long generatedId = (long)comm.ExecuteScalar();
                entity.Id = generatedId;
                return entity;
            }
        }

        public void Delete(long id)
        {
            log.InfoFormat("Deleting organizer with id {0}", id);
            IDbConnection connection = DBUtils.getConnection(props);
            using (var comm = connection.CreateCommand())
            {
                comm.CommandText = "delete from organizer where id = @id";
                var idParam = comm.CreateParameter();
                idParam.ParameterName = "@id";
                idParam.Value = id;
                comm.Parameters.Add(idParam);
                var result = comm.ExecuteNonQuery();
                if(result == 1)
                    log.InfoFormat("Deleted organizer with id {0}", id);
            }
        }

        public void Update(Organizer entity)
        {
            // nu e nevoie
        }

        public Organizer FindAccount(string username, string password)
        {
            log.InfoFormat("Finding organizer with username {0} and password {1}", username, password);
            IDbConnection connection = DBUtils.getConnection(props);
            Organizer organizer = null;
            using (var comm = connection.CreateCommand())
            {
                comm.CommandText = "SELECT * FROM organizer WHERE username = @username AND password = @password";
                IDbDataParameter paramUsername = comm.CreateParameter();
                paramUsername.ParameterName = "@username";
                paramUsername.Value = username;
                comm.Parameters.Add(paramUsername);
                
                IDbDataParameter paramPassword = comm.CreateParameter();
                paramPassword.ParameterName = "@password";
                paramPassword.Value = password;
                comm.Parameters.Add(paramPassword);

                using (var dataR = comm.ExecuteReader())
                {
                    if (dataR.Read())
                    {
                        long id = dataR.GetInt32(0);
                        organizer = new Organizer(username, password);
                        organizer.Id = id;
                    }
                }
            }
            log.InfoFormat("Exit with {0}", organizer);
            return organizer;
        }
    }
}