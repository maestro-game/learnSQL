package repositories;

import models.Mentor;
import models.Student;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

public class StudentsRepositoryJdbcImpl implements StudentsRepository {
    private static final String //language=PostgreSQL
            SQL_FIND_BY_ID = "select students.id,\n" +
            "       students.first_name,\n" +
            "       students.last_name,\n" +
            "       students.age,\n" +
            "       students.group_number,\n" +
            "       mentors.id         as mentor_id,\n" +
            "       mentors.first_name as mentor_first_name,\n" +
            "       mentors.last_name  as mentor_last_name,\n" +
            "       mentors.subject_id\n" +
            "from students\n" +
            "         join\n" +
            "     mentors\n" +
            "     on students.id = mentors.student_id and students.id = ";
    private static final String //language=PostgreSQL
            SQL_SAVE_STUDENT = "insert into students values (default, '%s', '%s', %d, %d) returning id";
    private static final String //language=PostgreSQL
            SQL_UPDATE_STUDENT = "update students set first_name = '%s', last_name = '%s', age = %d, group_number = %d where id = %d";
    private static final String //language=PostgreSQL
            SQL_FIND_ALL_BY_AGE = "select students.id,\n" +
            "       students.first_name,\n" +
            "       students.last_name,\n" +
            "       students.age,\n" +
            "       students.group_number,\n" +
            "       mentors.id         as mentor_id,\n" +
            "       mentors.first_name as mentor_first_name,\n" +
            "       mentors.last_name  as mentor_last_name,\n" +
            "       mentors.subject_id\n" +
            "from students\n" +
            "         join\n" +
            "     mentors\n" +
            "     on students.id = mentors.student_id and students.age = ";
    private static final String //language=PostgreSQL
            SQL_FIND_ALL = "select students.id,\n" +
            "       students.first_name,\n" +
            "       students.last_name,\n" +
            "       students.age,\n" +
            "       students.group_number,\n" +
            "       mentors.id         as mentor_id,\n" +
            "       mentors.first_name as mentor_first_name,\n" +
            "       mentors.last_name  as mentor_last_name,\n" +
            "       mentors.subject_id\n" +
            "from students\n" +
            "         join\n" +
            "     mentors\n" +
            "     on students.id = mentors.student_id";

    private Connection connection;

    public StudentsRepositoryJdbcImpl(Connection connection) {
        this.connection = connection;
    }


    public List<Student> findAllBySQL(String sql) {
        Statement statement = null;
        ResultSet resultSet = null;
        List<Student> result = new LinkedList<>();
        long prevId = -1;
        Student current = null;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                if (resultSet.getInt("id") != prevId) {
                    if (current != null) {
                        result.add(current);
                    }
                    current = new Student(resultSet.getLong("id"),
                            resultSet.getString("first_name"),
                            resultSet.getString("last_name"),
                            resultSet.getInt("age"),
                            resultSet.getInt("group_number"));
                }
                current.getMentors().add(new Mentor(resultSet.getLong("mentor_id"),
                        resultSet.getString("mentor_first_name"),
                        resultSet.getString("mentor_last_name"),
                        null)); //TODO correct student id
                prevId = current.getId();
            }
            if (current != null) {
                result.add(current);
            }
            return result;
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    //TODO handling
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    //TODO handling
                }
            }
        }
    }

    @Override
    public List<Student> findAllByAge(int age) {
        return findAllBySQL(SQL_FIND_ALL_BY_AGE + age);
    }

    @Override
    public List<Student> findAll() {
        return findAllBySQL(SQL_FIND_ALL);
    }

    @Override
    public Student findById(long id) {
        Statement statement = null;
        ResultSet resultSet = null;
        Student result = null;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(SQL_FIND_BY_ID + id);
            while (resultSet.next()) {
                if (result == null) {
                    result = new Student(resultSet.getLong("id"),
                            resultSet.getString("first_name"),
                            resultSet.getString("last_name"),
                            resultSet.getInt("age"),
                            resultSet.getInt("group_number"));
                }
                result.getMentors().add(new Mentor(resultSet.getLong("mentor_id"),
                        resultSet.getString("mentor_first_name"),
                        resultSet.getString("mentor_last_name"),
                        null)); //TODO correct student id
            }
            return result;
        } catch (
                SQLException e) {
            throw new IllegalArgumentException(e);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    //TODO handling
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    //TODO handling
                }
            }
        }

    }

    @Override
    public void save(Student entity) {
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(String.format(SQL_SAVE_STUDENT, entity.getFirstName(), entity.getLastName(), entity.getAge(), entity.getGroupNumber()));
            resultSet.next();
            entity.setId(resultSet.getLong("id"));
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    //TODO handling
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    //TODO handling
                }
            }
        }
    }

    @Override
    public void update(Student entity) {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.execute(String.format(SQL_UPDATE_STUDENT, entity.getFirstName(), entity.getLastName(), entity.getAge(), entity.getGroupNumber(), entity.getId()));
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    //TODO handling
                }
            }
        }
    }
}
