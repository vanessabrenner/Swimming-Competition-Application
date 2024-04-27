using System;
using System.Collections.Generic;
using System.IO;
using System.Net;
using System.Net.Sockets;
using System.Runtime.Serialization;
using System.Runtime.Serialization.Formatters.Binary;
using System.Threading;
using model;
using Newtonsoft.Json;
using services;

namespace networking
{
    public class SwimmingCompetitionServicesJsonProxy : ISwimmingCompetitionServices
    {
        private string host;
        private int port;

        private ISwimmingCompetitionObserver client;
        private NetworkStream networkStream;
        private IFormatter formatter;
        private TcpClient connection;
        
        private Queue<Response> qresponses;
        private EventWaitHandle _waitHandle;
        private volatile bool finished;

        public SwimmingCompetitionServicesJsonProxy(string host, int port)
        {
            this.host = host;
            this.port = port;
            qresponses = new Queue<Response>();
        }

        public virtual void login(Organizer user, ISwimmingCompetitionObserver client)
        {
            Request req= JsonProtocolUtils.createLoginRequest(user);
            sendRequest(req);
            Response response=readResponse();
            if (response.Type== ResponseType.OK){
                this.client=client;
                return;
            }
            if (response.Type== ResponseType.ERROR)
            {
                string err = response.ErrorMessage;
                throw new SwimmingCompetitionException(err);
            }
        }

        public virtual Organizer findAccount(string username, string password)
        {
            initializeConnection();
            Request request =JsonProtocolUtils.createFindingAccountRequest(username, password);
            sendRequest(request);
            Response response = readResponse();
            if(response.Type == ResponseType.FINDING_ACCOUNT){
                Organizer organizer = response.Account;
                //Console.WriteLine(organizer.ToString());
                return organizer;
            }
            if (response.Type== ResponseType.ERROR){
                string err=response.ErrorMessage;
                closeConnection();
                throw new SwimmingCompetitionException(err);
            }
            return null;
        }

        public virtual ChallengeDTO[] findAllChallenges()
        {
            Request request =JsonProtocolUtils.createGetAllChallengesRequest();
            sendRequest(request);
            Response response = readResponse();
            if(response.Type == ResponseType.GET_ALL_CHALLENGES){
                ChallengeDTO[] challenges = response.AllChallenges;
                //Console.WriteLine(challenges);
                return challenges;
            }
            if (response.Type== ResponseType.ERROR)
            {
                string err = response.ErrorMessage;
                throw new SwimmingCompetitionException(err);
            }
            return null;
        }

        public virtual IList<ParticipantDTO> findParticipantsByChallenge(Challenge challenge)
        {
            Request request =JsonProtocolUtils.createFindingParticipantByChallengeRequest(challenge);
            sendRequest(request);
            Response response = readResponse();
            if(response.Type == ResponseType.FINDING_ALL_PARTICIPANTS_BY_CHALLENGE){
                IList<ParticipantDTO> participants = response.ParicipantsByChallenge;
                //Console.WriteLine(participants);
                return participants;
            }
            if (response.Type== ResponseType.ERROR){
                string err=response.ErrorMessage;
                throw new SwimmingCompetitionException(err);
            }

            return new List<ParticipantDTO>();
        }

        public virtual Participant addParticipant(string name, int age)
        {
            Request request =JsonProtocolUtils.createAddParticipantRequest(name, age);
            sendRequest(request);
            Response response = readResponse();
            if(response.Type == ResponseType.ADD_PARTICIPANT){
                Participant participant = response.AddedParticipant;
                //Console.WriteLine(participant);
                return participant;
            }
            if (response.Type== ResponseType.ERROR){
                string err=response.ErrorMessage;
                throw new SwimmingCompetitionException(err);
            }
            return null;
        }

        public virtual void addParticipantToMoreChallenges(Participant participant, List<Challenge> challenges)
        {
            Request request =JsonProtocolUtils.createAddParticipantToMoreChallengesRequest(participant, challenges);
            sendRequest(request);
            Response response = readResponse();
            if(response.Type == ResponseType.OK){
                //Console.WriteLine("Added participant " + participant + " to " + challenges);
            }
            if (response.Type== ResponseType.ERROR){
                string err=response.ErrorMessage;
                throw new SwimmingCompetitionException(err);
            }
        }

        public virtual void logout(Organizer user, ISwimmingCompetitionObserver client)
        {
            Request req=JsonProtocolUtils.createLogoutRequest(user);
            sendRequest(req);
            Response response=readResponse();
            closeConnection();
            if (response.Type== ResponseType.ERROR){
                String err=response.ErrorMessage;//data().toString();
                throw new SwimmingCompetitionException(err);
            }
        }

        private void closeConnection()
        {
            finished = true;
            try
            {
                networkStream.Close();
                connection.Close();
                _waitHandle.Close();
                client = null;
            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
            }
        }

        private void startReader()
        {
            Thread tw = new Thread(run);
            tw.Start();
        }

        private void sendRequest(Request request)
        {
            try
            {
                formatter.Serialize(networkStream, request);
                networkStream.Flush();
            }
            catch (Exception e)
            {
                throw new SwimmingCompetitionException("Error sending object " + e);
            }
        }

        private Response readResponse() {
            Response response= null;
            try{
                _waitHandle.WaitOne();
                lock(qresponses){
                    response = qresponses.Dequeue();
                }
            }
            catch (Exception e) {
                Console.WriteLine(e.Message);
            }

            return response;
        }
        
        private void initializeConnection(){
            try {
                connection=new TcpClient(host,port);
                this.networkStream = connection.GetStream();
                formatter = new BinaryFormatter();
                finished=false;
                _waitHandle = new AutoResetEvent(false);
                startReader();
            } 
            catch (IOException e) {
                Console.WriteLine(e.Message);
            }
        }
        private void handleUpdate(Response response){
            if(response.Type== ResponseType.UPDATE_TABLES){
                Console.WriteLine("Updating participants");
                client.updateTables(response.AllChallenges);
            }
        }
        private bool isUpdate(Response response){
            return response.Type == ResponseType.UPDATE_TABLES;
        }
        public virtual void run()
        {
            while(!finished)
            {
                try
                {
                    object response = formatter.Deserialize(networkStream);
                    //Console.WriteLine("response received "+response);
                    if (isUpdate((Response)response))
                    {
                        handleUpdate((Response)response);
                    }
                    else
                    {
							
                        lock (qresponses)
                        {
                            qresponses.Enqueue((Response)response);
                        }
                        _waitHandle.Set();
                    }
                }
                catch (Exception e)
                {
                    Console.WriteLine("Reading error "+e);
                }
					
            }
        }
    }
}