package swimmingcompetition.networking.json;


import com.google.gson.Gson;

import swimmingcompetition.model.Challenge;
import swimmingcompetition.model.Organizer;
import swimmingcompetition.model.Participant;
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
import java.util.List;


public class SwimmingCompetitionClientJsonWorker implements Runnable, ISwimmingCompetitionObserver {
    private ISwimmingCompetitionServices server;
    private Socket connection;

    private BufferedReader input;
    private PrintWriter output;
    private Gson gsonFormatter;
    private volatile boolean connected;
    public SwimmingCompetitionClientJsonWorker(ISwimmingCompetitionServices server, Socket connection) {
        this.server = server;
        this.connection = connection;
        gsonFormatter=new Gson();
        try{
            output=new PrintWriter(connection.getOutputStream());
            input=new BufferedReader(new InputStreamReader(connection.getInputStream()));
            connected=true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while(connected){
            try {
                String requestLine=input.readLine();
                Request request=gsonFormatter.fromJson(requestLine, Request.class);
                Response response=handleRequest(request);
                if (response!=null){
                    sendResponse(response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error "+e);
        }
    }

    private static Response okResponse=JsonProtocolUtils.createOkResponse();

    private Response handleRequest(Request request) {
        Response response = null;
        if (request.getType() == RequestType.FINDING_ACCOUNT) {
            //System.out.println("Finding account request ..." + request.getType());
            String username = request.getUsername();
            String password = request.getPassword();
            try {
                Organizer organizer = server.findAccount(username, password);
                return JsonProtocolUtils.createFindingAccountResponse(organizer);
            } catch (SwimmingCompetitionException e) {
                connected = false;
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }

        if (request.getType() == RequestType.GET_ALL_CHALLENGES) {
            //System.out.println("Get all challenges request ..." + request.getType());
            try {
                ChallengeDTO[] challenges = server.findAllChallenges();
                return JsonProtocolUtils.createGetAllChallengesResponse(challenges);
            } catch (SwimmingCompetitionException e) {
                connected = false;
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }
        if (request.getType() == RequestType.FINDING_ALL_PARTICIPANTS_BY_CHALLENGE) {
            //System.out.println("Finding all participants by challenge request ..." + request.getType());
            Challenge challenge = request.getChallenge();
            try {
                List<ParticipantDTO> participantList = server.findParticipantsByChallenge(challenge);
                return JsonProtocolUtils.createFindingParticipantByChallengeResponse(participantList);
            } catch (SwimmingCompetitionException e) {
                connected = false;
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }
        if (request.getType() == RequestType.ADD_PARTICIPANT) {
            //System.out.println("Return added participant request ..." + request.getType());
            String name = request.getNameParticipant();
            int age = request.getAgeParticipant();
            try {
                Participant participant = server.addParticipant(name, age);
                return JsonProtocolUtils.createAddParticipantResponse(participant);
            } catch (SwimmingCompetitionException e) {
                connected = false;
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }
        if (request.getType() == RequestType.ADD_PARTICIPANT_TO_MORE_CHALLENGES) {
            //System.out.println("Add participant to more challenges request ..." + request.getType());
            Participant participant = request.getParticipant();
            List<Challenge> challenges = request.getChallenges();
            try {
                server.addParticipantToMoreChallenges(participant, challenges);
                return okResponse;
            } catch (SwimmingCompetitionException e) {
                connected = false;
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }
        if (request.getType()== RequestType.LOGOUT){
            //System.out.println("Logout request ..."+request.getType());
            Organizer organizer = request.getOrganizer();
            try {
                server.logout(organizer, this);
                connected=false;
                return okResponse;

            } catch (SwimmingCompetitionException e) {
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }
        if (request.getType()== RequestType.LOGIN){
            //System.out.println("Login request ..."+request.getType());
            Organizer organizer = request.getOrganizer();
            try {
                server.login(organizer, this);
                return okResponse;
            } catch (SwimmingCompetitionException e) {
                connected=false;
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }
        return response;
    }

    @Override
    public void updateTables(ChallengeDTO[] challengeDTOS) {
        Response resp=JsonProtocolUtils.createUpdateParticipantsResponse(challengeDTOS);
        //System.out.println("Update participants response");
        try {
            sendResponse(resp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendResponse(Response response) throws IOException{
        String responseLine=gsonFormatter.toJson(response);
        //System.out.println("sending response "+responseLine);
        synchronized (output) {
            output.println(responseLine);
            output.flush();
        }
    }

}
