# Bài 6 - User Management System (2.5 điểm)

## Mô tả

Hệ thống quản lý người dùng (User Management) với form đầy đủ CRUD, kết nối PostgreSQL và test cases sử dụng **Partition Testing**.

## Yêu cầu

### 1. Form có kết nối với cơ sở dữ liệu ✅

- Database: PostgreSQL
- Connection: `jdbc:postgresql://localhost:5432/tuan6_3`
- Auto-create tables với JPA/Hibernate

### 2. Kịch bản kiểm thử rõ ràng ✅

- Sử dụng **Partition Testing** (Boundary Value Analysis)
- 14 test cases bao phủ tất cả trường hợp
- Test cases được chia theo từng trường dữ liệu

### 3. Test case phải bao phủ tất cả các trường hợp ✅

- Username: 3 partitions (rỗng, hợp lệ, quá dài)
- Full Name: 3 partitions (rỗng, hợp lệ, quá dài)
- Email: 4 partitions (rỗng, hợp lệ, invalid format, quá dài)
- Password: 4 partitions (rỗng, quá ngắn, hợp lệ, quá dài)

### 4. Báo cáo rõ ràng, dễ dàng tái lập ✅

- Báo cáo chi tiết trong file README
- Bảng partition testing
- Hướng dẫn setup và chạy test

---

## Thiết lập Database

```sql
CREATE DATABASE tuan6_3;
```

---

## Cách chạy ứng dụng

### 1. Chạy Spring Boot application:

```bash
cd c:\Users\duong\Hoc\TESTER\Tuan6\bai6
mvn spring-boot:run
```

Hoặc chạy từ VS Code:

- Mở file `UserManagementApplication.java`
- Nhấn F5 hoặc click Run

### 2. Truy cập:

- Form quản lý user: **http://localhost:8080/users/add**
- Danh sách users: **http://localhost:8080/users/list**

---

## Chạy Test Cases

### Cách 1: Maven command

```bash
mvn test
```

### Cách 2: VS Code

- Mở file `UserManagementPartitionTest.java`
- Click icon ▶ để chạy tất cả tests
- Hoặc chạy từng test riêng lẻ

---

## Bảng Partition Testing

| Partition | Field         | Lower Boundary | Upper Boundary | Note               |
| --------- | ------------- | -------------- | -------------- | ------------------ |
| **1**     | **Username**  | length >= 0    | length <= 0    | Chuỗi rỗng         |
| **2**     | **Username**  | length > 0     | length <= 50   | Chuỗi hợp lệ       |
| **3**     | **Username**  | length > 50    | length <= max  | Chuỗi quá dài      |
| **4**     | **Full Name** | length >= 0    | length <= 0    | Chuỗi rỗng         |
| **5**     | **Full Name** | length > 0     | length <= 100  | Chuỗi hợp lệ       |
| **6**     | **Full Name** | length > 100   | length <= max  | Chuỗi quá dài      |
| **7**     | **Email**     | length >= 0    | length <= 0    | Chuỗi rỗng         |
| **8**     | **Email**     | Valid format   | length <= 100  | Email hợp lệ       |
| **9**     | **Email**     | Invalid format | -              | Email không hợp lệ |
| **10**    | **Email**     | length > 100   | length <= max  | Email quá dài      |
| **11**    | **Password**  | length >= 0    | length <= 0    | Chuỗi rỗng         |
| **12**    | **Password**  | length > 0     | length < 6     | Password quá ngắn  |
| **13**    | **Password**  | length >= 6    | length <= 100  | Password hợp lệ    |
| **14**    | **Password**  | length > 100   | length <= max  | Password quá dài   |

---

## Báo cáo Test Cases

### Test Case 1: Username - Chuỗi rỗng

- **Input**: Username = ""
- **Expected**: FAIL - "Username is required"
- **Actual**: PASS ✅ - Exception thrown
- **Status**: PASS

### Test Case 2: Username - Hợp lệ

- **Input**: Username = "john_doe" (8 chars)
- **Expected**: PASS - Lưu thành công
- **Actual**: PASS ✅ - Saved successfully
- **Status**: PASS

