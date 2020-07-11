import models.Student;
import repositories.StudentsRepository;
import repositories.StudentsRepositoryJdbcImpl;

import java.sql.*;

public class Main {

    private static String URL = "jdbc:postgresql://localhost:5432/java_lab_pract_2020";
    private static String USER = "postgres";
    private static String PASSWORD = "sdfsdf";

    public static void main(String[] args) throws SQLException {
        Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
        StudentsRepository studentsRepository = new StudentsRepositoryJdbcImpl(connection);
        Object res = studentsRepository.findAllByAge(25);
        connection.close();
    }
}
