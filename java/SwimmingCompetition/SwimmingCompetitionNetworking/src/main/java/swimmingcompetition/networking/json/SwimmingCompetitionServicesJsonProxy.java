package swimmingcompetition.networking.json;

import com.google.gson.Gson;
import swimmingcompetition.model.Challenge;
import swimmingcompetition.model.Organizer;
import swimmingcompetition.model.Participant;
import swimmingcompetition.model.Registration;
import swimmingcompetition.model.dto.ChallengeDTO;
import swimmingcompetition.model.dto.ParticipantDTO;
import swimmingcompetition.services.ISwimmingCompetitionObserver;
import swimmingcompetition.services.ISwimmingCompetitionServices;
import swimmingcompetition.services.SwimmingCompetitionException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SwimmingCompetitionServicesJsonProxy implements ISwimmingCompetitionServices {
    private String host;
    private int port;

    private ISwimmingCompetitionObserver client;

    private BufferedReader input;
    private PrintWriter output;
    private Gson gsonFormatter;
    private Socket connection;

    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;
    public SwimmingCompetitionServicesJsonProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses=new LinkedBlockingQueue<Response>();
    }

    @Override
    public void login(Organizer user, ISwimmingCompetitionObserver client) throws SwimmingCompetitionException {
        //initializeConnection();

        Request req= JsonProtocolUtils.createLoginRequest(user);
        sendRequest(req);
        Response response=readResponse();
        if (response.getType()== ResponseType.OK){
            this.client=client;
            return;
        }
        if (response.getType()== ResponseType.ERROR){
            String err=response.getErrorMessage();;
            //closeConnection();
            throw new SwimmingCompetitionException(err);
        }
    }

    @Override
    public Organizer findAccount(String username, String password) throws SwimmingCompetitionException{
        initializeConnection();
        Request request =JsonProtocolUtils.createFindingAccountRequest(username, password);
        sendRequest(request);
        Response response = readResponse();
        if(response.getType() == ResponseType.FINDING_ACCOUNT){
            Organizer organizer = response.getAccount();
            //System.out.println(organizer.toString());
            return organizer;
        }
        if (response.getType()== ResponseType.ERROR){
            String err=response.getErrorMessage();;
            closeConnection();
            throw new SwimmingCompetitionException(err);
        }
        return null;
    }

    @Override
    public ChallengeDTO[] findAllChallenges() throws SwimmingCompetitionException{
        //initializeConnection();
        Request request =JsonProtocolUtils.createGetAllChallengesRequest();
        sendRequest(request);
        Response response = readResponse();
        if(response.getType() == ResponseType.GET_ALL_CHALLENGES){
            ChallengeDTO[] challenges = response.getAllChallenges();
            //System.out.println(challenges);
            return challenges;
        }
        if (response.getType()== ResponseType.ERROR){
            String err=response.getErrorMessage();;
            //closeConnection();
            throw new SwimmingCompetitionException(err);
        }
        return null;
    }

    @Override
    public List<ParticipantDTO> findParticipantsByChallenge(Challenge challenge) throws SwimmingCompetitionException{
        //initializeConnection();
        Request request =JsonProtocolUtils.createFindingParticipantByChallengeRequest(challenge);
        sendRequest(request);
        Response response = readResponse();
        if(response.getType() == ResponseType.FINDING_ALL_PARTICIPANTS_BY_CHALLENGE){
            List<ParticipantDTO> participants = response.getParicipantsByChallenge();
            //System.out.println(participants);
            return participants;
        }
        if (response.getType()== ResponseType.ERROR){
            String err=response.getErrorMessage();;
            //closeConnection();
            throw new SwimmingCompetitionException(err);
        }
        return new ArrayList<>();
    }

    @Override
    public Participant addParticipant(String name, int age) throws SwimmingCompetitionException{
       // initializeConnection();
        Request request =JsonProtocolUtils.createAddParticipantRequest(name, age);
        sendRequest(request);
        Response response = readResponse();
        if(response.getType() == ResponseType.ADD_PARTICIPANT){
            Participant participant = response.getAddedParticipant();
            //System.out.println(participant);
            return participant;
        }
        if (response.getType()== ResponseType.ERROR){
            String err=response.getErrorMessage();;
            //closeConnection();
            throw new SwimmingCompetitionException(err);
        }
        return null;
    }

    @Override
    public void addParticipantToMoreChallenges(Participant participant, List<Challenge> challenges) throws SwimmingCompetitionException{
        //initializeConnection();
        Request request =JsonProtocolUtils.createAddParticipantToMoreChallengesRequest(participant, challenges);
        sendRequest(request);
        Response response = readResponse();
        if(response.getType() == ResponseType.OK){
            //System.out.println("Added participant " + participant + " to " + challenges);
        }
        if (response.getType()== ResponseType.ERROR){
            String err=response.getErrorMessage();;
            //closeConnection();
            throw new SwimmingCompetitionException(err);
        }
    }

    private void closeConnection() {
        finished=true;
        try {
            input.close();
            output.close();
            connection.close();
            client=null;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void logout(Organizer user, ISwimmingCompetitionObserver client) throws SwimmingCompetitionException {

        Request req=JsonProtocolUtils.createLogoutRequest(user);
        sendRequest(req);
        Response response=readResponse();
        closeConnection();
        if (response.getType()== ResponseType.ERROR){
            String err=response.getErrorMessage();//data().toString();
            throw new SwimmingCompetitionException(err);
        }
    }

    private void sendRequest(Request request)throws SwimmingCompetitionException {
        String reqLine=gsonFormatter.toJson(request);
        try {
            output.println(reqLine);
            output.flush();
        } catch (Exception e) {
            throw new SwimmingCompetitionException("Error sending object "+e);
        }

    }

    private Response readResponse() throws SwimmingCompetitionException {
       Response response=null;
        try{

            response=qresponses.take();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }
    private void initializeConnection() throws SwimmingCompetitionException {
        try {
            gsonFormatter= new Gson();
            connection=new Socket(host,port);
            output=new PrintWriter(connection.getOutputStream());
            output.flush();
            input=new BufferedReader(new InputStreamReader(connection.getInputStream()));
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


    private void handleUpdate(Response response){
        if(response.getType()== ResponseType.UPDATE_TABLES){
            System.out.println("Updating tables");
            ChallengeDTO[] challenges = response.getAllChallenges();
            client.updateTables(challenges);
        }
    }

    private boolean isUpdate(Response response){
        return response.getType() == ResponseType.UPDATE_TABLES;
    }
    private class ReaderThread implements Runnable{
        public void run() {
            while(!finished){
                try {
                    String responseLine=input.readLine();
                    //System.out.println("response received "+responseLine);
                    Response response=gsonFormatter.fromJson(responseLine, Response.class);
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
