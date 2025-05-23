# Railway91 - Hướng dẫn Test API

## Chuẩn bị
1. Đảm bảo MySQL đang chạy
2. Database `railway91` đã được tạo
3. Ứng dụng Spring Boot đang chạy trên port 8080

## Các API để test

### 1. Đăng ký tài khoản
```http
POST http://localhost:8080/api/auth/register
Content-Type: application/json

{
    "username": "testuser",
    "password": "123456"
}
```

### 2. Đăng nhập
```http
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
    "username": "testuser",
    "password": "123456"
}
```
- Lưu token nhận được từ response để sử dụng cho các request tiếp theo

### 3. Tạo khóa học mới (yêu cầu quyền ADMIN)
```http
POST http://localhost:8080/courses
Authorization: Bearer {token}
Content-Type: application/json

{
    "name": "Khóa học Java Spring Boot",
    "numberOfSessions": 12,
    "totalHours": 36,
    "description": "Khóa học về Java Spring Boot cho người mới bắt đầu",
    "categoryId": 1
}
```

### 4. Lấy danh sách khóa học
```http
GET http://localhost:8080/courses?page=0&size=10
Authorization: Bearer {token}
```

### 5. Tạo bài học mới
```http
POST http://localhost:8080/courses/{courseId}/lessons
Authorization: Bearer {token}
Content-Type: application/json

{
    "title": "Giới thiệu Spring Boot",
    "durationHours": 3,
    "content": "Nội dung bài học về Spring Boot",
    "courseId": 1
}
```

### 6. Lấy danh sách bài học của khóa học
```http
GET http://localhost:8080/courses/{courseId}/lessons?page=0&size=10
Authorization: Bearer {token}
```

## Quy trình test
1. Đăng ký tài khoản mới (tài khoản đầu tiên sẽ có quyền ADMIN)
2. Đăng nhập để lấy token
3. Copy token từ response của API đăng nhập
4. Sử dụng token trong header "Authorization" cho các request tiếp theo
5. Test các chức năng theo thứ tự:
   - Tạo khóa học mới
   - Xem danh sách khóa học
   - Tạo bài học mới
   - Xem danh sách bài học

## Lưu ý
- Thay thế `{token}` bằng JWT token thực tế nhận được sau khi đăng nhập
- Thay thế `{courseId}` bằng ID của khóa học thực tế
- Nếu gặp lỗi 403 Forbidden, kiểm tra:
  - Token có hợp lệ không
  - Tài khoản có đủ quyền không (ADMIN/USER)
- Nếu gặp lỗi 404 Not Found, kiểm tra:
  - URL có đúng không
  - ID của resource có tồn tại không 