package client.panel;

import model.Subject;
import service.SubjectService;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.List;

public class SubjectPanel extends JPanel {

    private final SubjectService subjectService;
    private JTable subjectTable;
    private DefaultTableModel model;

    public SubjectPanel(SubjectService subjectService) {
        this.subjectService = subjectService;

        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(240, 248, 255));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // ============================
        // Tiêu đề
        // ============================
        JLabel title = new JLabel("📚 Quản Lý Môn Học", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(41, 128, 185));
        add(title, BorderLayout.NORTH);

        // ============================
        // Bảng dữ liệu
        // ============================
        model = new DefaultTableModel(new Object[]{"Mã môn", "Tên môn", "Số lượng ĐK", "Số tín chỉ"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        subjectTable = new JTable(model);
        subjectTable.setRowHeight(28);
        subjectTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subjectTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        subjectTable.getTableHeader().setBackground(new Color(52, 152, 219));
        subjectTable.getTableHeader().setForeground(Color.WHITE);
        subjectTable.setSelectionBackground(new Color(155, 203, 255));
        subjectTable.setBorder(new LineBorder(new Color(200, 200, 200)));

        JScrollPane scrollPane = new JScrollPane(subjectTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 1));
        add(scrollPane, BorderLayout.CENTER);

        // ============================
        // Thanh công cụ
        // ============================
        JPanel toolPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        toolPanel.setBackground(new Color(240, 248, 255));

        JButton btnAdd = createButton("➕ Thêm", new Color(46, 204, 113));
        JButton btnEdit = createButton("✏️ Sửa", new Color(241, 196, 15));
        JButton btnDelete = createButton("🗑 Xóa", new Color(231, 76, 60));
        JButton btnRefresh = createButton("🔄 Làm mới", new Color(52, 152, 219));

        toolPanel.add(btnAdd);
        toolPanel.add(btnEdit);
        toolPanel.add(btnDelete);
        toolPanel.add(btnRefresh);

        add(toolPanel, BorderLayout.SOUTH);

        // ============================
        // Load dữ liệu
        // ============================
        loadSubjects();

        // ============================
        // Sự kiện
        // ============================
        btnAdd.addActionListener(e -> addSubjectDialog());
        btnEdit.addActionListener(e -> editSubjectDialog());
        btnDelete.addActionListener(e -> deleteSubjectAction());
        btnRefresh.addActionListener(e -> loadSubjects());
    }

    private JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setPreferredSize(new Dimension(120, 38));
        btn.setBorder(new LineBorder(Color.DARK_GRAY, 1, true));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void loadSubjects() {
        try {
            model.setRowCount(0);
            List<Subject> subjects = subjectService.getAllSubjects();
            for (Subject s : subjects) {
                model.addRow(new Object[]{s.getMaMon(), s.getTenMon(), s.getSoLuongDangKy(), s.getSoTinChi()});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "❌ Lỗi load môn học: " + e.getMessage());
        }
    }

    private void addSubjectDialog() {
        JTextField txtMaMon = new JTextField();
        JTextField txtTenMon = new JTextField();
        JTextField txtSoTinChi = new JTextField();

        JPanel panel = createFormPanel(
                new String[]{"Mã môn:", "Tên môn:", "Số tín chỉ:"},
                new JComponent[]{txtMaMon, txtTenMon, txtSoTinChi}
        );

        int result = JOptionPane.showConfirmDialog(this, panel,
                "➕ Thêm Môn Học", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                Subject s = new Subject(txtMaMon.getText(), txtTenMon.getText(), 0, Integer.parseInt(txtSoTinChi.getText()));
                subjectService.addSubject(s);
                JOptionPane.showMessageDialog(this, "✅ Thêm môn học thành công!");
                loadSubjects();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Lỗi thêm môn học: " + ex.getMessage());
            }
        }
    }

    private void editSubjectDialog() {
        int row = subjectTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "⚠ Vui lòng chọn môn học để sửa!");
            return;
        }

        JTextField txtMaMon = new JTextField(model.getValueAt(row, 0).toString());
        txtMaMon.setEditable(false);
        JTextField txtTenMon = new JTextField(model.getValueAt(row, 1).toString());
        JTextField txtSoTinChi = new JTextField(model.getValueAt(row, 3).toString());

        JPanel panel = createFormPanel(
                new String[]{"Mã môn:", "Tên môn:", "Số tín chỉ:"},
                new JComponent[]{txtMaMon, txtTenMon, txtSoTinChi}
        );

        int result = JOptionPane.showConfirmDialog(this, panel,
                "✏️ Sửa Môn Học", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                Subject s = new Subject(txtMaMon.getText(), txtTenMon.getText(), 0, Integer.parseInt(txtSoTinChi.getText()));
                subjectService.updateSubject(s);
                JOptionPane.showMessageDialog(this, "✅ Cập nhật môn học thành công!");
                loadSubjects();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Lỗi cập nhật môn học: " + ex.getMessage());
            }
        }
    }

    private void deleteSubjectAction() {
        int row = subjectTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "⚠ Vui lòng chọn môn học để xóa!");
            return;
        }

        String maMon = model.getValueAt(row, 0).toString();

        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa môn học " + maMon + "?",
                "Xác nhận", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                subjectService.deleteSubject(maMon);
                JOptionPane.showMessageDialog(this, "🗑 Xóa thành công!");
                loadSubjects();
            } catch (RemoteException e) {
                JOptionPane.showMessageDialog(this, "❌ Lỗi xóa môn học: " + e.getMessage());
            }
        }
    }

    private JPanel createFormPanel(String[] labels, JComponent[] fields) {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        for (int i = 0; i < labels.length; i++) {
            JLabel lbl = new JLabel(labels[i]);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
            panel.add(lbl);
            panel.add(fields[i]);
        }
        return panel;
    }
}
