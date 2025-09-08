package client.gui;

import model.Student;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class StudentFormDialog extends JDialog {
    private static final long serialVersionUID = 1L;

    private JTextField txtId, txtName, txtAge, txtAddress, txtGpa;
    private Student student;

    public StudentFormDialog(Frame parent, String title, Student existing) {
        super(parent, title, true);
        initUI(existing);
    }

    private void initUI(Student existing) {
        setSize(400, 300);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("ID:"));
        txtId = new JTextField();
        panel.add(txtId);

        panel.add(new JLabel("Tên:"));
        txtName = new JTextField();
        panel.add(txtName);

        panel.add(new JLabel("Tuổi:"));
        txtAge = new JTextField();
        panel.add(txtAge);

        panel.add(new JLabel("Địa chỉ:"));
        txtAddress = new JTextField();
        panel.add(txtAddress);

        panel.add(new JLabel("GPA:"));
        txtGpa = new JTextField();
        panel.add(txtGpa);

        // Nút bấm
        JPanel buttonPanel = new JPanel();
        JButton btnOk = new JButton("OK");
        JButton btnCancel = new JButton("Hủy");

        buttonPanel.add(btnOk);
        buttonPanel.add(btnCancel);

        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Nếu sửa, đổ dữ liệu cũ vào
        if (existing != null) {
            txtId.setText(existing.getId());
            txtId.setEditable(false); // không cho sửa ID
            txtName.setText(existing.getName());
            txtAge.setText(String.valueOf(existing.getAge()));
            txtAddress.setText(existing.getAddress());
            txtGpa.setText(String.valueOf(existing.getGpa()));
        }

        // Gắn sự kiện
        btnOk.addActionListener(this::onOk);
        btnCancel.addActionListener(e -> {
            student = null;
            dispose();
        });
    }

    private void onOk(ActionEvent e) {
        try {
            String id = txtId.getText().trim();
            String name = txtName.getText().trim();
            int age = Integer.parseInt(txtAge.getText().trim());
            String address = txtAddress.getText().trim();
            double gpa = Double.parseDouble(txtGpa.getText().trim());

            if (id.isEmpty() || name.isEmpty() || address.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
                return;
            }

            student = new Student(id, name, age, address, gpa);
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Tuổi và GPA phải là số hợp lệ!");
        }
    }

    public Student getStudent() {
        return student;
    }
}
