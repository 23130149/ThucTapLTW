package dao;

import model.UserAddress;

import java.util.List;

public class UserAddressDao extends BaseDao {


    public List<UserAddress> findByUserId(int userId) {

        String sql = """
        SELECT
            User_Address_Id AS userAddressId,
            User_Id AS userId,
            Country AS country,
            Province AS province,
            District AS district,
            Street AS street,
            Province_Id AS provinceId,
            District_Id AS districtId,
            Ward_Code AS wardCode
        FROM user_address
        WHERE User_Id = :user_id
    """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("user_id", userId)
                        .mapToBean(UserAddress.class)
                        .list()
        );
    }


    public void insert(UserAddress address) {

        String sql = """
            INSERT INTO user_address
            (User_Id, Country, Province, District, Street, Province_Id, District_Id, Ward_Code)
            VALUES (:user_id, :country, :province, :district, :street, :province_id, :district_id, :ward_code)
        """;

        getJdbi().withHandle(handle ->
                handle.createUpdate(sql)
                        .bind("user_id", address.getUserId())
                        .bind("country", address.getCountry())
                        .bind("province", address.getProvince())
                        .bind("district", address.getDistrict())
                        .bind("street", address.getStreet())
                        .bind("province_id", address.getProvinceId())
                        .bind("district_id", address.getDistrictId())
                        .bind("ward_code", address.getWardCode())
                        .execute()
        );
    }

    public void update(UserAddress address) {
        String sql = """
        UPDATE user_address
        SET
            Country = :country,
            Province = :province,
            District = :district,
            Street = :street,
            Province_Id = :province_id,
            District_Id = :district_id,
            Ward_Code = :ward_code
        WHERE User_Address_Id = :address_id
          AND User_Id = :user_id
    """;

        getJdbi().withHandle(handle ->
                handle.createUpdate(sql)
                        .bind("country", address.getCountry())
                        .bind("province", address.getProvince())
                        .bind("district", address.getDistrict())
                        .bind("street", address.getStreet())
                        .bind("province_id", address.getProvinceId())
                        .bind("district_id", address.getDistrictId())
                        .bind("ward_code", address.getWardCode())
                        .bind("address_id", address.getUserAddressId())
                        .bind("user_id", address.getUserId())
                        .execute()
        );
    }
    public UserAddress findById(int id) {
        String sql = """
        SELECT
            User_Address_Id AS userAddressId,
            User_Id AS userId,
            Country AS country,
            Province AS province,
            District AS district,
            Street AS street,
            Province_Id AS provinceId,
            District_Id AS districtId,
            Ward_Code AS wardCode
        FROM user_address
        WHERE User_Address_Id = :id
    """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("id", id)
                        .mapToBean(UserAddress.class)
                        .findOne()
                        .orElse(null)
        );
    }

    public UserAddress findByIdAndUserId(int id, int userId) {
        String sql = """
        SELECT
            User_Address_Id AS userAddressId,
            User_Id AS userId,
            Country AS country,
            Province AS province,
            District AS district,
            Street AS street,
            Province_Id AS provinceId,
            District_Id AS districtId,
            Ward_Code AS wardCode
        FROM user_address
        WHERE User_Address_Id = :id
          AND User_Id = :userId
        """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("id", id)
                        .bind("userId", userId)
                        .mapToBean(UserAddress.class)
                        .findOne()
                        .orElse(null)
        );
    }

    public void updateGhnCodes(int addressId, int userId, Integer provinceId, Integer districtId, String wardCode) {
        String sql = """
        UPDATE user_address
        SET
            Province_Id = :province_id,
            District_Id = :district_id,
            Ward_Code = :ward_code
        WHERE User_Address_Id = :address_id
          AND User_Id = :user_id
    """;

        getJdbi().withHandle(handle ->
                handle.createUpdate(sql)
                        .bind("province_id", provinceId)
                        .bind("district_id", districtId)
                        .bind("ward_code", wardCode)
                        .bind("address_id", addressId)
                        .bind("user_id", userId)
                        .execute()
        );
    }

    public void deleteById(int id) {
        String sql = "DELETE FROM user_address WHERE User_Address_Id = :id";

        getJdbi().withHandle(handle ->
                handle.createUpdate(sql)
                        .bind("id", id)
                        .execute()
        );
    }

    public boolean deleteByIdAndUserId(int id, int userId) {
        String sql = """
                DELETE FROM user_address
                WHERE User_Address_Id = :id
                  AND User_Id = :userId
                """;

        return getJdbi().withHandle(handle ->
                handle.createUpdate(sql)
                        .bind("id", id)
                        .bind("userId", userId)
                        .execute() > 0
        );
    }
}
