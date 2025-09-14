package client.panel;

import model.Score;
import model.Student;
import model.Subject;
import service.ScoreService;
import service.StudentService;
import service.SubjectService;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class StudentPanel extends JPanel {

    private final StudentService studentService;
    private final SubjectService subjectService;
    private final ScoreService scoreService;
    private JTable studentTable;
    private DefaultTableModel studentModel;

    public StudentPanel(StudentService studentService, SubjectService subjectService, ScoreService scoreService) {
        this.studentService = studentService;
        this.subjectService = subjectService;
        this.scoreService = scoreService;

        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(52, 152, 219), 2, true),
                "📚 Quản lý Sinh viên",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 18),
                new Color(41, 128, 185)
        ));

        // Bảng Sinh viên
        studentModel = new DefaultTableModel(
                new Object[]{"MSV", "Tên", "Ngày sinh", "Quê quán", "Điểm TB"}, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        studentTable = new JTable(studentModel);
        studentTable.setRowHeight(30);
        studentTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        studentTable.setShowHorizontalLines(true);
        studentTable.setShowVerticalLines(false);
        studentTable.setGridColor(new Color(220, 220, 220));
        studentTable.setSelectionBackground(new Color(52, 152, 219));
        studentTable.setSelectionForeground(Color.WHITE);

        studentTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        studentTable.getTableHeader().setBackground(new Color(41, 128, 185));
        studentTable.getTableHeader().setForeground(Color.WHITE);

        // Căn giữa cột Điểm TB
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        studentTable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);

        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        // Nút chức năng
        JButton btnAdd = createStyledButton("➕ Thêm", new Color(46, 204, 113));
        JButton btnEdit = createStyledButton("✏️ Sửa", new Color(241, 196, 15));
        JButton btnDelete = createStyledButton("🗑 Xóa", new Color(231, 76, 60));

        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        south.setBackground(new Color(245, 247, 250));
        south.add(btnAdd);
        south.add(btnEdit);
        south.add(btnDelete);
        add(south, BorderLayout.SOUTH);

        loadStudents();

        btnAdd.addActionListener(e -> addStudentDialog());
        btnEdit.addActionListener(e -> {
            int row = studentTable.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "⚠ Vui lòng chọn SV để sửa!");
                return;
            }
            Student s = getStudentFromRow(row);
            editStudentDialog(s);
        });
        btnDelete.addActionListener(e -> deleteStudentAction());

        studentTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2 && studentTable.getSelectedRow() != -1) {
                    int row = studentTable.getSelectedRow();
                    Student s = getStudentFromRow(row);
                    showStudentDetail(s);
                }
            }
        });
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(120, 38));
        return button;
    }

    private Student getStudentFromRow(int row) {
        String msv = (String) studentModel.getValueAt(row, 0);
        String ten = (String) studentModel.getValueAt(row, 1);
        LocalDate ngaySinh = LocalDate.parse(studentModel.getValueAt(row, 2).toString());
        String queQuan = (String) studentModel.getValueAt(row, 3);
        double diemTB = Double.parseDouble(studentModel.getValueAt(row, 4).toString());
        return new Student(msv, ten, ngaySinh, queQuan, diemTB);
    }

    private void loadStudents() {
        try {
            List<Student> students = studentService.getAllStudents();
            studentModel.setRowCount(0);
            for (Student s : students) {
                studentModel.addRow(new Object[]{s.getMsv(), s.getTen(), s.getNgaySinh(),
                        s.getQueQuan(), s.getDiemTB()});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi load dữ liệu: " + e.getMessage());
        }
    }

    private void addStudentDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "➕ Thêm Sinh viên", true);
        dialog.setSize(450, 320);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridLayout(5, 2, 12, 12));
        dialog.getContentPane().setBackground(new Color(250, 250, 252));

        JTextField txtMsv = new JTextField();
        JTextField txtTen = new JTextField();
        JTextField txtNgaySinh = new JTextField("2000-01-01");
        JTextField txtQueQuan = new JTextField();

        dialog.add(new JLabel("Mã SV:"));
        dialog.add(txtMsv);
        dialog.add(new JLabel("Tên:"));
        dialog.add(txtTen);
        dialog.add(new JLabel("Ngày sinh (yyyy-MM-dd):"));
        dialog.add(txtNgaySinh);
        dialog.add(new JLabel("Quê quán:"));
        dialog.add(txtQueQuan);

        JButton btnSave = createStyledButton("💾 Lưu", new Color(39, 174, 96));
        JButton btnCancel = createStyledButton("❌ Hủy", new Color(149, 165, 166));

        dialog.add(btnSave);
        dialog.add(btnCancel);

        btnSave.addActionListener(e -> {
            try {
                Student s = new Student(
                        txtMsv.getText(),
                        txtTen.getText(),
                        LocalDate.parse(txtNgaySinh.getText()),
                        txtQueQuan.getText(),
                        0.0
                );
                studentService.addStudent(s);
                JOptionPane.showMessageDialog(dialog, "✅ Thêm thành công!");
                loadStudents();
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Lỗi: " + ex.getMessage());
            }
        });

        btnCancel.addActionListener(e -> dialog.dispose());
        dialog.setVisible(true);
    }

    private void editStudentDialog(Student s) {
        if (s == null) return;

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "✏️ Sửa Sinh viên", true);
        dialog.setSize(450, 320);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridLayout(5, 2, 12, 12));
        dialog.getContentPane().setBackground(new Color(250, 250, 252));

        JTextField txtMsv = new JTextField(s.getMsv());
        txtMsv.setEditable(false);
        JTextField txtTen = new JTextField(s.getTen());
        JTextField txtNgaySinh = new JTextField(s.getNgaySinh().toString());
        JTextField txtQueQuan = new JTextField(s.getQueQuan());

        dialog.add(new JLabel("Mã SV:"));
        dialog.add(txtMsv);
        dialog.add(new JLabel("Tên:"));
        dialog.add(txtTen);
        dialog.add(new JLabel("Ngày sinh (yyyy-MM-dd):"));
        dialog.add(txtNgaySinh);
        dialog.add(new JLabel("Quê quán:"));
        dialog.add(txtQueQuan);

        JButton btnSave = createStyledButton("💾 Lưu", new Color(41, 128, 185));
        JButton btnCancel = createStyledButton("❌ Hủy", new Color(149, 165, 166));

        dialog.add(btnSave);
        dialog.add(btnCancel);

        btnSave.addActionListener(e -> {
            try {
                Student updated = new Student(
                        s.getMsv(),
                        txtTen.getText(),
                        LocalDate.parse(txtNgaySinh.getText()),
                        txtQueQuan.getText(),
                        s.getDiemTB()
                );
                studentService.updateStudent(updated);
                JOptionPane.showMessageDialog(dialog, "✅ Cập nhật thành công!");
                loadStudents();
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Lỗi khi cập nhật: " + ex.getMessage());
            }
        });

        btnCancel.addActionListener(e -> dialog.dispose());
        dialog.setVisible(true);
    }

    private void deleteStudentAction() {
        int row = studentTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Chọn SV để xóa");
            return;
        }
        String msv = (String) studentModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Xóa SV " + msv + " ?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                studentService.deleteStudent(msv);
                loadStudents();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi xóa SV: " + e.getMessage());
            }
        }
    }

    private void showStudentDetail(Student s) {
        if (s == null) return;

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "📖 Chi tiết Sinh viên", true);
        dialog.setSize(750, 480);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.getContentPane().setBackground(new Color(250, 250, 252));

        JPanel infoPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        infoPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(41, 128, 185)),
                "Thông tin",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(41, 128, 185)
        ));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.add(new JLabel("Mã SV: " + s.getMsv()));
        infoPanel.add(new JLabel("Tên: " + s.getTen()));
        infoPanel.add(new JLabel("Ngày sinh: " + s.getNgaySinh()));
        infoPanel.add(new JLabel("Quê quán: " + s.getQueQuan()));
        infoPanel.add(new JLabel("Điểm TB: " + s.getDiemTB()));
        dialog.add(infoPanel, BorderLayout.NORTH);

        DefaultTableModel scoreModel = new DefaultTableModel(new Object[]{"Mã môn", "Tên môn", "Điểm"}, 0);
        JTable scoreTable = new JTable(scoreModel);
        scoreTable.setRowHeight(28);
        scoreTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        scoreTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        scoreTable.getTableHeader().setBackground(new Color(52, 152, 219));
        scoreTable.getTableHeader().setForeground(Color.WHITE);

        try {
            List<Score> scores = scoreService.getAllScores();
            for (Score sc : scores) {
                if (sc.getMsv().equals(s.getMsv())) {
                    Subject subj = subjectService.findSubjectById(sc.getMaMon());
                    String tenMon = (subj != null) ? subj.getTenMon() : "N/A";
                    scoreModel.addRow(new Object[]{sc.getMaMon(), tenMon, sc.getDiem()});
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi load điểm: " + ex.getMessage());
        }
        dialog.add(new JScrollPane(scoreTable), BorderLayout.CENTER);

        JButton btnClose = createStyledButton("Đóng", new Color(127, 140, 141));
        btnClose.addActionListener(e -> dialog.dispose());
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setBackground(new Color(250, 250, 252));
        bottom.add(btnClose);
        dialog.add(bottom, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }
}
