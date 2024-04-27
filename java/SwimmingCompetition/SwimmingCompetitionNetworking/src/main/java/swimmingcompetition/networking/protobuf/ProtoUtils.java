package swimmingcompetition.networking.protobuf;

import swimmingcompetition.model.*;
import swimmingcompetition.model.dto.ChallengeDTO;
import swimmingcompetition.model.dto.ParticipantDTO;

import java.util.ArrayList;
import java.util.List;

public class ProtoUtils {
    // requests
    public static SwimmingCompetitionProtobufs.Request createLoginRequest(Organizer o){
        SwimmingCompetitionProtobufs.Organizer organizer = SwimmingCompetitionProtobufs.Organizer.newBuilder().
                setId(o.getId()).setPassword(o.getPassword()).setUsername(o.getUsername()).build();
        SwimmingCompetitionProtobufs.Request request = SwimmingCompetitionProtobufs.Request.newBuilder().
                setType(SwimmingCompetitionProtobufs.Request.Type.LOGIN).setOrganizer(organizer).build();
        return request;
    }
    public static SwimmingCompetitionProtobufs.Request createFindingAccountRequest(String username, String password){
        SwimmingCompetitionProtobufs.Request request = SwimmingCompetitionProtobufs.Request.newBuilder().
                setUsername(username).setPassword(password).
                setType(SwimmingCompetitionProtobufs.Request.Type.FINDING_ACCOUNT).build();
        return request;
    }
    public static SwimmingCompetitionProtobufs.Request createGetAllChallengesRequest(){
        SwimmingCompetitionProtobufs.Request request = SwimmingCompetitionProtobufs.Request.newBuilder().
                setType(SwimmingCompetitionProtobufs.Request.Type.GET_ALL_CHALLENGES).build();
        return request;
    }
    public static SwimmingCompetitionProtobufs.Request createFindingParticipantByChallengeRequest(Challenge c){
        SwimmingCompetitionProtobufs.Challenge challenge = SwimmingCompetitionProtobufs.Challenge.newBuilder().
                setId(c.getId()).setDistance(mapDistanceEnum(c.getDistance())).setStyle(mapStyleEnum(c.getStyle())).build();
        SwimmingCompetitionProtobufs.Request request = SwimmingCompetitionProtobufs.Request.newBuilder().
                setChallenge(challenge).
                setType(SwimmingCompetitionProtobufs.Request.Type.FINDING_ALL_PARTICIPANTS_BY_CHALLENGE).build();
        return request;
    }
    public static SwimmingCompetitionProtobufs.Request createAddParticipantRequest(String name, int age){
        SwimmingCompetitionProtobufs.Request request = SwimmingCompetitionProtobufs.Request.newBuilder().
                setName(name).
                setAge(age).
                setType(SwimmingCompetitionProtobufs.Request.Type.ADD_PARTICIPANT).build();
        return request;
    }
    public static SwimmingCompetitionProtobufs.Request createAddParticipantToMoreChallengesRequest(Participant p, List<Challenge> chs){
        SwimmingCompetitionProtobufs.Participant participant = SwimmingCompetitionProtobufs.Participant.newBuilder().
                setId(p.getId()).setAge(p.getAge()).setName(p.getName()).build();
        List<SwimmingCompetitionProtobufs.Challenge> challenges = new ArrayList<>();
        for(Challenge c : chs){
            SwimmingCompetitionProtobufs.Challenge challenge = SwimmingCompetitionProtobufs.Challenge.newBuilder().
                    setId(c.getId()).setStyle(mapStyleEnum(c.getStyle())).setDistance(mapDistanceEnum(c.getDistance())).build();
            challenges.add(challenge);
        }
        SwimmingCompetitionProtobufs.Request request = SwimmingCompetitionProtobufs.Request.newBuilder().
                addAllChallenges(challenges).setParticipant(participant).
                setType(SwimmingCompetitionProtobufs.Request.Type.ADD_PARTICIPANT_TO_MORE_CHALLENGES).build();
        return request;
    }
    public static SwimmingCompetitionProtobufs.Request createLogoutRequest(Organizer o){
        SwimmingCompetitionProtobufs.Organizer organizer = SwimmingCompetitionProtobufs.Organizer.newBuilder().
                setId(o.getId()).setPassword(o.getPassword()).setUsername(o.getUsername()).build();
        SwimmingCompetitionProtobufs.Request request = SwimmingCompetitionProtobufs.Request.newBuilder().
                setOrganizer(organizer).
                setType(SwimmingCompetitionProtobufs.Request.Type.LOGOUT).build();
        return request;
    }
    // responses
    public static SwimmingCompetitionProtobufs.Response createFindingAccountResponse(Organizer o){
        SwimmingCompetitionProtobufs.Organizer organizer = SwimmingCompetitionProtobufs.Organizer.newBuilder().
                setId(o.getId()).setPassword(o.getPassword()).setUsername(o.getUsername()).build();
        SwimmingCompetitionProtobufs.Response response = SwimmingCompetitionProtobufs.Response.newBuilder().
                setOrganizer(organizer).
                setType(SwimmingCompetitionProtobufs.Response.Type.FINDING_ACCOUNT).build();
        return response;
    }
    public static SwimmingCompetitionProtobufs.Response createGetAllChallengesResponse(ChallengeDTO[] chs){
        List<SwimmingCompetitionProtobufs.ChallengeDTO> challengesDTO = new ArrayList<>();
        for(ChallengeDTO c : chs){
            SwimmingCompetitionProtobufs.ChallengeDTO challengeDTO = SwimmingCompetitionProtobufs.ChallengeDTO.newBuilder().
                    setId(c.getId()).setDistance(mapDistanceEnum(c.getDistance())).
                    setStyle(mapStyleEnum(c.getStyle())).
                    setNoParticipants(c.getNoParticipants()).build();
            challengesDTO.add(challengeDTO);
        }
        SwimmingCompetitionProtobufs.Response  response = SwimmingCompetitionProtobufs.Response.newBuilder().
                addAllChallenges(challengesDTO).
                setType(SwimmingCompetitionProtobufs.Response.Type.GET_ALL_CHALLENGES).build();
        return response;
    }
    public static SwimmingCompetitionProtobufs.Response createFindingParticipantByChallengeResponse(List<ParticipantDTO> ps){
        List<SwimmingCompetitionProtobufs.ParticipantDTO> participantDTOs = new ArrayList<>();
        for(ParticipantDTO p : ps){
            List<SwimmingCompetitionProtobufs.Challenge> challenges = new ArrayList<>();
            for(Challenge c: p.getChallenges()){
                SwimmingCompetitionProtobufs.Challenge challenge = SwimmingCompetitionProtobufs.Challenge.newBuilder().
                        setId(c.getId()).setStyle(mapStyleEnum(c.getStyle())).setDistance(mapDistanceEnum(c.getDistance())).build();
                challenges.add(challenge);
            }
            SwimmingCompetitionProtobufs.ParticipantDTO participantDTO = SwimmingCompetitionProtobufs.ParticipantDTO.newBuilder().
                    setId(p.getId()).setAge(p.getAge()).setName(p.getName()).addAllChallenges(challenges).build();
            participantDTOs.add(participantDTO);
        }
        SwimmingCompetitionProtobufs.Response response = SwimmingCompetitionProtobufs.Response.newBuilder().
                addAllParticipants(participantDTOs).
                setType(SwimmingCompetitionProtobufs.Response.Type.FINDING_ALL_PARTICIPANTS_BY_CHALLENGE).build();
        return response;
    }
    public static SwimmingCompetitionProtobufs.Response createAddParticipantResponse(Participant p){
        SwimmingCompetitionProtobufs.Participant participant = SwimmingCompetitionProtobufs.Participant.newBuilder().
                setId(p.getId()).setAge(p.getAge()).setName(p.getName()).build();
        SwimmingCompetitionProtobufs.Response response = SwimmingCompetitionProtobufs.Response.newBuilder().
                setParticipant(participant).
                setType(SwimmingCompetitionProtobufs.Response.Type.ADD_PARTICIPANT).build();
        return response;
    }
    public static SwimmingCompetitionProtobufs.Response createUpdateParticipantsResponse(ChallengeDTO[] chdtos){
        List<SwimmingCompetitionProtobufs.ChallengeDTO> challengeDTOS = new ArrayList<>();
        for(ChallengeDTO c : chdtos){
            SwimmingCompetitionProtobufs.ChallengeDTO challengeDTO = SwimmingCompetitionProtobufs.ChallengeDTO.newBuilder().
                    setId(c.getId()).setDistance(mapDistanceEnum(c.getDistance())).
                    setStyle(mapStyleEnum(c.getStyle())).
                    setNoParticipants(c.getNoParticipants()).build();
            challengeDTOS.add(challengeDTO);
        }
        SwimmingCompetitionProtobufs.Response response = SwimmingCompetitionProtobufs.Response.newBuilder().
                setType(SwimmingCompetitionProtobufs.Response.Type.UPDATE_TABLES).
                addAllChallenges(challengeDTOS).build();
        return response;
    }
    public static SwimmingCompetitionProtobufs.Response createErrorResponse(String errorMessage){
        SwimmingCompetitionProtobufs.Response response = SwimmingCompetitionProtobufs.Response.newBuilder().
                setError(errorMessage).
                setType(SwimmingCompetitionProtobufs.Response.Type.ERROR).build();
        return response;
    }
    public static SwimmingCompetitionProtobufs.Response createOkResponse() {
        SwimmingCompetitionProtobufs.Response response = SwimmingCompetitionProtobufs.Response.newBuilder().
                setType(SwimmingCompetitionProtobufs.Response.Type.OK).build();
        return response;
    }
    // utils
    private static SwimmingCompetitionProtobufs.Distance mapDistanceEnum(Distance distance) {
        switch (distance) {
            case MICA:
                return SwimmingCompetitionProtobufs.Distance.MICA;
            case MARE:
                return SwimmingCompetitionProtobufs.Distance.MARE;
            case MEDIE:
                return SwimmingCompetitionProtobufs.Distance.MEDIE;
            case EXTREMA:
                return SwimmingCompetitionProtobufs.Distance.EXTREMA;
            default:
                throw new IllegalArgumentException("Unknown distance: " + distance);
        }
    }
    private static Distance mapDistanceEnum(SwimmingCompetitionProtobufs.Distance distance) {
        switch (distance) {
            case MICA:
                return Distance.MICA;
            case MARE:
                return Distance.MARE;
            case MEDIE:
                return Distance.MEDIE;
            case EXTREMA:
                return Distance.EXTREMA;
            default:
                throw new IllegalArgumentException("Unknown distance: " + distance);
        }
    }
    private static SwimmingCompetitionProtobufs.Style mapStyleEnum(Style style) {
        switch (style) {
            case LIBER:
                return SwimmingCompetitionProtobufs.Style.LIBER;
            case SPATE:
                return SwimmingCompetitionProtobufs.Style.SPATE;
            case FLUTURE:
                return SwimmingCompetitionProtobufs.Style.FLUTURE;
            case MIXT:
                return SwimmingCompetitionProtobufs.Style.MIXT;
            default:
                throw new IllegalArgumentException("Unknown style: " + style);
        }
    }
    private static Style mapStyleEnum(SwimmingCompetitionProtobufs.Style style) {
        switch (style) {
            case LIBER:
                return Style.LIBER;
            case SPATE:
                return Style.SPATE;
            case FLUTURE:
                return Style.FLUTURE;
            case MIXT:
                return Style.MIXT;
            default:
                throw new IllegalArgumentException("Unknown style: " + style);
        }
    }
    public static String getError(SwimmingCompetitionProtobufs.Response response){
        String errorMessage=response.getError();
        return errorMessage;
    }

