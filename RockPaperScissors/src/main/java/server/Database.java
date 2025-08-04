package server;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class Database {

    public Database() {
    }

    public boolean authenticate(String login, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE login = ? AND password = ?";
        String jdbcURL = "jdbc:sqlite:database.db"; // with sqlite (to check if necessary)

        try {
            Connection connection = DriverManager.getConnection(jdbcURL);
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, login);
            statement.setString(2, password);

            ResultSet rs = statement.executeQuery(); // executeQuery for SELECT

            if (rs.next()) {
                return true;
            }
            else {
                return false;
            }
        }
        catch (SQLException e) {
            throw new SQLException(e);
        }

    }

    public void updateLeaderboard(String winner, String loser) throws SQLException {
         String add = "UPDATE users SET points = points + 1 WHERE login = ?";
         String subtract = "UPDATE users SET points = points - 1 WHERE login = ?";

        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            PreparedStatement addStatement = connection.prepareStatement(add);
            PreparedStatement subtractStatement = connection.prepareStatement(subtract);

            addStatement.setString(1, winner);
            subtractStatement.setString(1, loser);

            addStatement.executeUpdate();  // executeUpdate for INSERT/UPDATE/DELETE
            subtractStatement.executeUpdate();
        }
        catch (SQLException e) {
            throw new SQLException(e);
        }

    }

    public Map<String, Integer> getLeaderboard() throws SQLException {
        String sql = "SELECT login, points FROM users ORDER BY points DESC";
        Map<String, Integer> leaderBoard = new LinkedHashMap<>();

        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:database.db");

            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                leaderBoard.put(rs.getString(1), rs.getInt(2));
            }


        } catch (SQLException e) {
            throw new SQLException(e);
        }


        /*
        leaderBoard = leaderBoard.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).collect(Collectors.toMap(
        Map.Entry::getKey, Map.Entry::getValue, (x, y) -> {throw new AssertionError();}, LinkedHashMap::new));
        */ // -- sort a linkedHashMap but SQL sorts already (hashMap doesn't save the order at all)

        return leaderBoard;
    }
}
