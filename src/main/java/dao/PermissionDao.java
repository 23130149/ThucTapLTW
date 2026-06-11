package dao;

import model.Permission;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PermissionDao extends BaseDao {

    public List<Permission> getAllPermissions() {
        String sql = """
            SELECT
                Permission_Id AS permissionId,
                Permission_Code AS permissionCode,
                Permission_Name AS permissionName,
                Description AS description
            FROM permissions
            ORDER BY Sort_Order ASC, Permission_Id ASC
        """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .mapToBean(Permission.class)
                        .list()
        );
    }

    public Set<String> getPermissionCodesByUserId(int userId) {
        String sql = """
            SELECT p.Permission_Code
            FROM user_permissions up
            JOIN permissions p ON up.Permission_Id = p.Permission_Id
            WHERE up.User_Id = :userId
        """;

        return new HashSet<>(getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("userId", userId)
                        .mapTo(String.class)
                        .list()
        ));
    }

    public boolean hasPermission(int userId, String permissionCode) {
        String sql = """
            SELECT COUNT(*)
            FROM user_permissions up
            JOIN permissions p ON up.Permission_Id = p.Permission_Id
            WHERE up.User_Id = :userId
              AND p.Permission_Code = :permissionCode
        """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("userId", userId)
                        .bind("permissionCode", permissionCode)
                        .mapTo(Integer.class)
                        .one() > 0
        );
    }

    public void updateUserPermissions(int userId, List<String> permissionCodes) {
        String deleteSql = "DELETE FROM user_permissions WHERE User_Id = :userId";

        String insertSql = """
            INSERT INTO user_permissions (User_Id, Permission_Id)
            SELECT :userId, Permission_Id
            FROM permissions
            WHERE Permission_Code = :permissionCode
        """;

        getJdbi().useTransaction(handle -> {
            handle.createUpdate(deleteSql)
                    .bind("userId", userId)
                    .execute();

            for (String permissionCode : permissionCodes) {
                handle.createUpdate(insertSql)
                        .bind("userId", userId)
                        .bind("permissionCode", permissionCode)
                        .execute();
            }
        });
    }
}