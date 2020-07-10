import java.sql.*;

public class Main {

    private static String URL = "jdbc:postgresql://localhost:5432/java_lab_pract_2020";
    private static String USER = "postgres";
    private static String PASSWORD = "sdfsdf";

    public static Connection openConnection(String url, String user, String password) {
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static void main(String[] args) {
        Connection connection = openConnection(URL, USER, PASSWORD);
        //ладно, будем сразу в try оборачивать
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from students");
            while (resultSet.next()) {
                System.out.println(resultSet.getString("first_name") + " " + resultSet.getString("last_name"));
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
