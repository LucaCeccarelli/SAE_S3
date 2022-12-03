package fr.univ_amu.iut.database.jdbc;

import fr.univ_amu.iut.database.Database;
import fr.univ_amu.iut.database.User;
import fr.univ_amu.iut.database.dao.DAOUsers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAOUsersJDBC implements DAOUsers {
    private final Connection connection = Database.getConnetion();
    private final PreparedStatement findAllUsers;

    private final PreparedStatement getFiveBestUsers;

    public static DAOUsersJDBC daoUsersJDBC;

    /**
     * Constructor used to initialize the statements
     *
     * @throws SQLException
     */
    public DAOUsersJDBC() throws SQLException {
        findAllUsers = connection.prepareStatement("SELECT * FROM USERS");
        getFiveBestUsers = connection.prepareStatement("SELECT NICKNAME, SCORE FROM USERS WHERE SCORE IS NOT NULL ORDER BY SCORE DESC LIMIT 5");
    }

    /**
     * Method used to initialize the DAO
     *
     * @throws SQLException
     */
    public static void initDAOUsersJDBC() throws SQLException {
        daoUsersJDBC = new DAOUsersJDBC();
    }

    /**
     *
     * @return the current object
     */
    public static DAOUsersJDBC getDAOUsersJDBC(){return daoUsersJDBC;}

    /**
     *
     * @return all the users in the DB
     * @throws SQLException
     */
    @Override
    public List<User> findAll() throws SQLException {
        List<User> users = new ArrayList<>();
        ResultSet resultSet = findAllUsers.executeQuery();

        while(resultSet.next()) {
            User user = new User();
            user.setId_user(resultSet.getInt(1));
            user.setNickname(resultSet.getString(2));
            user.setEmail(resultSet.getString(3));
            user.setScore(resultSet.getInt(4));
            user.setPassword(resultSet.getString(5));
            users.add(user);
        }
        return users;
    }

    /**
     *
     * @return the ArrayList of the users in the leader board
     * @throws SQLException
     */
    @Override
    public ArrayList<User> getLeaderBoard() throws SQLException {
        ArrayList<User> users = new ArrayList<>();
        ResultSet resultSet = getFiveBestUsers.executeQuery();

        while(resultSet.next()) {
            User user = new User();
            user.setNickname(resultSet.getString(1));
            user.setScore(resultSet.getInt(2));
            users.add(user);
        }
        return users;
    }


    /**
     *
     * @param obj
     * @return
     * @throws SQLException
     */
    @Override
    public User insert(User obj) throws SQLException {
        return null;
    }

    /**
     *
     * @param obj
     * @throws SQLException
     */
    @Override
    public void delete(User obj) throws SQLException {

    }

    /**
     *
     * @param obj
     * @throws SQLException
     */
    @Override
    public void update(User obj) throws SQLException {

    }
}