    public static Organizer getAccountOrganizer(SwimmingCompetitionProtobufs.Response response){
        String username = response.getOrganizer().getUsername();
        String password = response.getOrganizer().getPassword();
        long id = response.getOrganizer().getId();
        Organizer organizer = new Organizer(username, password);
        organizer.setId(id);
        return organizer;
    }
    public static ChallengeDTO[] getAllChallenges(SwimmingCompetitionProtobufs.Response response){
        ChallengeDTO[] challengeDTOS =new ChallengeDTO[response.getChallengesCount()];
        for (int i = 0; i < response.getChallengesCount(); i++) {
            SwimmingCompetitionProtobufs.ChallengeDTO challenge = response.getChallenges(i);
            ChallengeDTO challengeDTO = new ChallengeDTO(challenge.getId(),
                    mapDistanceEnum(challenge.getDistance()), mapStyleEnum(challenge.getStyle()),
                    challenge.getNoParticipants());
            challengeDTOS[i] = challengeDTO;
        }
        return challengeDTOS;
    }
    public static List<ParticipantDTO> getParicipantsByChallenge(SwimmingCompetitionProtobufs.Response response){
        List<ParticipantDTO> participantDTOS = new ArrayList<>();
        for (int i = 0; i < response.getParticipantsCount(); i++){
            SwimmingCompetitionProtobufs.ParticipantDTO p = response.getParticipants(i);
            List<Challenge> challenges = new ArrayList<>();
            for(int c = 0; c < p.getChallengesCount(); c++) {
                SwimmingCompetitionProtobufs.Challenge challenge = p.getChallenges(c);
                Challenge ch = new Challenge(mapDistanceEnum(challenge.getDistance()), mapStyleEnum(challenge.getStyle()));
                ch.setId(challenge.getId());
                challenges.add(ch);
            }
            ParticipantDTO participantDTO = new ParticipantDTO(p.getId(), p.getName(), p.getAge(), challenges);
            participantDTOS.add(participantDTO);
        }
        return participantDTOS;
    }
    public static Participant getAddedParticipant(SwimmingCompetitionProtobufs.Response response){
        SwimmingCompetitionProtobufs.Participant p = response.getParticipant();
        Participant participant = new Participant(p.getName(), p.getAge());
        participant.setId(p.getId());
        return participant;
    }

