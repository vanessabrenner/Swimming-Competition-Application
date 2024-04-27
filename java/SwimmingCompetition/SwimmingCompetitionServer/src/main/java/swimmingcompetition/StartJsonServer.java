package swimmingcompetition;


import swimmingcompetition.networking.utils.AbstractServer;
import swimmingcompetition.networking.utils.ServerException;
import swimmingcompetition.networking.utils.SwimmingCompetitionJsonConcurrentServer;
import swimmingcompetition.persistence.repository.ChallengeDBRepository;
import swimmingcompetition.persistence.repository.OrganizerDBRepository;
import swimmingcompetition.persistence.repository.ParticipantDBRepository;
import swimmingcompetition.persistence.repository.RegistrationDBRepository;
import swimmingcompetition.server.SwimmingCompetitionServicesImpl;
import swimmingcompetition.services.ISwimmingCompetitionServices;

import java.io.IOException;
import java.util.Properties;

public class StartJsonServer {
    private static int defaultPort=55555;
    public static void main(String[] args) {
        // UserRepository userRepo=new UserRepositoryMock();
        Properties propertiessrv=new Properties();
        try {
            propertiessrv.load(StartJsonServer.class.getResourceAsStream("/swimmingcompetitionserver.properties"));
            System.out.println("Server properties set. ");
            propertiessrv.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find chatserver.properties "+e);
            return;
        }
        OrganizerDBRepository organizerDBRepository = new OrganizerDBRepository(propertiessrv);
        ChallengeDBRepository challengeDBRepository = new ChallengeDBRepository(propertiessrv);
        ParticipantDBRepository participantDBRepository = new ParticipantDBRepository(propertiessrv);
        RegistrationDBRepository registrationDBRepository = new RegistrationDBRepository(propertiessrv);

        ISwimmingCompetitionServices swimmingCompetitionServerImpl = new SwimmingCompetitionServicesImpl(challengeDBRepository, organizerDBRepository, participantDBRepository, registrationDBRepository);
        int swimmingCompetitionServerPort=defaultPort;
        try {
            swimmingCompetitionServerPort = Integer.parseInt(propertiessrv.getProperty("swimmingcompetition.server.port"));
        }catch (NumberFormatException nef){
            System.err.println("Wrong  Port Number"+nef.getMessage());
            System.err.println("Using default port "+defaultPort);
        }
        System.out.println("Starting server on port: "+swimmingCompetitionServerPort);
        AbstractServer server = new SwimmingCompetitionJsonConcurrentServer(swimmingCompetitionServerPort, swimmingCompetitionServerImpl);
        try {
            server.start();
        } catch (ServerException e) {
            System.err.println("Error starting the server" + e.getMessage());
        }
    }
}
