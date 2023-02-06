import java.io.Serializable;

public class Grade implements Serializable {
    private String subject;
    private int grades;
    private int semester;

    public Grade(String subject, int grades, int semester){
        this.subject = subject;
        this.grades = grades;
        this.semester = semester;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getGrades() {
        return grades;
    }

    public void setGrades(int grades) {
        this.grades = grades;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    @Override
    public String toString() {
        return " " + subject + " " + semester + " " + grades;
    }
}
