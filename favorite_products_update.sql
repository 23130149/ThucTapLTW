-- Bảng favorite_products đã có trong project123.sql.
-- Chạy thêm dòng này để tránh 1 user tim trùng 1 sản phẩm nhiều lần.
ALTER TABLE favorite_products
ADD UNIQUE KEY uq_favorite_user_product (User_Id, Product_Id);
