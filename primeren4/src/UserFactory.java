import java.util.regex.Pattern;

public class UserFactory {
    private static final Pattern EMAIL = Pattern.compile("[a-z]+@tu-sofia.bg");
    private static final Pattern EGN = Pattern.compile("\\d{10}");
    private static final Pattern FACULTY_NUMBER = Pattern.compile("\\d{9}");

    public static User userCreate(String userName, String password , UserType userType) throws CredentialsException {
        switch (userType){
            case ADMIN -> {
                return new Admin(userName,password);
            }
            case TEACHER -> {
                if(!EMAIL.matcher(userName).matches()){
                    throw new CredentialsException("Invalid email!");
                }
                if(password.length() < 5){
                    throw new CredentialsException("Invalid password!");
                }
                return new Teacher(userName,password);
            }
            case STUDENT -> {
                if(!FACULTY_NUMBER.matcher(userName).matches()){
                    throw new CredentialsException("Invalid faculty number!)");
                }
                if(!EGN.matcher(password).matches()){
                    throw new CredentialsException("Invalid EGN!");
                }
                return new Student(userName,password);
            }
            default -> {return null;}
        }
    }
}
