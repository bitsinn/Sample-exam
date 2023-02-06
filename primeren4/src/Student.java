import java.util.ArrayList;
import java.util.List;

public class Student extends User{

    List<Grade> gradeList;

    public Student(String facultyNumber, String EGN){
        super(facultyNumber,EGN);
        gradeList = new ArrayList<>();
    }

    public List<Grade> getGradeList(){
        return gradeList;
    }

    public UserType getUserType(){
        return UserType.STUDENT;
    }

    @Override
    public String toString() {
        return "Student{" +
                "gradeList=" + gradeList +
                '}' + super.toString();
    }
}
