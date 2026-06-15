package dao;

import model.StoreSetting;

public class StoreSettingDao extends BaseDao {
    public StoreSetting getStoreSetting() {
        try {
            ensureTable();

            return getJdbi().withHandle(handle ->
                    handle.createQuery("""
                                    SELECT
                                        Store_Name AS storeName,
                                        Store_Email AS storeEmail,
                                        Store_Phone AS storePhone,
                                        Store_Website AS storeWebsite,
                                        Store_Address AS storeAddress
                                    FROM store_setting
                                    WHERE Setting_Id = 1
                                    """)
                            .mapToBean(StoreSetting.class)
                            .findOne()
                            .orElseGet(StoreSettingDao::defaultSetting)
            );
        } catch (RuntimeException e) {
            return defaultSetting();
        }
    }

    public boolean save(StoreSetting setting) {
        if (setting == null || setting.getStoreName() == null || setting.getStoreName().isBlank()) {
            return false;
        }

        try {
            ensureTable();

            return getJdbi().withHandle(handle ->
                    handle.createUpdate("""
                                    INSERT INTO store_setting (
                                        Setting_Id, Store_Name, Store_Email, Store_Phone,
                                        Store_Website, Store_Address
                                    )
                                    VALUES (
                                        1, :storeName, :storeEmail, :storePhone,
                                        :storeWebsite, :storeAddress
                                    )
                                    ON DUPLICATE KEY UPDATE
                                        Store_Name = VALUES(Store_Name),
                                        Store_Email = VALUES(Store_Email),
                                        Store_Phone = VALUES(Store_Phone),
                                        Store_Website = VALUES(Store_Website),
                                        Store_Address = VALUES(Store_Address),
                                        Updated_At = CURRENT_TIMESTAMP
                                    """)
                            .bindBean(setting)
                            .execute() > 0
            );
        } catch (RuntimeException e) {
            return false;
        }
    }

    private void ensureTable() {
        getJdbi().useHandle(handle -> {
            handle.execute("""
                    CREATE TABLE IF NOT EXISTS store_setting (
                        Setting_Id INT PRIMARY KEY,
                        Store_Name VARCHAR(150) NOT NULL,
                        Store_Email VARCHAR(190),
                        Store_Phone VARCHAR(30),
                        Store_Website VARCHAR(255),
                        Store_Address VARCHAR(500),
                        Updated_At TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                            ON UPDATE CURRENT_TIMESTAMP
                    )
                    """);

            StoreSetting defaults = defaultSetting();
            handle.createUpdate("""
                            INSERT IGNORE INTO store_setting (
                                Setting_Id, Store_Name, Store_Email, Store_Phone,
                                Store_Website, Store_Address
                            )
                            VALUES (
                                1, :storeName, :storeEmail, :storePhone,
                                :storeWebsite, :storeAddress
                            )
                            """)
                    .bindBean(defaults)
                    .execute();
        });
    }

    private static StoreSetting defaultSetting() {
        StoreSetting setting = new StoreSetting();
        setting.setStoreName("Handmade House");
        setting.setStoreEmail("handmadehouse23@handmade.vn");
        setting.setStorePhone("0944912685");
        setting.setStoreWebsite("https://handmadehouse.com");
        setting.setStoreAddress("Khu phố 6, Phường Linh Trung, TP. Thủ Đức, TP. Hồ Chí Minh");
        return setting;
    }
}
