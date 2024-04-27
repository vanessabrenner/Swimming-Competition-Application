package swimmingcompetition.persistence.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import swimmingcompetition.model.Participant;
import swimmingcompetition.persistence.ParticipantRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ParticipantDBRepository implements ParticipantRepository {
    private JdbcUtils dbUtils;
    private static final Logger logger= LogManager.getLogger(OrganizerDBRepository.class);
    public ParticipantDBRepository(Properties properties) {
        this.dbUtils = new JdbcUtils(properties);
    }

    @Override
    public Participant findOne(Long aLong) {
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
            System.out.println("Eroare la DB -- Participant " + ex.getMessage().toString());
        }
        return null;
    }

    @Override
    public Iterable<Participant> findAll() {
        logger.traceEntry("finding all participants");
        Connection connection = dbUtils.getConnection();
        List<Participant> participantList = new ArrayList<>();
        try(PreparedStatement preparedStatement = connection.prepareStatement("select * from participant")) {
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
            System.out.println("Error la DB -- Participant " + e.getMessage().toString());
        }
        logger.traceExit(participantList);
        return participantList;
    }

    @Override
    public Participant save(Participant entity) {
        logger.traceEntry("saving participant {}", entity);
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement("insert into participant(name, age) values(?,?)", Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setInt(2, entity.getAge());
            int result = preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if(resultSet.next()){
                Long id = resultSet.getLong(1);
                entity.setId(id);
            }
            logger.trace("saved participant {}", entity);
            return entity;
        }
        catch (SQLException e) {
            logger.error(e);
            System.out.println("Error la DB -- Participant " + e.getMessage().toString());
        }
        return null;
    }

    @Override
    public void delete(Long aLong) {
        logger.traceEntry("deleting participant with id {}", aLong);
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement("delete from participant where id = ?")) {
            preparedStatement.setLong(1, aLong);
            int result = preparedStatement.executeUpdate();
            logger.trace("deleted participants {}", result);
        }
        catch (SQLException e) {
            logger.error(e);
            System.out.println("Error la DB -- Participant " + e.getMessage().toString());
        }
    }

    @Override
    public void update(Participant entity) {

    }
}
