package client.gui;

import model.Student;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class StudentTableModel extends AbstractTableModel {
    private static final long serialVersionUID = 1L;

    private final String[] columnNames = {"ID", "Name", "Age", "Address", "GPA"};
    private List<Student> students = new ArrayList<>();

    public void setStudents(List<Student> students) {
        this.students = students;
        fireTableDataChanged();
    }

    public Student getStudentAt(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < students.size()) {
            return students.get(rowIndex);
        }
        return null;
    }

    @Override
    public int getRowCount() {
        return students.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Student s = students.get(rowIndex);
        switch (columnIndex) {
            case 0: return s.getId();
            case 1: return s.getName();
            case 2: return s.getAge();
            case 3: return s.getAddress();
            case 4: return s.getGpa();
            default: return null;
        }
    }
}
