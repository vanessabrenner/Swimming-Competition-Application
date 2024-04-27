package swimmingcompetition.persistence.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import swimmingcompetition.model.Organizer;
import swimmingcompetition.persistence.OrganizerRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class OrganizerDBRepository implements OrganizerRepository {
    private JdbcUtils dbUtils;
    private static final Logger logger= LogManager.getLogger(OrganizerDBRepository.class);
    public OrganizerDBRepository(Properties properties) {
        this.dbUtils = new JdbcUtils(properties);
    }

    @Override
    public Organizer findOne(Long aLong) {
        logger.traceEntry("finding organizer with id {}", aLong);
        Connection connection = this.dbUtils.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement("select * from organizer where id = ?")){
            preparedStatement.setLong(1, aLong);
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                if(resultSet.next()){
                    Long id = resultSet.getLong("id");
                    String username = resultSet.getString("username");
                    String password = resultSet.getString("password");
                    Organizer organizer = new Organizer(username, password);
                    organizer.setId(id);
                    return organizer;
                }
            }
        }
        catch (SQLException ex){
            logger.error(ex);
            System.out.println("Eroare la DB -- Organizer " + ex.getMessage().toString());
        }
        logger.traceExit("finding no organizer with id {}", aLong);
        return null;
    }

    @Override
    public Iterable<Organizer> findAll() {
        logger.traceEntry("finding all organizers");
        Connection connection = dbUtils.getConnection();
        List<Organizer> organizerList = new ArrayList<>();
        try(PreparedStatement preparedStatement = connection.prepareStatement("select * from organizer")) {
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Long id = resultSet.getLong("id");
                    String username = resultSet.getString("username");
                    String password = resultSet.getString("password");
                    Organizer organizer = new Organizer(username, password);
                    organizer.setId(id);
                    organizerList.add(organizer);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error la DB -- Organizer " + e.getMessage().toString());
        }
        //logger.traceExit(organizerList);
        return organizerList;
    }

    @Override
    public Organizer save(Organizer entity) {
        logger.traceEntry("saving organizer {}", entity);
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement("insert into organizer(username, password) values(?,?)", Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, entity.getUsername());
            preparedStatement.setString(2, entity.getPassword());
            int result = preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if(resultSet.next()){
                Long id = resultSet.getLong(1);
                entity.setId(id);
            }
            logger.trace("saved organizer {}", entity);
            return entity;
        }
        catch (SQLException e) {
           logger.error(e);
            System.out.println("Error la DB -- Organizer " + e.getMessage().toString());
        }
        return null;
    }

    @Override
    public void delete(Long aLong) {
        logger.traceEntry("deleting organizer with id {}", aLong);
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement("delete from organizer where id = ?")) {
            preparedStatement.setLong(1, aLong);
            int result = preparedStatement.executeUpdate();
            logger.trace("deleted organizer {}", result);
        }
        catch (SQLException e) {
            logger.error(e);
            System.out.println("Error la DB -- Organizer " + e.getMessage().toString());
        }
    }

    @Override
    public void update(Organizer entity) {

    }
    @Override
    public Organizer findAccount(String username, String password) {
        logger.traceEntry("finding organizator account by username {} and password {}", username, password);
        Connection conn = dbUtils.getConnection();
        Organizer organizer = null;
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM organizer WHERE username = ? AND password = ?")) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    long id = resultSet.getInt("id");
                    organizer = new Organizer(username, password);
                    organizer.setId(id);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB" + e);
        }
        logger.traceExit(organizer);
        return organizer;
    }
}
