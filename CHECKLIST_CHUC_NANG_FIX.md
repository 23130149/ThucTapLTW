# Checklist chức năng đã kiểm/sửa trong bản này

## Đã sửa trực tiếp trong code
- Fix trùng mapping `/admin/dashboard`: `AdminController` đổi sang `/admin`, `/admin/dashboard` để `AdminDashboardController` xử lý.
- Fix `orderdetail.jsp` dư dấu `<` ở `<<div`.
- Fix Gradle compile thiếu `jakarta.servlet`: ưu tiên lấy `servlet-api.jar` từ Tomcat local, không đóng Servlet API vào WAR.
- Header search dùng chung `header.jsp`, thêm autocomplete 3-4 sản phẩm bán chạy liên quan keyword.
- Fix search suggestion bị lệch/chồng như 2 thanh tìm kiếm bằng CSS dropdown nổi dưới input.
- Tìm kiếm nâng cao `áo + len`: tách keyword theo dấu `+`, mỗi token phải khớp trong tên/mô tả/danh mục.
- Danh mục trang sản phẩm có số lượng sản phẩm theo DB.
- Lọc sản phẩm theo giá, tình trạng, danh mục, vật liệu, nhu cầu sử dụng, bán chạy nhất.
- Vật liệu/nhu cầu chỉ hiện option có sản phẩm khớp trong DB hiện tại.
- Phân trang giữ lại toàn bộ query lọc/search/sort và có dấu `...`, không bung 24 nút.
- Sản phẩm nổi bật trang chủ: user đã mua thì ưu tiên danh mục từng mua, user mới fallback bán chạy.
- Sản phẩm liên quan tăng từ 4 lên 12 và ưu tiên sản phẩm bán chạy trong cùng danh mục.
- Chặn thêm giỏ khi hết hàng hoặc vượt tồn kho.

## Chưa biến thành DB chuẩn 100%
- Vật liệu/nhu cầu hiện đang lọc theo text trong `Product_Name`, `Product_Description`, `Category.Name` vì DB chưa có cột riêng `Material` và `Usage_Purpose`.
- Muốn chuẩn nhất: thêm cột `Material`, `Usage_Purpose`, `Sold_Count` hoặc view thống kê bán chạy.
