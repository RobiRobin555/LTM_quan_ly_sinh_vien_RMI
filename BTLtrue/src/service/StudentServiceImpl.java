package service;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import model.Student;
import util.ExcelHelper;

public class StudentServiceImpl extends UnicastRemoteObject implements StudentService {
    private static final long serialVersionUID = 1L;

    private final String filePath = "data/students.xlsx";
    private final Object lock = new Object();  // đảm bảo đồng bộ khi multi-thread

    public StudentServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public List<Student> getAllStudents() throws RemoteException {
        synchronized (lock) {
            return ExcelHelper.readStudentsFromExcel(filePath);
        }
    }

    @Override
    public void addStudent(Student student) throws RemoteException {
        synchronized (lock) {
            List<Student> students = ExcelHelper.readStudentsFromExcel(filePath);
            students.add(student);
            ExcelHelper.writeStudentsToExcel(filePath, students);
        }
    }

    @Override
    public void updateStudent(Student student) throws RemoteException {
        synchronized (lock) {
            List<Student> students = ExcelHelper.readStudentsFromExcel(filePath);
            for (int i = 0; i < students.size(); i++) {
                if (students.get(i).getId().equals(student.getId())) {
                    students.set(i, student);
                    break;
                }
            }
            ExcelHelper.writeStudentsToExcel(filePath, students);
        }
    }

    @Override
    public void deleteStudent(String id) throws RemoteException {
        synchronized (lock) {
            List<Student> students = ExcelHelper.readStudentsFromExcel(filePath);
            Iterator<Student> it = students.iterator();
            while (it.hasNext()) {
                if (it.next().getId().equals(id)) {
                    it.remove();
                    break;
                }
            }
            ExcelHelper.writeStudentsToExcel(filePath, students);
        }
    }

    @Override
    public Student findStudentById(String id) throws RemoteException {
        synchronized (lock) {
            List<Student> students = ExcelHelper.readStudentsFromExcel(filePath);
            for (Student s : students) {
                if (s.getId().equals(id)) {
                    return s;
                }
            }
        }
        return null;
    }
}
