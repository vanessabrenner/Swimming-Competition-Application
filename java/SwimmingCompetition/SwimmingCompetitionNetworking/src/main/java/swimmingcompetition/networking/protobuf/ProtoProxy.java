package swimmingcompetition.networking.protobuf;

import com.google.gson.Gson;
import swimmingcompetition.model.Challenge;
import swimmingcompetition.model.Organizer;
import swimmingcompetition.model.Participant;
import swimmingcompetition.model.dto.ChallengeDTO;
import swimmingcompetition.model.dto.ParticipantDTO;
import swimmingcompetition.networking.json.*;
import swimmingcompetition.services.ISwimmingCompetitionObserver;
import swimmingcompetition.services.ISwimmingCompetitionServices;
import swimmingcompetition.services.SwimmingCompetitionException;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static swimmingcompetition.networking.protobuf.SwimmingCompetitionProtobufs.Response.Type.*;

public class ProtoProxy implements ISwimmingCompetitionServices {
    private String host;
    private int port;

    private ISwimmingCompetitionObserver client;

    private InputStream input;
    private OutputStream output;
    private Socket connection;

    private BlockingQueue<SwimmingCompetitionProtobufs.Response> qresponses;
    private volatile boolean finished;
    public ProtoProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses=new LinkedBlockingQueue<SwimmingCompetitionProtobufs.Response>();
    }

    @Override
    public void login(Organizer organizer, ISwimmingCompetitionObserver client) throws SwimmingCompetitionException {
        //initializeConnection();

        SwimmingCompetitionProtobufs.Request req= ProtoUtils.createLoginRequest(organizer);
        sendRequest(req);
        SwimmingCompetitionProtobufs.Response response=readResponse();
        if (response.getType()== OK){
            this.client=client;
            return;
        }
        if (response.getType()== ERROR){
            String err=ProtoUtils.getError(response);
            //closeConnection();
            throw new SwimmingCompetitionException(err);
        }
    }

    @Override
    public Organizer findAccount(String username, String password) throws SwimmingCompetitionException{
        initializeConnection();
        SwimmingCompetitionProtobufs.Request request =ProtoUtils.createFindingAccountRequest(username, password);
        sendRequest(request);
        SwimmingCompetitionProtobufs.Response response = readResponse();
        if(response.getType() == FINDING_ACCOUNT){
            Organizer organizer = ProtoUtils.getAccountOrganizer(response);
            //System.out.println(organizer.toString());
            return organizer;
        }
        if (response.getType()== ERROR){
            String err=ProtoUtils.getError(response);
            closeConnection();
            throw new SwimmingCompetitionException(err);
        }
        return null;
    }

    @Override
    public ChallengeDTO[] findAllChallenges() throws SwimmingCompetitionException{
        //initializeConnection();
        SwimmingCompetitionProtobufs.Request request =ProtoUtils.createGetAllChallengesRequest();
        sendRequest(request);
        SwimmingCompetitionProtobufs.Response response = readResponse();
        if(response.getType() == GET_ALL_CHALLENGES){
            ChallengeDTO[] challenges = ProtoUtils.getAllChallenges(response);
            //System.out.println(challenges);
            return challenges;
        }
        if (response.getType()== ERROR){
            String err=ProtoUtils.getError(response);
            //closeConnection();
            throw new SwimmingCompetitionException(err);
        }
        return null;
    }

    @Override
    public List<ParticipantDTO> findParticipantsByChallenge(Challenge challenge) throws SwimmingCompetitionException{
        //initializeConnection();
        SwimmingCompetitionProtobufs.Request request =ProtoUtils.createFindingParticipantByChallengeRequest(challenge);
        sendRequest(request);
        SwimmingCompetitionProtobufs.Response response = readResponse();
        if(response.getType() == FINDING_ALL_PARTICIPANTS_BY_CHALLENGE){
            List<ParticipantDTO> participants = ProtoUtils.getParicipantsByChallenge(response);
            //System.out.println(participants);
            return participants;
        }
        if (response.getType()== ERROR){
            String err=ProtoUtils.getError(response);
            //closeConnection();
            throw new SwimmingCompetitionException(err);
        }
        return new ArrayList<>();
    }

    @Override
    public Participant addParticipant(String name, int age) throws SwimmingCompetitionException{
        // initializeConnection();
        SwimmingCompetitionProtobufs.Request request =ProtoUtils.createAddParticipantRequest(name, age);
        sendRequest(request);
        SwimmingCompetitionProtobufs.Response response = readResponse();
        if(response.getType() == ADD_PARTICIPANT){
            Participant participant = ProtoUtils.getAddedParticipant(response);
            //System.out.println(participant);
            return participant;
        }
        if (response.getType()== ERROR){
            String err=ProtoUtils.getError(response);
            //closeConnection();
            throw new SwimmingCompetitionException(err);
        }
        return null;
    }

    @Override
    public void addParticipantToMoreChallenges(Participant participant, List<Challenge> challenges) throws SwimmingCompetitionException{
        //initializeConnection();
        SwimmingCompetitionProtobufs.Request request =ProtoUtils.createAddParticipantToMoreChallengesRequest(participant, challenges);
        sendRequest(request);
        SwimmingCompetitionProtobufs.Response response = readResponse();
        if(response.getType() == OK){
            //System.out.println("Added participant " + participant + " to " + challenges);
        }
        if (response.getType()== ERROR){
            String err=ProtoUtils.getError(response);
            //closeConnection();
            throw new SwimmingCompetitionException(err);
        }
    }
    @Override
    public void logout(Organizer organizer, ISwimmingCompetitionObserver client) throws SwimmingCompetitionException {

        SwimmingCompetitionProtobufs.Request req=ProtoUtils.createLogoutRequest(organizer);
        sendRequest(req);
        SwimmingCompetitionProtobufs.Response response=readResponse();
        closeConnection();
        if (response.getType()== ERROR){
            String err=ProtoUtils.getError(response);
            throw new SwimmingCompetitionException(err);
        }
    }

    private void closeConnection() {
        finished = true;
        try {
            input.close();
            output.close();
            connection.close();
            client = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendRequest(SwimmingCompetitionProtobufs.Request request) throws SwimmingCompetitionException {
        try {
            request.writeDelimitedTo(output);
            output.flush();
        } catch (Exception e) {
            throw new SwimmingCompetitionException("Error sending object "+e);
        }
    }

    private SwimmingCompetitionProtobufs.Response readResponse() throws SwimmingCompetitionException {
        SwimmingCompetitionProtobufs.Response response=null;
        try{

            response=qresponses.take();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    private void initializeConnection() throws SwimmingCompetitionException {
        try {
            connection=new Socket(host,port);
            output=connection.getOutputStream();
            //output.flush();
            input=connection.getInputStream();
            finished=false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startReader(){
        Thread tw=new Thread(new ReaderThread());
        tw.start();
    }

    private void handleUpdate(SwimmingCompetitionProtobufs.Response response){
        if(response.getType()== UPDATE_TABLES){
            System.out.println("Updating tables");
            // TO DO
            ChallengeDTO[] challenges = ProtoUtils.getAllChallenges(response);
            client.updateTables(challenges);
        }
    }

    private boolean isUpdate(SwimmingCompetitionProtobufs.Response response){
        return response.getType() == UPDATE_TABLES;
    }

    private class ReaderThread implements Runnable{
        public void run() {
            while(!finished){
                try {
                    SwimmingCompetitionProtobufs.Response response=SwimmingCompetitionProtobufs.Response.parseDelimitedFrom(input);
                    //System.out.println("response received "+responseLine);
                    if (isUpdate(response)){
                        handleUpdate(response);
                    }else{

                        try {
                            qresponses.put(response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error "+e);
                }
            }
        }
    }

}
