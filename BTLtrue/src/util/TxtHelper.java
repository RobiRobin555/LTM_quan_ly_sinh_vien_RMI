package util;

import model.Student;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TxtHelper {
    private static final String HEADER = "ID;Name;Age;Address;GPA";

    // Đọc danh sách sinh viên từ file txt
    public static List<Student> readStudentsFromTxt(String filePath) {
        List<Student> students = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) {
            writeStudentsToTxt(filePath, new ArrayList<>()); // tạo file rỗng nếu chưa tồn tại
            return students;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isHeader = true;
            while ((line = br.readLine()) != null) {
                if (isHeader) { // bỏ qua dòng header
                    isHeader = false;
                    continue;
                }
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(";");
                if (parts.length == 5) {
                    String id = parts[0];
                    String name = parts[1];
                    int age = Integer.parseInt(parts[2]);
                    String address = parts[3];
                    double gpa = Double.parseDouble(parts[4]);

                    students.add(new Student(id, name, age, address, gpa));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return students;
    }

    // Ghi danh sách sinh viên ra file txt
    public static void writeStudentsToTxt(String filePath, List<Student> students) {
        File file = new File(filePath);
        file.getParentFile().mkdirs(); // tạo thư mục nếu chưa có

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            // Ghi header
            bw.write(HEADER);
            bw.newLine();

            // Ghi dữ liệu
            for (Student s : students) {
                bw.write(String.format("%s;%s;%d;%s;%.2f",
                        s.getId(), s.getName(), s.getAge(), s.getAddress(), s.getGpa()));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
