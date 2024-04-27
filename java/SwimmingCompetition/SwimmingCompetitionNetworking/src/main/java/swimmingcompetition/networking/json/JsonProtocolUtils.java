package swimmingcompetition.networking.json;

import swimmingcompetition.model.Challenge;
import swimmingcompetition.model.Organizer;
import swimmingcompetition.model.Participant;
import swimmingcompetition.model.dto.ChallengeDTO;
import swimmingcompetition.model.dto.ParticipantDTO;

import java.util.List;


public class JsonProtocolUtils {
    public static Request createFindingAccountRequest(String username, String password){
        Request request=new Request();
        request.setType(RequestType.FINDING_ACCOUNT);
        request.setUsername(username);
        request.setPassword(password);
        return request;
    }
    public static Response createFindingAccountResponse(Organizer organizer){
        Response response=new Response();
        response.setType(ResponseType.FINDING_ACCOUNT);
        response.setAccount(organizer);
        return response;
    }
    public static Request createLogoutRequest(Organizer organizer){
        Request req=new Request();
        req.setType(RequestType.LOGOUT);
        req.setOrganizer(organizer);
        return req;
    }
    public static Request createGetAllChallengesRequest(){
        Request request=new Request();
        request.setType(RequestType.GET_ALL_CHALLENGES);
        return request;
    }
    public static Response createGetAllChallengesResponse(ChallengeDTO[] challenges){
        Response response=new Response();
        response.setType(ResponseType.GET_ALL_CHALLENGES);
        response.setAllChallenges(challenges);
        return response;
    }
//    public static Response createGetNumberOfParticipantsByChallengeResponse(int number){
//        Response response=new Response();
//        response.setType(ResponseType.NUMBER_OF_PARTICIPANTS_BY_CHALLENGE);
//        response.setNumberOfParticipantsByChallenge(number);
//        return response;
//    }
//    public static Request createGetNumberofParticipantsByChallengeRequest(Challenge challenge){
//        Request request=new Request();
//        request.setChallenge(challenge);
//        request.setType(RequestType.NUMBER_OF_PARTICIPANTS_BY_CHALLENGE);
//        return request;
//    }
    public static Response createFindingParticipantByChallengeResponse(List<ParticipantDTO> participants){
        Response response=new Response();
        response.setType(ResponseType.FINDING_ALL_PARTICIPANTS_BY_CHALLENGE);
        response.setParicipantsByChallenge(participants);
        return response;
    }
    public static Request createFindingParticipantByChallengeRequest(Challenge challenge){
        Request request=new Request();
        request.setChallenge(challenge);
        request.setType(RequestType.FINDING_ALL_PARTICIPANTS_BY_CHALLENGE);
        return request;
    }
//    public static Response createFindingChallengesByParticipantResponse(List<Challenge> challenges){
//        Response response=new Response();
//        response.setType(ResponseType.FINDING_ALL_CHALLENGES_BY_PARTICIPANT);
//        response.setChallengesByParticipant(challenges);
//        return response;
//    }
//    public static Request createFindingChallengesByParticipantRequest(Participant participant){
//        Request request=new Request();
//        request.setParticipant(participant);
//        request.setType(RequestType.FINDING_ALL_CHALENGES_BY_PARTICIPANT);
//        return request;
//    }
    public static Response createAddParticipantResponse(Participant participant){
        Response response=new Response();
        response.setType(ResponseType.ADD_PARTICIPANT);
        response.setAddedParticipant(participant);
        return response;
    }
    public static Request createAddParticipantRequest(String name, int age){
        Request request=new Request();
        request.setNameParticipant(name);
        request.setAgeParticipant(age);
        request.setType(RequestType.ADD_PARTICIPANT);
        return request;
    }
    public static Response createAddParticipantToMoreChallengesResponse(){
        Response response=new Response();
        response.setType(ResponseType.ADD_PARTICIPANT);
        return response;
    }
    public static Request createAddParticipantToMoreChallengesRequest(Participant participant, List<Challenge> challenges){
        Request request=new Request();
        request.setParticipant(participant);
        request.setChallenges(challenges);
        request.setType(RequestType.ADD_PARTICIPANT_TO_MORE_CHALLENGES);
        return request;
    }
    public static Request createLoginRequest(Organizer user){
        Request req=new Request();
        req.setType(RequestType.LOGIN);
        req.setOrganizer(user);
        return req;
    }
    public static Response createUpdateParticipantsResponse(ChallengeDTO[] challengeDTOS){
        Response resp=new Response();
        resp.setAllChallenges(challengeDTOS);
        resp.setType(ResponseType.UPDATE_TABLES);
        return resp;
    }
    public static Response createOkResponse(){
        Response resp=new Response();
        resp.setType(ResponseType.OK);
        return resp;
    }

    public static Response createErrorResponse(String errorMessage){
        Response resp=new Response();
        resp.setType(ResponseType.ERROR);
        resp.setErrorMessage(errorMessage);
        return resp;
    }

}