### Test Case 3: Username - Quá dài

- **Input**: Username = 51 characters
- **Expected**: FAIL - "Username must not exceed 50 characters"
- **Actual**: PASS ✅ - Exception thrown
- **Status**: PASS

### Test Case 4: Full Name - Chuỗi rỗng

- **Input**: Full Name = ""
- **Expected**: FAIL - "Full name is required"
- **Actual**: PASS ✅ - Exception thrown
- **Status**: PASS

### Test Case 5: Full Name - Hợp lệ

- **Input**: Full Name = "Jane Smith" (10 chars)
- **Expected**: PASS - Lưu thành công
- **Actual**: PASS ✅ - Saved successfully
- **Status**: PASS

### Test Case 6: Full Name - Quá dài

- **Input**: Full Name = 101 characters
- **Expected**: FAIL - "Full name must not exceed 100 characters"
- **Actual**: PASS ✅ - Exception thrown
- **Status**: PASS

### Test Case 7: Email - Chuỗi rỗng

- **Input**: Email = ""
- **Expected**: FAIL - "Email is required"
- **Actual**: PASS ✅ - Exception thrown
- **Status**: PASS

### Test Case 8: Email - Format hợp lệ

- **Input**: Email = "valid@example.com"
- **Expected**: PASS - Lưu thành công
- **Actual**: PASS ✅ - Saved successfully
- **Status**: PASS

### Test Case 9: Email - Format không hợp lệ

- **Input**: Email = "invalid-email"
- **Expected**: FAIL - "Email format is invalid"
- **Actual**: PASS ✅ - Exception thrown
- **Status**: PASS

### Test Case 10: Email - Quá dài

- **Input**: Email = 101+ characters
- **Expected**: FAIL - "Email must not exceed 100 characters"
- **Actual**: PASS ✅ - Exception thrown
- **Status**: PASS

### Test Case 11: Password - Chuỗi rỗng

- **Input**: Password = ""
- **Expected**: FAIL - "Password is required"
- **Actual**: PASS ✅ - Exception thrown
- **Status**: PASS

### Test Case 12: Password - Quá ngắn

- **Input**: Password = "123" (3 chars)
- **Expected**: FAIL - "Password must be at least 6 characters"
- **Actual**: PASS ✅ - Exception thrown
- **Status**: PASS

### Test Case 13: Password - Hợp lệ

- **Input**: Password = "validpass123" (12 chars)
- **Expected**: PASS - Lưu thành công
- **Actual**: PASS ✅ - Saved successfully
- **Status**: PASS

### Test Case 14: Password - Quá dài

- **Input**: Password = 101 characters
- **Expected**: FAIL - "Password must not exceed 100 characters"
- **Actual**: PASS ✅ - Exception thrown
- **Status**: PASS

---

## Bảng Tóm Tắt Lỗi

| TC ID | Field     | Test Type        | Input               | Expected Result | Actual Result | Status  | Error Message                            |
| ----- | --------- | ---------------- | ------------------- | --------------- | ------------- | ------- | ---------------------------------------- |
| TC1   | Username  | Boundary (Lower) | ""                  | FAIL            | FAIL          | ✅ PASS | Username is required                     |
| TC2   | Username  | Valid            | "john_doe"          | PASS            | PASS          | ✅ PASS | -                                        |
| TC3   | Username  | Boundary (Upper) | 51 chars            | FAIL            | FAIL          | ✅ PASS | Username must not exceed 50 characters   |
| TC4   | Full Name | Boundary (Lower) | ""                  | FAIL            | FAIL          | ✅ PASS | Full name is required                    |
| TC5   | Full Name | Valid            | "Jane Smith"        | PASS            | PASS          | ✅ PASS | -                                        |
| TC6   | Full Name | Boundary (Upper) | 101 chars           | FAIL            | FAIL          | ✅ PASS | Full name must not exceed 100 characters |
| TC7   | Email     | Boundary (Lower) | ""                  | FAIL            | FAIL          | ✅ PASS | Email is required                        |
| TC8   | Email     | Valid            | "valid@example.com" | PASS            | PASS          | ✅ PASS | -                                        |
| TC9   | Email     | Invalid Format   | "invalid-email"     | FAIL            | FAIL          | ✅ PASS | Email format is invalid                  |
| TC10  | Email     | Boundary (Upper) | 101+ chars          | FAIL            | FAIL          | ✅ PASS | Email must not exceed 100 characters     |
| TC11  | Password  | Boundary (Lower) | ""                  | FAIL            | FAIL          | ✅ PASS | Password is required                     |
| TC12  | Password  | Too Short        | "123"               | FAIL            | FAIL          | ✅ PASS | Password must be at least 6 characters   |
| TC13  | Password  | Valid            | "validpass123"      | PASS            | PASS          | ✅ PASS | -                                        |
| TC14  | Password  | Boundary (Upper) | 101 chars           | FAIL            | FAIL          | ✅ PASS | Password must not exceed 100 characters  |

