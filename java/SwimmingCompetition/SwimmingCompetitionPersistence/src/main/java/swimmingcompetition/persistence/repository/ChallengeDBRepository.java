package swimmingcompetition.persistence.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import swimmingcompetition.model.Challenge;
import swimmingcompetition.model.Distance;
import swimmingcompetition.model.Style;
import swimmingcompetition.persistence.ChallengeRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ChallengeDBRepository implements ChallengeRepository {
    private JdbcUtils dbUtils;
    private static final Logger logger= LogManager.getLogger(ChallengeDBRepository.class);
    public ChallengeDBRepository(Properties props) {
        this.dbUtils = new JdbcUtils(props);
    }

    @Override
    public Challenge findOne(Long aLong) {
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
    public Iterable<Challenge> findAll() {
        logger.traceEntry("find all challenges");
        Connection connection = dbUtils.getConnection();
        List<Challenge> challengeList = new ArrayList<>();
        try(PreparedStatement preparedStatement = connection.prepareStatement("select * from challenge")) {
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
            System.out.println("Error la DB -- Challenge " + e.getMessage().toString());
        }
        logger.traceExit(challengeList);
        return challengeList;

    }

    @Override
    public Challenge save(Challenge entity) {
        logger.traceEntry("save challenge {}", entity);
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement("insert into challenge(distance, style) values(?,?)", Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, entity.getDistance().get());
            preparedStatement.setString(2, entity.getStyle().name());
            int result = preparedStatement.executeUpdate();
            ResultSet  resultSet = preparedStatement.getGeneratedKeys();
            if(resultSet.next()){
                Long id = resultSet.getLong(1);
                entity.setId(id);
            }
            logger.trace("saved challenge {}", entity);
            return entity;
        }
        catch (SQLException e) {
            logger.error(e);
            System.out.println("Error la DB -- Challenge " + e.getMessage().toString());
        }
        return null;
    }

    @Override
    public void delete(Long aLong) {
        logger.traceEntry("delete challenge with id {}", aLong);
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement("delete from challenge where id = ?")) {
            preparedStatement.setLong(1, aLong);
            int result = preparedStatement.executeUpdate();
            logger.trace("challenges deleted {}", result);
        }
        catch (SQLException e) {
            logger.error(e);
            System.out.println("Error la DB -- Challenge " + e.getMessage().toString());
        }
    }

    @Override
    public void update(Challenge entity) {
        // Nu cred ca avem nevoie
    }
}
