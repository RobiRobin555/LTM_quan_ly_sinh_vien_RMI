package server;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import service.StudentService;
import service.StudentServiceImpl;

public class ServerMain {
    public static void main(String[] args) {
        try {
            // Khởi động RMI Registry tại cổng 1099 (nếu chưa chạy)
            try {
                LocateRegistry.createRegistry(1099);
                System.out.println(">>> RMI Registry đã được khởi tạo tại cổng 1099.");
            } catch (RemoteException e) {
                System.out.println(">>> RMI Registry đã tồn tại, bỏ qua khởi tạo mới.");
            }

            // Tạo service
            StudentService studentService = new StudentServiceImpl();

            // Đăng ký đối tượng với RMI Registry qua URL
            String url = "rmi://localhost:1099/StudentService";
            Naming.rebind(url, studentService);

            System.out.println(">>> Server đã khởi động và bind service tại: " + url);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
