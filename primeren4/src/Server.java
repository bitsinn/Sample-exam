import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.stream.Collectors;

public class Server {
    private static final String FILE_NAME = "user.bin";
    private final Object usersLock;
    private ServerSocket serverSocket;

    public Server(){
        initAdmin();
        usersLock = new Object();
    }

    public void initAdmin(){
        if(new File(FILE_NAME).exists()){
            return;
        }
        List<User> users = new ArrayList<>();
        users.add(new Admin("Admin","Admin"));
        saveUsers(users);
    }

    public List<User >loadUsers(){
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_NAME))){
            return (List<User>) in.readObject();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        catch (ClassNotFoundException e){
            throw new RuntimeException(e);
        }
        return null;
    }

    private void saveUsers(List<User> users){
        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))){
            out.writeObject(users);
        }
        catch (IOException e ){
            e.printStackTrace();
        }
    }

    private void registerUser(String username, String password, UserType userType) throws CredentialsException {
        User user = UserFactory.userCreate(username,password,userType);
        synchronized (usersLock){
            List<User> users = loadUsers();
            users.add(user);
            saveUsers(users);
        }
    }

    private User login(String username, String password){
        synchronized (usersLock){
            for(User user : loadUsers()){
                if(Objects.equals(user.getUsername(), username) && Objects.equals(user.getPassword(), password)){
                    return user;
                }
            }
            return null;
        }
    }

    private void adminMenu(Scanner sc, PrintStream out, Admin admin){
        try {
            UserType userType = UserType.valueOf(sc.nextLine());

            out.println("Enter username: ");
            String uname = sc.nextLine();

            out.println("Enter password: ");
            String pass = sc.nextLine();

            registerUser(uname,pass,userType);
        }
        catch (CredentialsException e){
            e.printStackTrace();
        }
    }

    private void studentMenu(Scanner sc, PrintStream out, Student student){
        List<Grade> sortedGrades = student.getGradeList().stream()
                .sorted(Comparator.comparingInt(Grade::getSemester).thenComparing(Grade::getSubject))
                .collect(Collectors.toList());
        out.println(sortedGrades);
    }

    private void teacherMenu(Scanner sc, PrintStream out, Teacher teacher){
        out.println("Enter faculty number: ");
        String facultyNum = sc.nextLine();

        out.println("Enter subject: ");
        String Subject = sc.nextLine();

        out.println("Enter semester: ");
        int Semester = Integer.parseInt(sc.nextLine());

        out.println("Enter grade: ");
        int Grade = Integer.parseInt(sc.nextLine());

        Grade grade = new Grade(Subject, Grade, Semester);

        synchronized (usersLock){
            List<User> users = loadUsers();
            for(User user : users){
                if(user.getUsername().equals(facultyNum) && user instanceof Student){
                    Student student = (Student) user;
                    student.getGradeList().add(grade);
                    saveUsers(users);
                    return;
                }
            }
        }
    }

    private void userMenu(Scanner sc, PrintStream out){
        while(true){
            out.println("Enter username: ");
            String username = sc.nextLine();

            out.println("Enter password: ");
            String password = sc.nextLine();

            User user = login(username,password);

            switch (user.getUserType()){
                case ADMIN -> {
                    adminMenu(sc,out, (Admin) user);
                    break;
                }
                case TEACHER -> {
                    teacherMenu(sc, out, (Teacher) user);
                    break;
                }
                case STUDENT -> {
                    studentMenu(sc, out, (Student) user);
                    break;
                }
            }
        }
    }

    public void Start(){
        try{
            serverSocket = new ServerSocket(8080);
            while (true){
                Socket client = serverSocket.accept();
                Thread clientThread = new Thread(()->{
                    Scanner sc = null;
                    PrintStream out = null;

                    try {
                        sc = new Scanner(client.getInputStream());
                        out =  new PrintStream(client.getOutputStream());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                clientThread.start();
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
