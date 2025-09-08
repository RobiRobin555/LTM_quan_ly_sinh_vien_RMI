package service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import model.Student;

public interface StudentService extends Remote {

    // Lấy danh sách tất cả sinh viên
    List<Student> getAllStudents() throws RemoteException;

    // Thêm sinh viên mới
    void addStudent(Student student) throws RemoteException;

    // Cập nhật thông tin sinh viên
    void updateStudent(Student student) throws RemoteException;

    // Xóa sinh viên theo ID
    void deleteStudent(String id) throws RemoteException;

    // Tìm sinh viên theo ID
    Student findStudentById(String id) throws RemoteException;
}
