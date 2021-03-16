package server;

import java.sql.*;

public class SQLHandler {
    protected  static Connection connection;
    private static PreparedStatement psGetNickname;
    private static PreparedStatement psRegistration;
    private static PreparedStatement psChangeNickname;

    public static boolean connect(){
        try{
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:chat.db");
            //System.out.println("connection ok");
            prepareStatements();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    private static void prepareStatements() throws SQLException {
        psGetNickname = connection.prepareStatement("SELECT nickname FROM users WHERE login = ? AND password = ?;");
        psRegistration = connection.prepareStatement("INSERT INTO users(nickname, login, password) VALUES (?,?,?);");
        psChangeNickname = connection.prepareStatement("UPDATE users SET nickname = ? WHERE nickname = ?;");
    }

    public static void disconnect() {

    }

    public static boolean changeNickname(String oldNickname, String newNickname) {
        try{
            psChangeNickname.setString(1, newNickname);
            psChangeNickname.setString(2, oldNickname);
            psChangeNickname.executeUpdate();
            return true;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public static boolean registration(String login, String password, String nickname) {
        try{
            psRegistration.setString(1, nickname);
            psRegistration.setString(2, login);
            psRegistration.setString(3, password);
            psRegistration.executeUpdate();
            return true;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public static String getNicknameByLoginandPassword(String login, String password) {
        String nick = null;
        try{
            psGetNickname.setString(1,login);
            psGetNickname.setString(2,password);
            ResultSet rs = psGetNickname.executeQuery();
            if(rs.next()){
                nick = rs.getString(1);
            }
            rs.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return nick;
    }
}
