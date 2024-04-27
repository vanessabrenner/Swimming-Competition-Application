using System;
using System.Collections.Generic;
using System.IO;
using System.Net.Sockets;
using System.Threading;
using Google.Protobuf;
using services;

namespace protobuf
{
    public class ProtoWorker:ISwimmingCompetitionObserver
    {
        private ISwimmingCompetitionServices server;
        private TcpClient connection;

        private NetworkStream stream;
        private volatile bool connected;

        public ProtoWorker(ISwimmingCompetitionServices server, TcpClient connection)
        {
            this.server = server;
            this.connection = connection;
            try
            {
				
                stream=connection.GetStream();
                connected=true;
            }
            catch (Exception e)
            {
                Console.WriteLine(e.StackTrace);
            }
        }
        public virtual void run()
        {
            while(connected)
            {
                try
                {
					
                    Request request = Request.Parser.ParseDelimitedFrom(stream);
                    Response response =handleRequest(request);
                    if (response!=null)
                    {
                        sendResponse(response);
                    }
                }
                catch (Exception e)
                {
                    Console.WriteLine(e.StackTrace);
                }
				
                try
                {
                    Thread.Sleep(1000);
                }
                catch (Exception e)
                {
                    Console.WriteLine(e.StackTrace);
                }
            }
            try
            {
                stream.Close();
                connection.Close();
            }
            catch (Exception e)
            {
                Console.WriteLine("Error "+e);
            }
        }
        private void sendResponse(Response response)
        {
            Console.WriteLine("sending response "+response);
            lock (stream)
            {
                response.WriteDelimitedTo(stream);
                stream.Flush();
            }

        }

        private Response handleRequest(Request request)
        {
            Response response = null;
            Request.Types.Type type = request.Type;
            switch (type)
            {
                case Request.Types.Type.FindingAccount:
                {
                    string username = request.Username;
                    string password = request.Password;
                    try
                    {   
                        lock(server){
                            model.Organizer organizer = server.findAccount(username, password);
                            return ProtoUtils.createFindingAccountResponse(organizer);
                        }
                    }
                    catch (SwimmingCompetitionException e)
                    {
                        connected = false;
                        return ProtoUtils.createErrorResponse(e.Message);
                    }
                }
                case Request.Types.Type.Login:
                {
                    model.Organizer organizer = ProtoUtils.getOrganizer(request);
                    try {
                        lock (server)
                        {
                            server.login(organizer, this);
                            return ProtoUtils.createOkResponse();
                        }
                    } catch (SwimmingCompetitionException e) {
                        connected=false;
                        return ProtoUtils.createErrorResponse(e.Message);
                    }
                }
                case Request.Types.Type.GetAllChallenges:
                {
                    try {
                        lock (server)
                        {
                            model.ChallengeDTO[] challenges = server.findAllChallenges();
                            return ProtoUtils.createGetAllChallengesResponse(challenges);
                        }
                    } catch (SwimmingCompetitionException e) {
                        connected = false;
                        return ProtoUtils.createErrorResponse(e.Message);
                    }
                }
                case Request.Types.Type.FindingAllParticipantsByChallenge:
                {
                    model.Challenge challenge = ProtoUtils.getChallenge(request);
                    try {
                        lock (server)
                        {
                            IList<model.ParticipantDTO> participantList = server.findParticipantsByChallenge(challenge);
                            return ProtoUtils.createFindingParticipantByChallengeResponse(participantList);
                        }
                    } catch (SwimmingCompetitionException e) {
                        connected = false;
                        return ProtoUtils.createErrorResponse(e.Message);
                    }
                }
                case Request.Types.Type.AddParticipant:
                {
                    string name = request.Name;
                    int age = request.Age;
                    try {
                        lock (server)
                        {
                            model.Participant participant = server.addParticipant(name, age);
                            return ProtoUtils.createAddParticipantResponse(participant);
                        }
                    } catch (SwimmingCompetitionException e) {
                        connected = false;
                        return ProtoUtils.createErrorResponse(e.Message);
                    }
                }
                case Request.Types.Type.AddParticipantToMoreChallenges:
                {
                    model.Participant participant = ProtoUtils.getParticipant(request);
                    List<model.Challenge> challenges = ProtoUtils.getAllChallenges(request);
                    try {
                        lock (server)
                        {
                            server.addParticipantToMoreChallenges(participant, challenges);
                            return ProtoUtils.createOkResponse();
                        }
                    } catch (SwimmingCompetitionException e) {
                        connected = false;
                        return ProtoUtils.createErrorResponse(e.Message);
                    }
                }
                case Request.Types.Type.Logout:
                {
                    model.Organizer organizer = ProtoUtils.getOrganizer(request);
                    try {
                        lock (server)
                        {
                            server.logout(organizer, this);
                            connected = false;
                            return ProtoUtils.createOkResponse();
                        }

                    } catch (SwimmingCompetitionException e) {
                        return ProtoUtils.createErrorResponse(e.Message);
                    }
                }
            }

            return response;
        }
        public virtual void updateTables(model.ChallengeDTO[] challenges)
        {
            Response resp = ProtoUtils.createUpdateParticipantsResponse(challenges);
            //Console.WriteLine("Update participants response");
            try
            {
                sendResponse(resp);
            }
            catch (IOException e)
            {
                Console.WriteLine("Error " + e);
            }
        }
    }
}