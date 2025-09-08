package client;

import java.rmi.Naming;

import service.StudentService;
import client.gui.StudentManagementUI;

public class ClientMain {
    public static void main(String[] args) {
        try {
            // URL của service (phải khớp với bên ServerMain)
            String url = "rmi://localhost:1099/StudentService";

            // Lookup đối tượng từ RMI Registry
            StudentService studentService = (StudentService) Naming.lookup(url);

            System.out.println(">>> Client đã kết nối thành công tới Server.");

            // Khởi chạy giao diện Swing, truyền vào service
            javax.swing.SwingUtilities.invokeLater(() -> {
                StudentManagementUI ui = new StudentManagementUI(studentService);
                ui.setVisible(true);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
