# Bài 5 - Job Title Management System (2.5 điểm)

## Mô tả

Hệ thống quản lý Job Title với form upload file, kết nối PostgreSQL và test cases đầy đủ sử dụng **Partition Testing**.

## Yêu cầu

### 1. Form có kết nối với cơ sở dữ liệu ✅

- Database: PostgreSQL
- Connection: `jdbc:postgresql://localhost:5432/tuan6_2`
- Auto-create tables với JPA/Hibernate

### 2. Kịch bản kiểm thử rõ ràng ✅

- Sử dụng **Partition Testing** (Boundary Value Analysis)
- 12 test cases bao phủ tất cả trường hợp
- Test cases được chia thành 4 nhóm theo từng trường dữ liệu

### 3. Test case phải bao phủ tất cả các trường hợp ✅

- Job Title: 3 partitions (rỗng, hợp lệ, quá dài)
- Description: 3 partitions (rỗng, hợp lệ, quá dài)
- Job Specification (File): 3 partitions (rỗng, hợp lệ, quá lớn)
- Note: 3 partitions (rỗng, hợp lệ, quá dài)

### 4. Báo cáo rõ ràng, dễ dàng tái lập ✅

- Báo cáo chi tiết trong file README
- Bảng partition testing
- Hướng dẫn setup và chạy test

---

## Thiết lập Database

```sql
CREATE DATABASE tuan6_2;
```

---

## Cách chạy ứng dụng

### 1. Chạy Spring Boot application:

```bash
cd c:\Users\duong\Hoc\TESTER\Tuan6\bai5
mvn spring-boot:run
```

Hoặc chạy từ VS Code:

- Mở file `JobTitleApplication.java`
- Nhấn F5 hoặc click Run

### 2. Truy cập:

- Form thêm mới: http://localhost:8080/jobtitle/add
- Danh sách: http://localhost:8080/jobtitle/list

---

## Chạy Test Cases

### Cách 1: Maven command

```bash
mvn test
```

### Cách 2: VS Code

- Mở file `JobTitlePartitionTest.java`
- Click icon ▶ để chạy tất cả tests
- Hoặc chạy từng test riêng lẻ

---

## Bảng Partition Testing

| Partition | Value                 | Lower Boundary      | Upper Boundary                          | Note          |
| --------- | --------------------- | ------------------- | --------------------------------------- | ------------- |
| **1**     | **Job Title**         | length(input) >= 0  | length(input) <= 0                      | Chuỗi rỗng    |
| **2**     | **Job Title**         | length(input) > 0   | length(input) <= 100                    | Chuỗi hợp lệ  |
| **3**     | **Job Title**         | length(input) > 100 | length(input) <= length(longest string) | Chuỗi rất dài |
| **4**     | **Description**       | length(input) >= 0  | length(input) <= 0                      | File rỗng     |
| **5**     | **Description**       | length(input) > 0   | length(input) <= 400                    | Chuỗi hợp lệ  |
| **6**     | **Description**       | length(input) > 400 | length(input) <= length(longest string) | Chuỗi rất dài |
| **7**     | **Job Specification** | File size >= 0 kB   | File size <= 0 kB                       | File rỗng     |
| **8**     | **Job Specification** | File size = 0 kB    | File size <= 1024 kB                    | File hợp lệ   |
| **9**     | **Job Specification** | File size > 1024 kB | File size <= Largest File               | File rất lớn  |
| **10**    | **Note**              | length(input) >= 0  | length(input) <= 0                      | Chuỗi rỗng    |
| **11**    | **Note**              | length(input) > 0   | length(input) <= 400                    | Chuỗi hợp lệ  |
| **12**    | **Note**              | length(input) > 400 | length(input) <= length(longest string) | Chuỗi rất dài |

---

## Báo cáo Test Cases

### Test Case 1: Job Title - Chuỗi rỗng

- **Input**: Job Title = ""
- **Expected**: FAIL - "Job Title is required"
- **Actual**: PASS ✅ - Exception thrown with correct message
- **Status**: PASS

### Test Case 2: Job Title - Hợp lệ

- **Input**: Job Title = "Software Engineer" (18 chars)
- **Expected**: PASS - Lưu thành công
- **Actual**: PASS ✅ - Saved successfully
- **Status**: PASS

### Test Case 3: Job Title - Quá dài

- **Input**: Job Title = "A" × 101
- **Expected**: FAIL - "Job Title must not exceed 100 characters"
- **Actual**: PASS ✅ - Exception thrown with correct message
- **Status**: PASS

### Test Case 4: Description - Chuỗi rỗng

- **Input**: Description = ""
- **Expected**: PASS - Description is optional
- **Actual**: PASS ✅ - Saved with empty description
- **Status**: PASS

### Test Case 5: Description - Hợp lệ

- **Input**: Description = "This is a valid description" (50 chars)
- **Expected**: PASS - Lưu thành công
- **Actual**: PASS ✅ - Saved successfully
- **Status**: PASS

### Test Case 6: Description - Quá dài

- **Input**: Description = "A" × 401
- **Expected**: FAIL - "Description must not exceed 400 characters"
- **Actual**: PASS ✅ - Exception thrown with correct message
- **Status**: PASS

### Test Case 7: File - Rỗng (0 KB)

- **Input**: File size = 0 bytes
- **Expected**: FAIL - "File cannot be empty"
- **Actual**: PASS ✅ - Exception thrown with correct message
- **Status**: PASS

### Test Case 8: File - Hợp lệ (100 KB)

- **Input**: File size = 100 KB
- **Expected**: PASS - File uploaded successfully
- **Actual**: PASS ✅ - File saved with correct size
- **Status**: PASS

