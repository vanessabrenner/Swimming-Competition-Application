using System;
using System.Collections.Generic;
using System.Configuration;
using System.Net.Sockets;
using System.Threading;
using networking;
using persistence;
using protobuf;
using services;

namespace Server
{
    public class StartServer
    {
        private static int DEFAULT_PORT=55556;
        private static String DEFAULT_IP="127.0.0.1";
        
        static string GetConnectionStringByName(string name)
        {
            // Assume failure.
            string returnValue = null;

            // Look for the name in the connectionStrings section.
            ConnectionStringSettings settings =ConfigurationManager.ConnectionStrings[name];

            // If found, return the connection string.
            if (settings != null)
                returnValue = settings.ConnectionString;

            return returnValue;
        }
        
        static void Main(string[] args)
        {
            // IUserRepository userRepo = new UserRepositoryMock();
            Console.WriteLine("Reading properties from app.config ...");
            int port = DEFAULT_PORT;
            String ip = DEFAULT_IP;
            String portS= ConfigurationManager.AppSettings["port"];
            if (portS == null)
            {
                Console.WriteLine("Port property not set. Using default value "+DEFAULT_PORT);
            }
            else
            {
                bool result = Int32.TryParse(portS, out port);
                if (!result)
                {
                    Console.WriteLine("Port property not a number. Using default value "+DEFAULT_PORT);
                    port = DEFAULT_PORT;
                    Console.WriteLine("Portul "+port);
                }
            }
            String ipS=ConfigurationManager.AppSettings["ip"];
           
            if (ipS == null)
            {
                Console.WriteLine("Port property not set. Using default value "+DEFAULT_IP);
            }
            Console.WriteLine("Configuration Settings for database {0}",GetConnectionStringByName("SwimmingCompetitionDB"));
            IDictionary<String, string> props = new SortedList<String, String>();
            props.Add("ConnectionString", GetConnectionStringByName("SwimmingCompetitionDB"));
            
            // cream repo urile
            OrganizerDBRepository organizerDbRepository = new OrganizerDBRepository(props);
            ChallengeDBRepository challengeDbRepository = new ChallengeDBRepository(props);
            ParticipantDBRepository participantDbRepository = new ParticipantDBRepository(props);
            RegistrationDBRepository registrationDbRepository = new RegistrationDBRepository(props);

            ISwimmingCompetitionServices serviceImpl = new SwimmingCompetitionServicesImpl(challengeDbRepository,
                organizerDbRepository, participantDbRepository, registrationDbRepository);

            //AbstractServer server = new SwimmingCompetitionJsonConcurrentServer(ip, port, serviceImpl);
            AbstractServer server = new ProtoV3ChatServer(ip, port, serviceImpl);
            server.Start();
            Console.WriteLine("Server started...");
        }
        
    }
    public class ProtoV3ChatServer : AbsConcurrentServer
    {
        private ISwimmingCompetitionServices server;
        private ProtoWorker worker;
        public ProtoV3ChatServer(string host, int port, ISwimmingCompetitionServices server)
            : base(host, port)
        {
            this.server = server;
            Console.WriteLine("ProtoChatServer...");
        }
        protected override Thread createWorker(TcpClient client)
        {
            worker = new ProtoWorker(server, client);
            return new Thread(new ThreadStart(worker.run));
        }
    }
}