package models;

public class Mentor {
    private long id;
    private String firstName;
    private String lastName;
    private Student student;

    public Mentor(long id, String firstName, String lastName, Student student) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.student = student;
    }
}