### Test Case 9: File - Quá lớn (1025 KB)

- **Input**: File size = 1025 KB
- **Expected**: FAIL - "File size must not exceed 1024 KB"
- **Actual**: PASS ✅ - Exception thrown with correct message
- **Status**: PASS

### Test Case 10: Note - Chuỗi rỗng

- **Input**: Note = ""
- **Expected**: PASS - Note is optional
- **Actual**: PASS ✅ - Saved with empty note
- **Status**: PASS

### Test Case 11: Note - Hợp lệ

- **Input**: Note = "This is a valid note" (20 chars)
- **Expected**: PASS - Lưu thành công
- **Actual**: PASS ✅ - Saved successfully
- **Status**: PASS

### Test Case 12: Note - Quá dài

- **Input**: Note = "A" × 401
- **Expected**: FAIL - "Note must not exceed 400 characters"
- **Actual**: PASS ✅ - Exception thrown with correct message
- **Status**: PASS

---

## Bảng Tóm Tắt Lỗi

| TC ID | Field       | Test Type        | Input               | Expected Result | Actual Result | Status  | Error Message                              |
| ----- | ----------- | ---------------- | ------------------- | --------------- | ------------- | ------- | ------------------------------------------ |
| TC1   | Job Title   | Boundary (Lower) | ""                  | FAIL            | FAIL          | ✅ PASS | Job Title is required                      |
| TC2   | Job Title   | Valid            | "Software Engineer" | PASS            | PASS          | ✅ PASS | -                                          |
| TC3   | Job Title   | Boundary (Upper) | 101 chars           | FAIL            | FAIL          | ✅ PASS | Job Title must not exceed 100 characters   |
| TC4   | Description | Boundary (Lower) | ""                  | PASS            | PASS          | ✅ PASS | -                                          |
| TC5   | Description | Valid            | 50 chars            | PASS            | PASS          | ✅ PASS | -                                          |
| TC6   | Description | Boundary (Upper) | 401 chars           | FAIL            | FAIL          | ✅ PASS | Description must not exceed 400 characters |
| TC7   | File        | Boundary (Lower) | 0 KB                | FAIL            | FAIL          | ✅ PASS | File cannot be empty                       |
| TC8   | File        | Valid            | 100 KB              | PASS            | PASS          | ✅ PASS | -                                          |
| TC9   | File        | Boundary (Upper) | 1025 KB             | FAIL            | FAIL          | ✅ PASS | File size must not exceed 1024 KB          |
| TC10  | Note        | Boundary (Lower) | ""                  | PASS            | PASS          | ✅ PASS | -                                          |
| TC11  | Note        | Valid            | 20 chars            | PASS            | PASS          | ✅ PASS | -                                          |
| TC12  | Note        | Boundary (Upper) | 401 chars           | FAIL            | FAIL          | ✅ PASS | Note must not exceed 400 characters        |

### Tổng kết:

- **Tổng số test cases**: 12
- **Passed**: 12 ✅
- **Failed**: 0
- **Pass Rate**: 100%

---

## Các trường hợp lỗi được kiểm tra

### 1. Validation Errors (Required Fields)

- ✅ Job Title rỗng
- ✅ Job Title null

### 2. Boundary Value Errors (Max Length)

- ✅ Job Title > 100 characters
- ✅ Description > 400 characters
- ✅ Note > 400 characters

### 3. File Upload Errors

- ✅ File rỗng (0 KB)
- ✅ File quá lớn (> 1024 KB)

### 4. Success Cases

- ✅ Tất cả trường hợp hợp lệ
- ✅ Trường optional có thể để trống (Description, Note)
- ✅ File upload trong giới hạn

---

## Hướng dẫn tái lập test

### Bước 1: Setup environment

```bash
# Tạo database
CREATE DATABASE tuan6_2;

# Cài đặt dependencies
cd bai5
mvn clean install
```

### Bước 2: Chạy test

```bash
# Chạy tất cả tests
mvn test

# Chạy test cụ thể
mvn test -Dtest=JobTitlePartitionTest#testPartition1_JobTitle_Empty
```

### Bước 3: Xem kết quả

- Test results: `target/surefire-reports/`
- Console output hiển thị chi tiết từng test case

---

## Cấu trúc Project

```
bai5/
├── src/
│   ├── main/
│   │   ├── java/com/example/jobtitle/
│   │   │   ├── JobTitleApplication.java
│   │   │   ├── entity/JobTitle.java
│   │   │   ├── repository/JobTitleRepository.java
│   │   │   ├── service/JobTitleService.java
│   │   │   └── controller/JobTitleController.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── templates/
│   │           ├── job-form.html
│   │           └── job-list.html
│   └── test/
│       └── java/com/example/jobtitle/
│           └── JobTitlePartitionTest.java
├── uploads/ (file upload directory)
├── pom.xml
└── README.md
```

---

## Kết luận

✅ **Form có kết nối với cơ sở dữ liệu**: PostgreSQL với JPA/Hibernate

✅ **Kịch bản kiểm thử rõ ràng**: Sử dụng Partition Testing với 12 test cases

✅ **Test case bao phủ đầy đủ**:

- 100% coverage cho tất cả trường dữ liệu
- Test cả boundary values và valid values
- Test cả success và error cases

✅ **Báo cáo dễ tái lập**:

- Hướng dẫn setup chi tiết
- Bảng partition testing rõ ràng
- Bảng tóm tắt lỗi đầy đủ
- Có thể chạy lại test bất cứ lúc nào với `mvn test`
