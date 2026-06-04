ALTER TABLE orders
ADD COLUMN Ghn_Order_Code VARCHAR(50) NULL AFTER Paid_At,
ADD COLUMN Ghn_Status VARCHAR(50) NULL AFTER Ghn_Order_Code,
ADD COLUMN Ghn_Updated_At DATETIME NULL AFTER Ghn_Status,
ADD COLUMN Ghn_Leadtime DATETIME NULL AFTER Ghn_Updated_At,
ADD COLUMN Ghn_Finish_Date DATETIME NULL AFTER Ghn_Leadtime;

ALTER TABLE orders
ADD UNIQUE INDEX idx_orders_ghn_order_code (Ghn_Order_Code);

