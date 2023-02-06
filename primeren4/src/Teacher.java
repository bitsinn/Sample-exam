public class Teacher extends User{
    public Teacher (String email, String password){
        super(email,password);
    }

    public UserType getUserType(){
        return UserType.TEACHER;
    }
}
