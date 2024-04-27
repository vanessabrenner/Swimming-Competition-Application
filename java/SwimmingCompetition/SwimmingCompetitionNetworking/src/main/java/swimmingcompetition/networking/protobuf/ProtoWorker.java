package swimmingcompetition.networking.protobuf;

import swimmingcompetition.model.Challenge;
import swimmingcompetition.model.Organizer;
import swimmingcompetition.model.Participant;
import swimmingcompetition.model.dto.ChallengeDTO;
import swimmingcompetition.model.dto.ParticipantDTO;
import swimmingcompetition.networking.json.JsonProtocolUtils;
import swimmingcompetition.networking.json.Response;
import swimmingcompetition.services.ISwimmingCompetitionObserver;
import swimmingcompetition.services.ISwimmingCompetitionServices;
import swimmingcompetition.services.SwimmingCompetitionException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

public class ProtoWorker implements Runnable, ISwimmingCompetitionObserver {
    private ISwimmingCompetitionServices server;
    private Socket connection;

    private InputStream input;
    private OutputStream output;
    private volatile boolean connected;
    public ProtoWorker(ISwimmingCompetitionServices server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try{
            output=connection.getOutputStream() ;//new ObjectOutputStream(connection.getOutputStream());
            input=connection.getInputStream(); //new ObjectInputStream(connection.getInputStream());
            connected=true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {

        while(connected){
            try {
                // Object request=input.readObject();
                System.out.println("Waiting requests ...");
                SwimmingCompetitionProtobufs.Request request=SwimmingCompetitionProtobufs.Request.parseDelimitedFrom(input);
                System.out.println("Request received: "+request);
                SwimmingCompetitionProtobufs.Response response=handleRequest(request);
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

    private SwimmingCompetitionProtobufs.Response handleRequest(SwimmingCompetitionProtobufs.Request request) {
        SwimmingCompetitionProtobufs.Response response=null;
        switch (request.getType()){
            case FINDING_ACCOUNT:{
                //System.out.println("Finding account request ..." + request.getType());
                String username = request.getUsername();
                String password = request.getPassword();
                try {
                    Organizer organizer = server.findAccount(username, password);
                    return ProtoUtils.createFindingAccountResponse(organizer);
                } catch (SwimmingCompetitionException e) {
                    connected = false;
                    return ProtoUtils.createErrorResponse(e.getMessage());
                }
            }
            case LOGIN:{
                Organizer organizer = ProtoUtils.getOrganizer(request);
                try {
                    server.login(organizer, this);
                    return ProtoUtils.createOkResponse();
                } catch (SwimmingCompetitionException e) {
                    connected=false;
                    return ProtoUtils.createErrorResponse(e.getMessage());
                }
            }
            case GET_ALL_CHALLENGES:{
                try {
                    ChallengeDTO[] challenges = server.findAllChallenges();
                    return ProtoUtils.createGetAllChallengesResponse(challenges);
                } catch (SwimmingCompetitionException e) {
                    connected = false;
                    return ProtoUtils.createErrorResponse(e.getMessage());
                }
            }
            case FINDING_ALL_PARTICIPANTS_BY_CHALLENGE:{
                Challenge challenge = ProtoUtils.getChallenge(request);
                try {
                    List<ParticipantDTO> participantList = server.findParticipantsByChallenge(challenge);
                    return ProtoUtils.createFindingParticipantByChallengeResponse(participantList);
                } catch (SwimmingCompetitionException e) {
                    connected = false;
                    return ProtoUtils.createErrorResponse(e.getMessage());
                }
            }
            case ADD_PARTICIPANT:
                String name = request.getName();
                int age = request.getAge();
                try {
                    Participant participant = server.addParticipant(name, age);
                    return ProtoUtils.createAddParticipantResponse(participant);
                } catch (SwimmingCompetitionException e) {
                    connected = false;
                    return ProtoUtils.createErrorResponse(e.getMessage());
                }
            case ADD_PARTICIPANT_TO_MORE_CHALLENGES:
                Participant participant = ProtoUtils.getParticipant(request);
                List<Challenge> challenges = ProtoUtils.getChallenges(request);
                try {
                    server.addParticipantToMoreChallenges(participant, challenges);
                    return ProtoUtils.createOkResponse();
                } catch (SwimmingCompetitionException e) {
                    connected = false;
                    return ProtoUtils.createErrorResponse(e.getMessage());
                }
            case LOGOUT:
                Organizer organizer = ProtoUtils.getOrganizer(request);
                try {
                    server.logout(organizer, this);
                    connected=false;
                    return ProtoUtils.createOkResponse();

                } catch (SwimmingCompetitionException e) {
                    return ProtoUtils.createErrorResponse(e.getMessage());
                }
            // TO DO
        }
        return response;
    }

    private void sendResponse(SwimmingCompetitionProtobufs.Response response) throws IOException{
        System.out.println("sending response "+response);
        response.writeDelimitedTo(output);
        //output.writeObject(response);
        output.flush();
    }

    @Override
    public void updateTables(ChallengeDTO[] challengeDTOS) {
        // TO DO
        SwimmingCompetitionProtobufs.Response resp = ProtoUtils.createUpdateParticipantsResponse(challengeDTOS);
        //System.out.println("Update participants response");
        try {
            sendResponse(resp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
