package client.gui;

import service.StudentService;
import model.Student;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.rmi.RemoteException;

public class StudentManagementUI extends JFrame {
    private static final long serialVersionUID = 1L;

    private StudentService studentService;
    private JTable table;
    private StudentTableModel tableModel;

    public StudentManagementUI(StudentService studentService) {
        this.studentService = studentService;
        initUI();
        loadStudents();
    }

    private void initUI() {
        setTitle("Quản lý Sinh viên - RMI");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Bảng hiển thị sinh viên
        tableModel = new StudentTableModel();
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // Panel nút bấm
        JPanel buttonPanel = new JPanel();
        JButton btnAdd = new JButton("Thêm");
        JButton btnEdit = new JButton("Sửa");
        JButton btnDelete = new JButton("Xóa");
        JButton btnRefresh = new JButton("Làm mới");
        JButton btnFind = new JButton("Tìm kiếm");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnFind);

        // Gắn sự kiện
        btnAdd.addActionListener(this::onAdd);
        btnEdit.addActionListener(this::onEdit);
        btnDelete.addActionListener(this::onDelete);
        btnRefresh.addActionListener(e -> loadStudents());
        btnFind.addActionListener(this::onFind);

        // Layout
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadStudents() {
        try {
            tableModel.setStudents(studentService.getAllStudents());
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu: " + e.getMessage());
        }
    }

    private void onAdd(ActionEvent e) {
        StudentFormDialog dialog = new StudentFormDialog(this, "Thêm sinh viên", null);
        dialog.setVisible(true);
        Student s = dialog.getStudent();
        if (s != null) {
            try {
                studentService.addStudent(s);
                loadStudents();
            } catch (RemoteException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi thêm: " + ex.getMessage());
            }
        }
    }

    private void onEdit(ActionEvent e) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            Student existing = tableModel.getStudentAt(selectedRow);
            try {
                if (existing != null) {
                    StudentFormDialog dialog = new StudentFormDialog(this, "Sửa sinh viên", existing);
                    dialog.setVisible(true);
                    Student updated = dialog.getStudent();
                    if (updated != null) {
                        studentService.updateStudent(updated);
                        loadStudents();
                    }
                }
            } catch (RemoteException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi sửa: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sinh viên cần sửa!");
        }
    }

    private void onDelete(ActionEvent e) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            Student s = tableModel.getStudentAt(selectedRow);
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc muốn xóa sinh viên có ID: " + s.getId() + "?",
                    "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    studentService.deleteStudent(s.getId());
                    loadStudents();
                } catch (RemoteException ex) {
                    JOptionPane.showMessageDialog(this, "Lỗi xóa: " + ex.getMessage());
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sinh viên cần xóa!");
        }
    }

    private void onFind(ActionEvent e) {
        String id = JOptionPane.showInputDialog(this, "Nhập ID sinh viên cần tìm:");
        if (id != null && !id.trim().isEmpty()) {
            try {
                Student s = studentService.findStudentById(id.trim());
                if (s != null) {
                    JOptionPane.showMessageDialog(this,
                            String.format("Tìm thấy: %s - %s - %d - %s - %.2f",
                                    s.getId(), s.getName(), s.getAge(), s.getAddress(), s.getGpa()));
                } else {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy sinh viên có ID: " + id);
                }
            } catch (RemoteException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi tìm kiếm: " + ex.getMessage());
            }
        }
    }
}
