# BÀI TẬP 07 - PHẦN 02: RESTFUL API & AJAX VỚI SPRING BOOT 3

**Thông Tin Sinh Viên**:
- **Họ và Tên**: Huỳnh Cao Trung Đức
- **Mã số sinh viên (MSSV)**: 24133012
- **Môn học**: Lập trình Web (WEBPR330479)
- **Giảng viên hướng dẫn**: ThS. Nguyễn Hữu Trung
- **Khoa**: Công nghệ Thông tin - Trường Đại học Công phạm Kỹ thuật TP.HCM (HCM-UTE)
- **Repository GitHub**: [duchuynhct/-24133012-HuynhCaoTrungDuc-BaiTap07-phan02](https://github.com/duchuynhct/-24133012-HuynhCaoTrungDuc-BaiTap07-phan02)

---

## 1. Cấu Trúc Nhánh Git Theo Yêu Cầu Đề Bài

Repository được tổ chức thành các nhánh (branches) độc lập và rõ ràng theo từng nội dung yêu cầu của bài tập:

| STT | Tên Nhánh Git | Nội Dung / Yêu Cầu Tương Ứng |
| :---: | :--- | :--- |
| 1 | **`crud-api-category-spring-boot-3`** | **Mục 3**: Xây dựng thực thể Category & Product, File Storage Service và hoàn thiện hệ thống RESTful API CRUD cho Category trên Spring Boot 3. |
| 2 | **`cau-hinh-swagger-3-tren-spring-boot`** | **Mục 4**: Tích hợp và cấu hình tài liệu Swagger 3 / OpenAPI 3 (`springdoc-openapi-starter-webmvc-ui`) để test trực tiếp các API. |
| 3 | **`api-render-ajax-crud-product-category`** | **Mục 5**: Xây dựng giao diện tương tác bằng AJAX (jQuery + Bootstrap 5 Modal) cho Category, đồng thời mở rộng hoàn thiện REST API và giao diện AJAX CRUD cho Product. |
| 4 | **`main`** | Nhánh chính tổng hợp toàn bộ bài làm, mã nguồn hoàn chỉnh, cấu hình chạy và tài liệu hướng dẫn. |

---

## 2. Công Nghệ Sử Dụng

- **Ngôn ngữ & Môi trường**: Java 26 (tương thích hoàn toàn từ JDK 17, 21 đến JDK 26)
- **Framework**: Spring Boot 3.2.5
  - Spring Web MVC, Spring Data JPA, Hibernate, Validation
  - Springdoc OpenAPI 3 (v2.3.0)
  - Apache Commons IO (v2.11.0)
- **Cơ sở dữ liệu**: Microsoft SQL Server
- **Frontend**: HTML5, Thymeleaf, Bootstrap 5.3.2, FontAwesome 6, jQuery 3.7.1 AJAX

---

## 3. Cấu Hình Cơ Sở Dữ Liệu & Khởi Chạy Ứng Dụng

### Cấu hình `application.properties`:
- Cổng chạy: `server.port=8082`
- Chuỗi kết nối CSDL SQL Server:
  ```properties
  spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=webst2;encrypt=false;trustServerCertificate=true;sslProtocol=TLSv1.2;characterEncoding=UTF-8
  spring.datasource.username=sa
  spring.datasource.password=123456
  spring.datasource.driverClassName=com.microsoft.sqlserver.jdbc.SQLServerDriver
  spring.jpa.hibernate.ddl-auto=update
  storage.location=uploads
  ```

### Lệnh khởi chạy:
```bash
# Biên dịch dự án
mvn clean compile

# Chạy ứng dụng Spring Boot
mvn spring-boot:run
```

---

## 4. Danh Sách Các Endpoint RESTful API

### 4.1. Category API (`/api/category`)
| Phương thức | Endpoint | Tham số | Chức năng |
| :---: | :--- | :--- | :--- |
| `GET` | `/api/category` | Không | Lấy toàn bộ danh sách Category |
| `GET` | `/api/category/{id}` | Path variable `id` | Lấy chi tiết Category theo ID |
| `POST` | `/api/category/getCategory` | Query param `id` | Lấy chi tiết Category theo ID (chuẩn slide) |
| `POST` | `/api/category/addCategory` | Form data: `categoryName`, `icon` (file) | Thêm mới Category và lưu file icon |
| `PUT` | `/api/category/updateCategory` | Form data: `categoryId`, `categoryName`, `icon` (file tùy chọn) | Cập nhật thông tin Category |
| `DELETE` | `/api/category/deleteCategory` | Query param: `categoryId` | Xóa Category theo ID |

### 4.2. Product API (`/api/product`)
| Phương thức | Endpoint | Tham số | Chức năng |
| :---: | :--- | :--- | :--- |
| `GET` | `/api/product` | Không | Lấy toàn bộ danh sách sản phẩm |
| `GET` | `/api/product/{id}` | Path variable `id` | Lấy chi tiết sản phẩm theo ID |
| `POST` | `/api/product/getProduct` | Query param `id` | Lấy chi tiết sản phẩm theo ID |
| `POST` | `/api/product/addProduct` | Form data: `productName`, `unitPrice`, `discount`, `quantity`, `description`, `categoryId`, `imageFile`, `status` | Thêm mới sản phẩm và upload ảnh |
| `PUT` | `/api/product/updateProduct` | Form data: `productId`, `productName`, `unitPrice`, `discount`, `quantity`, `description`, `categoryId`, `imageFile` (tùy chọn), `status` | Cập nhật sản phẩm và đổi ảnh |
| `DELETE` | `/api/product/deleteProduct` | Query param: `productId` | Xóa sản phẩm theo ID |

### 4.3. File / Image Stream API
- `GET /uploads/{filename}`: Phục vụ xem trực tiếp ảnh được upload.
- `GET /admin/categories/images/{filename}`: Phục vụ xem icon của Category.
- `GET /admin/products/images/{filename}`: Phục vụ xem ảnh của Product.

---

## 5. Hướng Dẫn Kiểm Thử (Testing Guide)

### 5.1. Kiểm thử Swagger 3 UI (OpenAPI 3)
Mở trình duyệt và truy cập vào đường dẫn:
```
http://localhost:8082/swagger-ui/index.html
```
Giao diện Swagger 3 hiển thị đầy đủ thông tin mô tả, nhóm endpoint `category-api-controller` và `product-api-controller`, cho phép gửi request và kiểm tra response JSON trực tiếp.

### 5.2. Kiểm thử Giao diện AJAX Category
Mở trình duyệt và truy cập:
```
http://localhost:8082/category-ajax
```
- Hiển thị danh sách danh mục và xem trước ảnh icon.
- Nút **"Thêm Category Ajax"**: Mở Modal Bootstrap, chọn ảnh và bấm Thêm -> Dữ liệu cập nhật ngay trên bảng mà không cần tải lại toàn trang.
- Nút **"Sửa"**: Mở Modal hiển thị thông tin cũ và ảnh preview, cho phép cập nhật tên và chọn ảnh mới.
- Nút **"Xóa"**: Xác nhận trước khi xóa và cập nhật lại giao diện bằng AJAX.

### 5.3. Kiểm thử Giao diện AJAX Product (Bài tập thêm)
Mở trình duyệt và truy cập:
```
http://localhost:8082/product-ajax
```
- Danh mục sản phẩm được load tự động từ API vào danh sách lựa chọn.
- Thêm mới, cập nhật giá tiền (được format VNĐ trực quan), số lượng, mô tả và tải lên ảnh sản phẩm bằng Modal Bootstrap và jQuery AJAX.
