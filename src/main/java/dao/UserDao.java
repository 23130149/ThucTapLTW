package dao;

import model.User;

import java.util.List;

public class UserDao extends BaseDao {

    public void register(User user) {
        String sql = """
            INSERT INTO user (User_Name, Email, Phone, Password, Create_At, Role)
            VALUES (:user_name, :email, :phone, :password, NOW(), :role)
        """;
        getJdbi().withHandle(handle ->
                handle.createUpdate(sql)
                        .bind("user_name", user.getUserName())
                        .bind("email", user.getEmail())
                        .bind("phone", user.getPhone())
                        .bind("password", user.getPassword())
                        .bind("role", user.getRole())
                        .execute()
        );
    }
    public boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM user WHERE Email = :email";

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("email", email)
                        .mapTo(Integer.class)
                        .one() > 0
        );
    }

    public User findByEmail(String email) {
        String sql = """
        SELECT
            User_Id     AS userId,
            User_Name   AS userName,
            Email       AS email,
            Phone       AS phone,
            Password    AS password,
            Google_Id   AS googleId,
            Create_At   AS createAt,
            Role        AS role
        FROM user
        WHERE Email = :email
    """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("email", email)
                        .mapToBean(User.class)
                        .findOne()
                        .orElse(null)
        );
    }

    public boolean updateProfile(User user) {
        String sql = """
            UPDATE user
            SET User_Name = :user_name,
                Phone = :phone
            WHERE User_Id = :user_id
        """;

        return getJdbi().withHandle(handle ->
                handle.createUpdate(sql)
                        .bind("user_name", user.getUserName())
                        .bind("phone", user.getPhone())
                        .bind("user_id", user.getUserId())
                        .execute()
        ) > 0;
    }

    public boolean updatePassword(int userId, String hashedPassword) {
        String sql = """
            UPDATE user
            SET Password = :password
            WHERE User_Id = :user_id
              AND Google_Id IS NULL
        """;

        return getJdbi().withHandle(handle ->
                handle.createUpdate(sql)
                        .bind("password", hashedPassword)
                        .bind("user_id", userId)
                        .execute()
        ) > 0;
    }

    public int countUsers() {
        String sql = "SELECT COUNT(*) FROM user";
        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .mapTo(Integer.class)
                        .one()
        );
    }

    public void insertGoogleUser(String email, String googleId) {
        String sql = """
            INSERT INTO user (Email, Google_Id, Role, Create_At)
            VALUES (:email, :google_id, 'USER', NOW())
        """;

        getJdbi().withHandle(handle ->
                handle.createUpdate(sql)
                        .bind("email", email)
                        .bind("google_id", googleId)
                        .execute()
        );
    }

    public User getAdmin() {
        String sql = """
            SELECT
                User_Id   AS userId,
                User_Name AS userName,
                Email     AS email,
                Phone     AS phone,
                Password  AS password,
                Create_At AS createAt,
                Role      AS role
            FROM user
            WHERE Role = :role
            LIMIT 1
        """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("role", "ADMIN")
                        .mapToBean(User.class)
                        .findOne()
                        .orElse(null)
        );
    }
    public List<User> getAllUsers() {
        String sql = """
        SELECT
            User_Id   AS userId,
            User_Name AS userName,
            Email     AS email,
            Phone     AS phone,
            Password  AS password,
            Google_Id AS googleId,
            Role      AS role
        FROM user
        WHERE Password IS NOT NULL
    """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .mapToBean(User.class)
                        .list()
        );
    }
    public List<User> getAllCustomers() {

        String sql = """
                    SELECT 
                        u.User_Id,
                        u.User_Name,
                        u.Phone,
                        u.Email,
                        u.Create_At,
                        COUNT(o.Order_Id) AS orderCount,
                        COALESCE(SUM(o.Total_Price),0) AS totalSpend
                    FROM user u
                    LEFT JOIN orders o 
                        ON u.User_Id = o.User_Id
                        AND o.Status = 'COMPLETED'
                    WHERE u.Role = 'USER'
                    GROUP BY u.User_Id
                    ORDER BY u.Create_At DESC
                """;

        return getJdbi().withHandle(h ->
                h.createQuery(sql)
                        .map((rs, ctx) -> {
                            User u = new User();
                            u.setUserId(rs.getInt("User_Id"));
                            u.setUserName(rs.getString("User_Name"));
                            u.setPhone(rs.getString("Phone"));
                            u.setEmail(rs.getString("Email"));
                            u.setCreateAt(rs.getTimestamp("Create_At").toLocalDateTime());
                            u.setOrderCount(rs.getInt("orderCount"));
                            u.setTotalSpend(rs.getBigDecimal("totalSpend"));
                            return u;
                        })
                        .list()
        );
    }
    public User getCustomerDetail(int userId) {

        String sql = """
            SELECT 
                u.User_Id,
                u.User_Name,
                u.Phone,
                u.Email,
                u.Create_At,
                COUNT(o.Order_Id) AS orderCount,
                COALESCE(SUM(o.Total_Price),0) AS totalSpend
            FROM user u
            LEFT JOIN orders o 
                ON u.User_Id = o.User_Id
                AND o.Status = 'COMPLETED'
            WHERE u.User_Id = :id
            GROUP BY u.User_Id
        """;

        return getJdbi().withHandle(h ->
                h.createQuery(sql)
                        .bind("id", userId)
                        .map((rs, ctx) -> {
                            User u = new User();
                            u.setUserId(rs.getInt("User_Id"));
                            u.setUserName(rs.getString("User_Name"));
                            u.setPhone(rs.getString("Phone"));
                            u.setEmail(rs.getString("Email"));
                            u.setCreateAt(rs.getTimestamp("Create_At").toLocalDateTime());
                            u.setOrderCount(rs.getInt("orderCount"));
                            u.setTotalSpend(rs.getBigDecimal("totalSpend"));
                            return u;
                        })
                        .findOne()
                        .orElse(null)
        );
    }
    public void updateCustomer(int userId, String userName, String phone) {
        String sql = """
        UPDATE user
        SET User_Name = :name,
            Phone = :phone
        WHERE User_Id = :id
          AND Role <> 'ADMIN'
    """;

        getJdbi().withHandle(h ->
                h.createUpdate(sql)
                        .bind("name", userName)
                        .bind("phone", phone)
                        .bind("id", userId)
                        .execute()
        );
    }
    public int countTotalCustomers() {
        String sql = "SELECT COUNT(*) FROM user WHERE Role = 'USER'";
        return getJdbi().withHandle(h ->
                h.createQuery(sql)
                        .mapTo(Integer.class)
                        .one()
        );
    }
    public int countVipCustomers() {
        String sql = """
        SELECT COUNT(*) FROM (
            SELECT u.User_Id
            FROM user u
            JOIN orders o ON u.User_Id = o.User_Id
            WHERE u.Role = 'USER'
              AND o.Status = 'COMPLETED'
            GROUP BY u.User_Id
            HAVING SUM(o.Total_Price) >= 5000000
        ) vip
    """;

        return getJdbi().withHandle(h ->
                h.createQuery(sql)
                        .mapTo(Integer.class)
                        .one()
        );
    }
    public int countNewCustomersThisMonth() {
        String sql = """
        SELECT COUNT(*) 
        FROM user
        WHERE Role = 'USER'
          AND MONTH(Create_At) = MONTH(CURRENT_DATE())
          AND YEAR(Create_At) = YEAR(CURRENT_DATE())
    """;

        return getJdbi().withHandle(h ->
                h.createQuery(sql)
                        .mapTo(Integer.class)
                        .one()
        );
    }
    public double getAverageSpendPerCustomer() {
        String sql = """
        SELECT 
            COALESCE(SUM(o.Total_Price) / COUNT(DISTINCT u.User_Id), 0)
        FROM user u
        LEFT JOIN orders o 
            ON u.User_Id = o.User_Id
            AND o.Status = 'COMPLETED'
        WHERE u.Role = 'USER'
    """;

        return getJdbi().withHandle(h ->
                h.createQuery(sql)
                        .mapTo(Double.class)
                        .one()
        );
    }




}