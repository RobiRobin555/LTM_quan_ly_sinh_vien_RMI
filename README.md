<h2 align="center">
    <a href="https://dainam.edu.vn/vi/khoa-cong-nghe-thong-tin">
    🎓 Faculty of Information Technology (DaiNam University)
    </a>
</h2>
<h2 align="center">
   QUẢN LÝ SINH VIÊN BẰNG RMI
</h2>
<div align="center">
    <p align="center">
        <img src="docs/aiotlab_logo.png" alt="AIoTLab Logo" width="170"/>
        <img src="docs/fitdnu_logo.png" alt="AIoTLab Logo" width="180"/>
        <img src="docs/dnu_logo.png" alt="DaiNam University Logo" width="200"/>
    </p>

[![AIoTLab](https://img.shields.io/badge/AIoTLab-green?style=for-the-badge)](https://www.facebook.com/DNUAIoTLab)
[![Faculty of Information Technology](https://img.shields.io/badge/Faculty%20of%20Information%20Technology-blue?style=for-the-badge)](https://dainam.edu.vn/vi/khoa-cong-nghe-thong-tin)
[![DaiNam University](https://img.shields.io/badge/DaiNam%20University-orange?style=for-the-badge)](https://dainam.edu.vn)

</div>

## 📖 1. Giới thiệu
Dự án “Quản lý Sinh viên bằng RMI” là một ứng dụng Java được xây dựng với mục tiêu minh họa cách kết hợp lập trình phân tán, xử lý dữ liệu và giao diện người dùng đồ họa trong một hệ thống hoàn chỉnh. Ở đây, công nghệ RMI được sử dụng như cầu nối cho phép client và server trao đổi dữ liệu đối tượng qua mạng. Mỗi sinh viên được mô tả bởi lớp Student, một đối tượng tuần tự hóa có thể truyền qua lại giữa các tiến trình. Server giữ vai trò trung tâm, chịu trách nhiệm quản lý toàn bộ dữ liệu sinh viên, xử lý yêu cầu từ nhiều client thông qua đa luồng và duy trì tính nhất quán khi đọc ghi dữ liệu. Thay vì sử dụng Excel, dữ liệu được lưu trữ dưới dạng tệp văn bản với cấu trúc rõ ràng, thuận tiện cho việc kiểm tra và chỉnh sửa thủ công, đồng thời giúp loại bỏ phụ thuộc vào các thư viện bên ngoài.

Ở phía client, ứng dụng Java Swing mang lại giao diện trực quan, hỗ trợ người dùng thực hiện các thao tác thêm, sửa, xóa và tìm kiếm sinh viên thông qua các bảng dữ liệu và hộp thoại nhập liệu. Mọi hành động của người dùng trên giao diện đều được chuyển thành lời gọi phương thức từ xa tới server, nơi logic nghiệp vụ và xử lý lưu trữ thực sự diễn ra. Nhờ cơ chế đa luồng sẵn có của RMI, server có thể phục vụ nhiều client đồng thời mà không ảnh hưởng đến hiệu năng.

Dự án không chỉ mang tính thực hành trong việc áp dụng RMI và Serialization mà còn cho thấy tầm quan trọng của việc thiết kế phân lớp rõ ràng giữa model, service, util, server và client. Nó đồng thời nhấn mạnh những vấn đề thực tế như quản lý đồng bộ truy cập file, xử lý ngoại lệ trong môi trường phân tán và tránh treo giao diện khi gọi các hàm mạng. Đây là một nền tảng tốt để phát triển các hệ thống quản lý lớn hơn, nơi ta có thể mở rộng bằng việc thay thế tệp văn bản bằng cơ sở dữ liệu quan hệ, bổ sung xác thực người dùng hoặc tích hợp cơ chế logging chuyên nghiệp.

## 🔧 2. Ngôn ngữ lập trình sử dụng: [![Java](https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=java&logoColor=white)](https://www.java.com/)

## 🚀 3. Cấu trúc dự án 
QuanLySinhVienRMI/
├─ src/
│  ├─ model/Student.java
│  ├─ service/StudentService.java
│  ├─ service/StudentServiceImpl.java
│  ├─ server/ServerMain.java
│  ├─ client/ClientMain.java
│  └─ client/gui/
│     ├─ StudentManagementUI.java
│     ├─ StudentFormDialog.java
│     └─ StudentTableModel.java
│  └─ util/TxtHelper.java
├─ data/students.txt
└─ README.md

## 📝 4. License

© 2025 AIoTLab, Faculty of Information Technology, DaiNam University. All rights reserved.

---
