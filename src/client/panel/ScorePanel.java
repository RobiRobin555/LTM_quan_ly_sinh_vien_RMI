package client.panel;

import model.Score;
import service.ScoreService;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.List;

public class ScorePanel extends JPanel {

    private final ScoreService scoreService;
    private JTable scoreTable;
    private DefaultTableModel model;

    public ScorePanel(ScoreService scoreService) {
        this.scoreService = scoreService;

        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(230, 240, 255));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // ============================
        // Tiêu đề
        // ============================
        JLabel title = new JLabel("📊 Quản Lý Điểm", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(41, 128, 185));
        add(title, BorderLayout.NORTH);

        // ============================
        // Bảng dữ liệu
        // ============================
        model = new DefaultTableModel(new Object[]{"ID", "Mã SV", "Mã Môn", "Điểm"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        scoreTable = new JTable(model);
        scoreTable.setRowHeight(30);
        scoreTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        scoreTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        scoreTable.getTableHeader().setBackground(new Color(52, 152, 219));
        scoreTable.getTableHeader().setForeground(Color.WHITE);
        scoreTable.setSelectionBackground(new Color(155, 203, 255));
        scoreTable.setBorder(new LineBorder(new Color(200, 200, 200)));

        JScrollPane scrollPane = new JScrollPane(scoreTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 1));
        add(scrollPane, BorderLayout.CENTER);

        // ============================
        // Thanh công cụ
        // ============================
        JPanel toolPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        toolPanel.setBackground(new Color(230, 240, 255));

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
        loadScores();

        // ============================
        // Sự kiện
        // ============================
        btnAdd.addActionListener(e -> addScoreDialog());
        btnEdit.addActionListener(e -> editScoreDialog());
        btnDelete.addActionListener(e -> deleteScoreAction());
        btnRefresh.addActionListener(e -> loadScores());
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

    private void loadScores() {
        try {
            model.setRowCount(0);
            List<Score> scores = scoreService.getAllScores();
            for (Score s : scores) {
                model.addRow(new Object[]{s.getId(), s.getMsv(), s.getMaMon(), s.getDiem()});
            }
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(this, "❌ Lỗi khi tải điểm", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addScoreDialog() {
        JTextField txtMsv = new JTextField();
        JTextField txtMaMon = new JTextField();
        JTextField txtDiem = new JTextField();

        JPanel panel = createFormPanel(
                new String[]{"Mã SV:", "Mã môn:", "Điểm:"},
                new JComponent[]{txtMsv, txtMaMon, txtDiem}
        );

        int result = JOptionPane.showConfirmDialog(this, panel,
                "➕ Thêm Điểm", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                Score s = new Score(txtMsv.getText(), txtMaMon.getText(), Double.parseDouble(txtDiem.getText()));
                scoreService.addScore(s);
                JOptionPane.showMessageDialog(this, "✅ Thêm điểm thành công!");
                loadScores();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Lỗi thêm điểm: " + ex.getMessage());
            }
        }
    }

    private void editScoreDialog() {
        int row = scoreTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "⚠ Vui lòng chọn điểm để sửa!");
            return;
        }

        int id = Integer.parseInt(model.getValueAt(row, 0).toString());
        JTextField txtMsv = new JTextField(model.getValueAt(row, 1).toString());
        JTextField txtMaMon = new JTextField(model.getValueAt(row, 2).toString());
        JTextField txtDiem = new JTextField(model.getValueAt(row, 3).toString());

        JPanel panel = createFormPanel(
                new String[]{"Mã SV:", "Mã môn:", "Điểm:"},
                new JComponent[]{txtMsv, txtMaMon, txtDiem}
        );

        int result = JOptionPane.showConfirmDialog(this, panel,
                "✏️ Sửa Điểm", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                Score s = new Score(id, txtMsv.getText(), txtMaMon.getText(), Double.parseDouble(txtDiem.getText()));
                scoreService.updateScore(s);
                JOptionPane.showMessageDialog(this, "✅ Cập nhật thành công!");
                loadScores();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Lỗi sửa điểm: " + ex.getMessage());
            }
        }
    }

    private void deleteScoreAction() {
        int row = scoreTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "⚠ Vui lòng chọn điểm để xóa!");
            return;
        }

        int id = Integer.parseInt(model.getValueAt(row, 0).toString());
        String maSV = model.getValueAt(row, 1).toString();
        String maMon = model.getValueAt(row, 2).toString();

        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa điểm ID " + id + " (SV: " + maSV + ", Môn: " + maMon + ")?",
                "Xác nhận", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                scoreService.deleteScore(id);
                JOptionPane.showMessageDialog(this, "🗑 Xóa thành công!");
                loadScores();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "❌ Lỗi xóa điểm: " + e.getMessage());
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
