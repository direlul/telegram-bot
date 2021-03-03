package ru.saburov.telegrambot;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAO {
    private static final String URL = "jdbc:postgresql://ec2-54-166-242-77.compute-1.amazonaws.com:5432/d5a97t3jlkoh62";
    private static final String USERNAME = "cmipkqfmagyuzo";
    private static final String PASSWORD = "d7dd3467c26c0d7cc95d86d7c92249753c4ad6e468bded552f6e4b349bd9da4c";

    private static Connection con;

    static {
        try {
            con = DriverManager.
                    getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void saveChat(String chatId) {
        try {
            PreparedStatement statement = con.prepareStatement("insert into chats (chat_id) values (?)");

            statement.setString(1, chatId);
            statement.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public List<String> readChats() {
        List<String> chats = new ArrayList<>();

        try {
            PreparedStatement statement = con.prepareStatement("select * from chats");

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                chats.add(resultSet.getString("chat_id"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return chats;
    }

    public void updateCurrentUrl(String newUrl) {
        try {
            PreparedStatement statement = con.prepareStatement("update url set url = ? where id = 1;");

            statement.setString(1, newUrl);
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public String getCurrentUrl() {
        try {
            PreparedStatement statement = con.prepareStatement("select url from url where id = 1");

            ResultSet rs = statement.executeQuery();

            return rs.getString("url");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return "Empty";
    }
}