### Tổng kết:

- **Tổng số test cases**: 14
- **Passed**: 14 ✅
- **Failed**: 0
- **Pass Rate**: 100%

---

## Các trường hợp lỗi được kiểm tra

### 1. Validation Errors (Required Fields)

- ✅ Username rỗng
- ✅ Full Name rỗng
- ✅ Email rỗng
- ✅ Password rỗng

### 2. Boundary Value Errors (Length Constraints)

- ✅ Username > 50 characters
- ✅ Full Name > 100 characters
- ✅ Email > 100 characters
- ✅ Password < 6 characters
- ✅ Password > 100 characters

### 3. Format Validation Errors

- ✅ Email format không hợp lệ

### 4. Success Cases

- ✅ Tất cả trường hợp hợp lệ
- ✅ Username hợp lệ (1-50 chars)
- ✅ Full Name hợp lệ (1-100 chars)
- ✅ Email hợp lệ (format correct, ≤100 chars)
- ✅ Password hợp lệ (6-100 chars)

---

## Hướng dẫn tái lập test

### Bước 1: Setup environment

```bash
# Tạo database
CREATE DATABASE tuan6_3;

# Cài đặt dependencies
cd bai6
mvn clean install
```

### Bước 2: Chạy test

```bash
# Chạy tất cả tests
mvn test

# Chạy test cụ thể
mvn test -Dtest=UserManagementPartitionTest#testPartition1_Username_Empty
```

### Bước 3: Xem kết quả

- Test results: `target/surefire-reports/`
- Console output hiển thị chi tiết từng test case

---

## Cấu trúc Project

```
bai6/
├── src/
│   ├── main/
│   │   ├── java/com/example/usermgmt/
│   │   │   ├── UserManagementApplication.java
│   │   │   ├── entity/User.java
│   │   │   ├── repository/UserRepository.java
│   │   │   ├── service/UserService.java
│   │   │   └── controller/UserController.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── templates/
│   │           ├── user-form.html
│   │           └── user-list.html
│   └── test/
│       └── java/com/example/usermgmt/
│           └── UserManagementPartitionTest.java
├── pom.xml
└── README.md
```

---

## Kết luận

✅ **Form có kết nối với cơ sở dữ liệu**: PostgreSQL với JPA/Hibernate

✅ **Kịch bản kiểm thử rõ ràng**: Sử dụng Partition Testing với 14 test cases

✅ **Test case bao phủ đầy đủ**:

- 100% coverage cho 4 trường dữ liệu (Username, Full Name, Email, Password)
- Test cả boundary values, valid values, và format validation
- Test cả success và error cases

✅ **Báo cáo dễ tái lập**:

- Hướng dẫn setup chi tiết
- Bảng partition testing rõ ràng
- Bảng tóm tắt lỗi đầy đủ
- Có thể chạy lại test bất cứ lúc nào với `mvn test`

## Giao diện Form

- Thiết kế dark theme hiện đại
- Sidebar navigation
- 4 buttons: Create, Update, Delete, Reset
- Validation errors hiển thị rõ ràng
- Responsive design
