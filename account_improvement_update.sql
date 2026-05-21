-- Bổ sung trạng thái trả hàng và mở rộng ghi chú hủy/trả hàng
ALTER TABLE orders
MODIFY Status ENUM(
    'PENDING',
    'PROCESSING',
    'CONFIRMED',
    'SHIPPED',
    'COMPLETED',
    'CANCELLED',
    'RETURN_REQUESTED',
    'RETURNED',
    'RETURN_REJECTED'
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL;

ALTER TABLE orders
MODIFY Note TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL;
