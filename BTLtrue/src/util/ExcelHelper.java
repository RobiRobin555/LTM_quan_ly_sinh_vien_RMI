package util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import model.Student;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelHelper {

    // Đọc danh sách sinh viên từ file Excel
    public static List<Student> readStudentsFromExcel(String filePath) {
        List<Student> students = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) { // bỏ qua header
                Row row = sheet.getRow(i);
                if (row != null) {
                    String id = row.getCell(0).getStringCellValue();
                    String name = row.getCell(1).getStringCellValue();
                    int age = (int) row.getCell(2).getNumericCellValue();
                    String address = row.getCell(3).getStringCellValue();
                    double gpa = row.getCell(4).getNumericCellValue();

                    students.add(new Student(id, name, age, address, gpa));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return students;
    }

    // Ghi danh sách sinh viên ra file Excel
    public static void writeStudentsToExcel(String filePath, List<Student> students) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Students");

            // Tạo header
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("ID");
            header.createCell(1).setCellValue("Name");
            header.createCell(2).setCellValue("Age");
            header.createCell(3).setCellValue("Address");
            header.createCell(4).setCellValue("GPA");

            // Ghi dữ liệu sinh viên
            int rowIndex = 1;
            for (Student s : students) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(s.getId());
                row.createCell(1).setCellValue(s.getName());
                row.createCell(2).setCellValue(s.getAge());
                row.createCell(3).setCellValue(s.getAddress());
                row.createCell(4).setCellValue(s.getGpa());
            }

            // Ghi ra file
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
