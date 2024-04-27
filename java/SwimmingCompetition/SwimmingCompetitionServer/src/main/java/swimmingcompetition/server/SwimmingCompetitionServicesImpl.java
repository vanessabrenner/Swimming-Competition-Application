package swimmingcompetition.server;

import swimmingcompetition.model.Challenge;
import swimmingcompetition.model.Organizer;
import swimmingcompetition.model.Participant;
import swimmingcompetition.model.Registration;
import swimmingcompetition.model.dto.ChallengeDTO;
import swimmingcompetition.model.dto.ParticipantDTO;
import swimmingcompetition.persistence.repository.ChallengeDBRepository;
import swimmingcompetition.persistence.repository.OrganizerDBRepository;
import swimmingcompetition.persistence.repository.ParticipantDBRepository;
import swimmingcompetition.persistence.repository.RegistrationDBRepository;
import swimmingcompetition.services.ISwimmingCompetitionObserver;
import swimmingcompetition.services.ISwimmingCompetitionServices;
import swimmingcompetition.services.SwimmingCompetitionException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class SwimmingCompetitionServicesImpl implements ISwimmingCompetitionServices {

    private ChallengeDBRepository challengeDBRepository;
    private OrganizerDBRepository organizerDBRepository;
    private ParticipantDBRepository participantDBRepository;
    private RegistrationDBRepository registrationDBRepository;
    private Map<Long, ISwimmingCompetitionObserver> loggedOrganizers;

    public SwimmingCompetitionServicesImpl(ChallengeDBRepository challengeDBRepository, OrganizerDBRepository organizerDBRepository, ParticipantDBRepository participantDBRepository, RegistrationDBRepository registrationDBRepository) {
        this.challengeDBRepository = challengeDBRepository;
        this.organizerDBRepository = organizerDBRepository;
        this.participantDBRepository = participantDBRepository;
        this.registrationDBRepository = registrationDBRepository;
        this.loggedOrganizers = new ConcurrentHashMap<>();
    }

    @Override
    public synchronized Organizer findAccount(String username, String password) throws SwimmingCompetitionException {
        Organizer organizer = this.organizerDBRepository.findAccount(username, password);
        if(organizer != null){
            if(loggedOrganizers.get(organizer.getId()) != null){
                throw new SwimmingCompetitionException("Organizer already logged.");
            }
        }
        else{
            throw new SwimmingCompetitionException("Organizer not found.");
        }
        return organizer;
    }

    @Override
    public synchronized void login(Organizer user, ISwimmingCompetitionObserver client) throws SwimmingCompetitionException {
        if(loggedOrganizers.get(user.getId())!=null)
            throw new SwimmingCompetitionException("Organizer already logged in.");
        loggedOrganizers.put(user.getId(), client);
    }

    @Override
    public synchronized ChallengeDTO[] findAllChallenges() throws SwimmingCompetitionException{
        List<ChallengeDTO> result = new ArrayList<>();
        Iterable<Challenge> challenges = this.challengeDBRepository.findAll();
        if(challenges == null){
            throw new SwimmingCompetitionException("Challenges not found");
        }
        for(Challenge challenge : challenges){
            int no = this.getNumberOfParticipantsByChallenge(challenge);
            ChallengeDTO dto = new ChallengeDTO(challenge.getId(), challenge.getDistance(), challenge.getStyle(), no);
            result.add(dto);
        }
        return result.toArray(new ChallengeDTO[result.size()]);
    }

    private synchronized int getNumberOfParticipantsByChallenge(Challenge challenge){
        return this.registrationDBRepository.countByChallenge(challenge);
    }

    @Override
    public synchronized List<ParticipantDTO> findParticipantsByChallenge(Challenge challenge) throws SwimmingCompetitionException {
        List<ParticipantDTO> result = new ArrayList<>();
        List<Participant> participants = this.registrationDBRepository.findParticipantsByChallenge(challenge);
        if(participants == null){
            throw new SwimmingCompetitionException("Participants not found.");
        }
        for(Participant participant : participants){
            List<Challenge> challenges = new ArrayList<>();
            ParticipantDTO dto = new ParticipantDTO(participant.getId(), participant.getName(), participant.getAge(), challenges);
            List<Challenge> challengeList = this.findChallengesByParticipant(participant);
            dto.setChallenges(challengeList);
            result.add(dto);
        }
        return result;
    }

    private synchronized List<Challenge> findChallengesByParticipant(Participant participant) throws SwimmingCompetitionException {
        List<Challenge> challenges = this.registrationDBRepository.findChallengesByParticipant(participant);
        if(challenges == null){
            throw new SwimmingCompetitionException("Challenges not found.");
        }
        return challenges;
    }

    @Override
    public synchronized Participant addParticipant(String name, int age) throws SwimmingCompetitionException {
        Participant participant = new Participant(name, age);
        Participant participant1 = this.participantDBRepository.save(participant);

        return participant1;
    }

    private final int defaultThreadsNo=5;
    private void notifyOrganizators() {
        Iterable<Organizer> organizers = this.organizerDBRepository.findAll();
        Iterable<Challenge> challenges = this.challengeDBRepository.findAll();
        List<ChallengeDTO> challengeDTOS = new ArrayList<>();
        for(Challenge challenge : challenges){
            int no = this.getNumberOfParticipantsByChallenge(challenge);
            ChallengeDTO dto = new ChallengeDTO(challenge.getId(), challenge.getDistance(), challenge.getStyle(), no);
            dto.setNoParticipants(no);
            challengeDTOS.add(dto);
        }
        ExecutorService executor= Executors.newFixedThreadPool(defaultThreadsNo);
        for(Organizer organizer : organizers){
            ISwimmingCompetitionObserver observer = loggedOrganizers.get(organizer.getId());
            if(observer != null){
                executor.execute(() -> {
                        System.out.println("Notifying [" + organizer.getId()+ "]  logged in.");
                        observer.updateTables(challengeDTOS.toArray(new ChallengeDTO[challengeDTOS.size()]));
                });
            }
        }
    }

    @Override
    public synchronized void addParticipantToMoreChallenges(Participant participant, List<Challenge> challenges){
        this.registrationDBRepository.addParticipantToMoreChallenges(participant, challenges);
        notifyOrganizators();
    }

    @Override
    public synchronized void logout(Organizer user, ISwimmingCompetitionObserver client) throws SwimmingCompetitionException {
        ISwimmingCompetitionObserver localClient = loggedOrganizers.remove(user.getId());
        if(localClient == null){
            throw new SwimmingCompetitionException("Client not logged in.");
        }
        // notify
    }
    private synchronized static <T> T[] iterableToArray(Iterable<T> iterable, Class<T> clazz) {
        // Creează o listă pentru a stoca elementele din iterable
        List<T> lista = new ArrayList<>();

        // Adaugă elementele din iterable în listă
        for (T element : iterable) {
            lista.add(element);
        }

        // Creează un array de tipul specificat
        @SuppressWarnings("unchecked")
        T[] array = (T[]) java.lang.reflect.Array.newInstance(clazz, lista.size());

        // Copiază elementele din lista în array
        lista.toArray(array);

        return array;
    }
}