    public static Organizer getOrganizer(SwimmingCompetitionProtobufs.Request request){
        SwimmingCompetitionProtobufs.Organizer o = request.getOrganizer();
        Organizer organizer =new Organizer(o.getUsername(), o.getPassword());
        organizer.setId(o.getId());
        return organizer;
    }

    public static Challenge getChallenge(SwimmingCompetitionProtobufs.Request request){
        SwimmingCompetitionProtobufs.Challenge c = request.getChallenge();
        Challenge challenge = new Challenge(mapDistanceEnum(c.getDistance()), mapStyleEnum(c.getStyle()));
        challenge.setId(c.getId());
        return challenge;
    }

    public static Participant getParticipant(SwimmingCompetitionProtobufs.Request request){
        SwimmingCompetitionProtobufs.Participant p = request.getParticipant();
        Participant participant = new Participant(p.getName(), p.getAge());
        participant.setId(p.getId());
        return participant;
    }

    public static List<Challenge> getChallenges(SwimmingCompetitionProtobufs.Request request){
        List<Challenge> result = new ArrayList<>();
        for (int i = 0; i < request.getChallengesCount(); i++) {
            SwimmingCompetitionProtobufs.Challenge c = request.getChallenges(i);
            Challenge ch = new Challenge(mapDistanceEnum(c.getDistance()), mapStyleEnum(c.getStyle()));
            ch.setId(c.getId());
            result.add(ch);
        }
        return result;
    }



}
