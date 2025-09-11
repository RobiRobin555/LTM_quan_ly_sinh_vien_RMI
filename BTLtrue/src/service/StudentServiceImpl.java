package service;
import model.Student;
import util.TxtHelper;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class StudentServiceImpl extends UnicastRemoteObject implements StudentService {
    private static final long serialVersionUID = 1L;
    private static final String FILE_PATH = "data/students.txt";

    public StudentServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public List<Student> getAllStudents() throws RemoteException {
        return TxtHelper.readStudentsFromTxt(FILE_PATH);
    }

    @Override
    public void addStudent(Student student) throws RemoteException {
        List<Student> students = TxtHelper.readStudentsFromTxt(FILE_PATH);
        students.add(student);
        TxtHelper.writeStudentsToTxt(FILE_PATH, students);
    }

    @Override
    public void updateStudent(Student student) throws RemoteException {
        List<Student> students = TxtHelper.readStudentsFromTxt(FILE_PATH);
        boolean updated = false;
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getId().equals(student.getId())) {
                students.set(i, student);
                updated = true;
                break;
            }
        }
        if (updated) {
            TxtHelper.writeStudentsToTxt(FILE_PATH, students);
        }
    }

    @Override
    public void deleteStudent(String id) throws RemoteException {
        List<Student> students = TxtHelper.readStudentsFromTxt(FILE_PATH);
        students.removeIf(s -> s.getId().equals(id));
        TxtHelper.writeStudentsToTxt(FILE_PATH, students);
    }

    @Override
    public Student findStudentById(String id) throws RemoteException {
        List<Student> students = TxtHelper.readStudentsFromTxt(FILE_PATH);
        for (Student s : students) {
            if (s.getId().equals(id)) {
                return s;
            }
        }
        return null;
    }
}
