using System;
using System.Collections;
using System.Collections.Generic;
using System.IO;
using System.Net.Sockets;
using System.Runtime.Serialization;
using System.Runtime.Serialization.Formatters.Binary;
using model;
using Newtonsoft.Json;
using services;

namespace networking
{
    public class SwimmingCompetitionClientJsonWorker: ISwimmingCompetitionObserver
    {
        private ISwimmingCompetitionServices server;
        private TcpClient connection;
        
        private NetworkStream networkStream;
        private IFormatter formatter;
        private volatile bool connected;

        public SwimmingCompetitionClientJsonWorker(ISwimmingCompetitionServices server, TcpClient connection)
        {
            this.server = server;
            this.connection = connection;
            try
            {
                networkStream = connection.GetStream();
                formatter = new BinaryFormatter();
                connected = true;
            }
            catch (IOException e)
            {
                Console.WriteLine("Error " + e);
            }
        }
        
        public virtual void run()
        {
            while (connected)
            {
                try
                {
                    object request = formatter.Deserialize(networkStream);
                    object response =handleRequest((Request)request);
                    if (response != null)
                    {
                        sendResponse((Response)response);
                    }
                }
                catch (IOException e)
                {
                    Console.WriteLine("Error " + e);
                }
                try
                {
                    System.Threading.Thread.Sleep(1000);
                }
                catch (Exception e)
                {
                    Console.WriteLine("Error " + e);
                }
            }
            try
            {
                networkStream.Close();
                connection.Close();
            }
            catch (IOException e)
            {
                Console.WriteLine("Error " + e);
            }
        }
        
        private static Response okResponse = JsonProtocolUtils.createOkResponse();
        private Response handleRequest(Request request)
        {
            Response response = null;
            if (request.Type == RequestType.FINDING_ACCOUNT)
            {
                //Console.WriteLine("Finding account request ..." + request.Type);
                string username = request.Username;
                string password = request.Password;
                try
                {   
                    lock(server){
                        Organizer organizer = server.findAccount(username, password);
                        return JsonProtocolUtils.createFindingAccountResponse(organizer);
                    }
                }
                catch (SwimmingCompetitionException e)
                {
                    connected = false;
                    return JsonProtocolUtils.createErrorResponse(e.Message);
                }
            }
            if (request.Type == RequestType.GET_ALL_CHALLENGES) {
                //Console.WriteLine("Get all challenges request ..." + request.Type);
                try {
                    lock (server)
                    {
                        ChallengeDTO[] challenges = server.findAllChallenges();
                        return JsonProtocolUtils.createGetAllChallengesResponse(challenges);
                    }
                } catch (SwimmingCompetitionException e) {
                    connected = false;
                    return JsonProtocolUtils.createErrorResponse(e.Message);
                }
            }
            if (request.Type == RequestType.FINDING_ALL_PARTICIPANTS_BY_CHALLENGE) {
                //Console.WriteLine("Finding all participants by challenge request ..." + request.Type);
                Challenge challenge = request.Challenge;
                try {
                    lock (server)
                    {
                        IList<ParticipantDTO> participantList = server.findParticipantsByChallenge(challenge);
                        return JsonProtocolUtils.createFindingParticipantByChallengeResponse(participantList);
                    }
                } catch (SwimmingCompetitionException e) {
                    connected = false;
                    return JsonProtocolUtils.createErrorResponse(e.Message);
                }
            }
            if (request.Type == RequestType.ADD_PARTICIPANT) {
                //Console.WriteLine("Return added participant request ..." + request.Type);
                string name = request.NameParticipant;
                int age = request.AgeParticipant;
                try {
                    lock (server)
                    {
                        Participant participant = server.addParticipant(name, age);
                        return JsonProtocolUtils.createAddParticipantResponse(participant);
                    }
                } catch (SwimmingCompetitionException e) {
                    connected = false;
                    return JsonProtocolUtils.createErrorResponse(e.Message);
                }
            }
            if (request.Type == RequestType.ADD_PARTICIPANT_TO_MORE_CHALLENGES) {
                //Console.WriteLine("Add participant to more challenges request ..." + request.Type);
                Participant participant = request.Participant;
                List<Challenge> challenges = request.Challenges;
                try {
                    lock (server)
                    {
                        server.addParticipantToMoreChallenges(participant, challenges);
                        return okResponse;
                    }
                } catch (SwimmingCompetitionException e) {
                    connected = false;
                    return JsonProtocolUtils.createErrorResponse(e.Message);
                }
            }
            if (request.Type== RequestType.LOGOUT){
                //Console.WriteLine("Logout request ..."+request.Type);
                Organizer organizer = request.Organizer;
                try {
                    lock (server)
                    {
                        server.logout(organizer, this);
                        connected = false;
                        return okResponse;
                    }

                } catch (SwimmingCompetitionException e) {
                    return JsonProtocolUtils.createErrorResponse(e.Message);
                }
            }
            if (request.Type== RequestType.LOGIN){
                //Console.WriteLine("Login request ..."+request.Type);
                Organizer organizer = request.Organizer;
                try {
                    lock (server)
                    {
                        server.login(organizer, this);
                        return okResponse;
                    }
                } catch (SwimmingCompetitionException e) {
                    connected=false;
                    return JsonProtocolUtils.createErrorResponse(e.Message);
                }
            }
            return response;
        }
        
        private void sendResponse(Response response)
        {
            try
            {
                lock (networkStream)
                {
                    formatter.Serialize(networkStream, response);
                    networkStream.Flush();
                }
            }
            catch (Exception e)
            {
                throw new SwimmingCompetitionException("Error sending object " + e);
            }
        }
        
        public virtual void updateTables(ChallengeDTO[] challenges)
        {
            Response resp = JsonProtocolUtils.createUpdateParticipantsResponse(challenges);
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