package swimmingcompetition.persistence.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import swimmingcompetition.model.*;
import swimmingcompetition.persistence.RegistrationRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class RegistrationDBRepository implements RegistrationRepository {
    private JdbcUtils dbUtils;
    private static final Logger logger= LogManager.getLogger(OrganizerDBRepository.class);
    public RegistrationDBRepository(Properties properties) {
        this.dbUtils = new JdbcUtils(properties);
    }

    @Override
    public List<Challenge> findChallengesByParticipant(Participant participant) {
        logger.traceEntry("finding challenges by participant", participant);
        Connection connection = dbUtils.getConnection();
        List<Challenge> challengeList = new ArrayList<>();
        try(PreparedStatement preparedStatement = connection.prepareStatement("select * from challenge as c inner join registration as r on c.id=r.idchallenge where r.idparticipant = ?")) {
            preparedStatement.setLong(1, participant.getId());
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Long id = resultSet.getLong("id");
                    Distance distance = Distance.fromInt(resultSet.getInt("distance"));
                    Style style = Style.valueOf(resultSet.getString("style"));
                    Challenge challenge = new Challenge(distance, style);
                    challenge.setId(id);
                    challengeList.add(challenge);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error la DB -- Registration " + e.getMessage().toString());
        }
        logger.traceExit(challengeList);
        return challengeList;
    }

    @Override
    public void addParticipantToMoreChallenges(Participant participant, List<Challenge> challenges) {
        logger.traceEntry("saving registration of participant {} at the challenges {}", participant, challenges);
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement("insert into registration(idparticipant, idchallenge) values(?,?)")) {
            for(Challenge challenge: challenges) {
                preparedStatement.setLong(1, participant.getId());
                preparedStatement.setLong(2, challenge.getId());
                int result = preparedStatement.executeUpdate();
                Registration registration =  new Registration(participant, challenge);
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    Long id = resultSet.getLong(1);
                    registration.setId(id);
                }
                logger.error("saved registration {}", registration);
            }
        }
        catch (SQLException e) {
            logger.error(e);
            System.out.println("Error la DB -- Registration " + e.getMessage().toString());
        }
    }

    @Override
    public List<Participant> findParticipantsByChallenge(Challenge challenge) {
        logger.traceEntry("finding parcipants by challenge", challenge);
        Connection connection = dbUtils.getConnection();
        List<Participant> participantList = new ArrayList<>();
        try(PreparedStatement preparedStatement = connection.prepareStatement("select * from participant as p inner join registration as r on p.id=r.idparticipant where r.idchallenge = ?")) {
            preparedStatement.setLong(1, challenge.getId());
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Long id = resultSet.getLong("id");
                    String name = resultSet.getString("name");
                    int age = resultSet.getInt("age");
                    Participant participant = new Participant(name, age);
                    participant.setId(id);
                    participantList.add(participant);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error la DB -- Registration " + e.getMessage());
        }
        logger.traceExit(participantList);
        return participantList;
    }

    @Override
    public int countByChallenge(Challenge challenge) {
        logger.traceEntry("count the participants at the challenge {}",challenge);
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement("select count(*) as count from registration where idchallenge = ?")) {
            preparedStatement.setLong(1, challenge.getId());
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if(resultSet.next()) {
                    int count = resultSet.getInt("count");
                    return count;
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error la DB -- Registration " + e.getMessage().toString());
        }
        logger.traceExit("count 0 participants at the challenge {}", challenge);
        return 0;
    }
    private Participant findParticipant(Long aLong) {
        logger.traceEntry("finding participant with id {}", aLong);
        Connection connection = this.dbUtils.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement("select * from participant where id = ?")){
            preparedStatement.setLong(1, aLong);
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                if(resultSet.next()){
                    Long id = resultSet.getLong("id");
                    String name = resultSet.getString("name");
                    int age = resultSet.getInt("age");
                    Participant participant = new Participant(name, age);
                    participant.setId(id);
                    return participant;
                }
            }
        }
        catch (SQLException ex){
            logger.error(ex);
            System.out.println("Eroare la DB -- Registration -- Participant " + ex.getMessage().toString());
        }
        return null;
    }
    private Challenge findChallenge(Long aLong) {
        logger.traceEntry("finding challenge with id {}", aLong);
        Connection connection = this.dbUtils.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement("select * from challenge where id = ?")){
            preparedStatement.setLong(1, aLong);
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                if(resultSet.next()){
                    Long id = resultSet.getLong("id");
                    Style style = Style.valueOf(resultSet.getString("style"));
                    Distance distance = Distance.fromInt(resultSet.getInt("distance"));
                    Challenge challenge = new Challenge(distance, style);
                    challenge.setId(id);
                    return challenge;
                }
            }
        }
        catch (SQLException ex){
            logger.error(ex);
            System.out.println("Eroare la DB -- Challenge " + ex.getMessage().toString());
        }
        logger.traceExit("No challenge found with id {}", aLong);
        return null;
    }
    @Override
    public Registration findOne(Long aLong) {
        //logger.traceEntry("finding registration with id {}", aLong);
        Connection connection = this.dbUtils.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement("select * from registration where id = ?")){
            preparedStatement.setLong(1, aLong);
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                if(resultSet.next()){
                    Long id = resultSet.getLong("id");
                    Long idParticipant = resultSet.getLong("idparticipant");
                    Long idChallenge = resultSet.getLong("idchallenge");
                    Participant participant = this.findParticipant(idParticipant);
                    Challenge challenge = this.findChallenge(idChallenge);
                    Registration registration = new Registration(participant, challenge);
                    return registration;
                }
            }
        }
        catch (SQLException ex){
            logger.error(ex);
            System.out.println("Eroare la DB -- Registration " + ex.getMessage().toString());
        }
       logger.traceExit("finding no registration with id {}", aLong);
        return null;
    }

    @Override
    public Iterable<Registration> findAll() {
        logger.traceEntry("finding all registrations");
        Connection connection = dbUtils.getConnection();
        List<Registration> registrationList = new ArrayList<>();
        try(PreparedStatement preparedStatement = connection.prepareStatement("select * from Registration")) {
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Long id = resultSet.getLong("id");
                    Long idParticipant = resultSet.getLong("idparticipant");
                    Long idChallenge = resultSet.getLong("idchallenge");
                    Participant participant = this.findParticipant(idParticipant);
                    Challenge challenge = this.findChallenge(idChallenge);
                    Registration registration = new Registration(participant, challenge);
                    registration.setId(id);
                    registrationList.add(registration);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error la DB -- Registration " + e.getMessage().toString());
        }
        logger.traceExit(registrationList);
        return registrationList;
    }

    @Override
    public Registration save(Registration entity) {
        logger.traceEntry("saving registration {}", entity);
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement("insert into registration(idparticipant, idchallenge) values(?,?)")) {
            preparedStatement.setLong(1, entity.getParticipant().getId());
            preparedStatement.setLong(2, entity.getChallenge().getId());
            int result = preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if(resultSet.next()){
                Long id = resultSet.getLong(1);
                entity.setId(id);
            }
            logger.error("saved registration {}", entity);
            return entity;
        }
        catch (SQLException e) {
            logger.error(e);
            System.out.println("Error la DB -- Registration " + e.getMessage().toString());
        }
        return null;
    }

    @Override
    public void delete(Long aLong) {
        logger.traceEntry("deleting registration with id {}", aLong);
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement("delete from registration where id = ?")) {
            preparedStatement.setLong(1, aLong);
            int result = preparedStatement.executeUpdate();
            logger.error("deleted registration {}", result);
        }
        catch (SQLException e) {
            logger.error(e);
            System.out.println("Error la DB -- Registration " + e.getMessage().toString());
        }
    }

    @Override
    public void update(Registration entity) {

    }
}

