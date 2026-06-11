package dao;

import model.User;

import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao extends BaseDao {

    public void register(User user) {
        String sql = """
            INSERT INTO user (
                Customer_Code,
                User_Name,
                Email,
                Phone,
                Password,
                Create_At,
                Role
            )
            VALUES (
                :customerCode,
                :user_name,
                :email,
                :phone,
                :password,
                NOW(),
                :role
            )
        """;

        getJdbi().useTransaction(handle -> {
            int nextId = handle.createQuery("SELECT COALESCE(MAX(User_Id), 0) + 1 FROM user")
                    .mapTo(Integer.class)
                    .one();

            String customerCode = "HH" + String.format("%06d", nextId);

            handle.createUpdate(sql)
                    .bind("customerCode", customerCode)
                    .bind("user_name", user.getUserName())
                    .bind("email", user.getEmail())
                    .bind("phone", user.getPhone())
                    .bind("password", user.getPassword())
                    .bind("role", user.getRole())
                    .execute();
        });
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
                User_Id       AS userId,
                Customer_Code AS customerCode,
                User_Name     AS userName,
                Email         AS email,
                Phone         AS phone,
                Date_Of_Birth AS dateOfBirth,
                Gender        AS gender,
                Password      AS password,
                Google_Id     AS googleId,
                Avatar_Url    AS avatarUrl,
                Bio           AS bio,
                Create_At     AS createAt,
                Role          AS role
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

    public boolean emailExistsExceptUser(String email, int userId) {
        if (email == null || email.isBlank()) {
            return false;
        }

        String sql = """
            SELECT COUNT(*)
            FROM user
            WHERE Email = :email
              AND User_Id <> :userId
        """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("email", email.trim())
                        .bind("userId", userId)
                        .mapTo(Integer.class)
                        .one() > 0
        );
    }

    public void updateProfile(User user) {
        String sql = """
            UPDATE user
            SET
                User_Name = :userName,
                Email = :email,
                Phone = :phone,
                Date_Of_Birth = :dateOfBirth,
                Gender = :gender,
                Avatar_Url = :avatarUrl,
                Bio = :bio
            WHERE User_Id = :userId
        """;

        getJdbi().withHandle(handle ->
                handle.createUpdate(sql)
                        .bindBean(user)
                        .execute()
        );
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
            INSERT INTO user (
                Customer_Code,
                Email,
                Google_Id,
                Role,
                Create_At
            )
            VALUES (
                :customerCode,
                :email,
                :google_id,
                'USER',
                NOW()
            )
        """;

        getJdbi().useTransaction(handle -> {
            int nextId = handle.createQuery("SELECT COALESCE(MAX(User_Id), 0) + 1 FROM user")
                    .mapTo(Integer.class)
                    .one();

            String customerCode = "HH" + String.format("%06d", nextId);

            handle.createUpdate(sql)
                    .bind("customerCode", customerCode)
                    .bind("email", email)
                    .bind("google_id", googleId)
                    .execute();
        });
    }

    public User getAdmin() {
        String sql = """
            SELECT
                User_Id       AS userId,
                Customer_Code AS customerCode,
                User_Name     AS userName,
                Email         AS email,
                Phone         AS phone,
                Date_Of_Birth AS dateOfBirth,
                Gender        AS gender,
                Password      AS password,
                Google_Id     AS googleId,
                Avatar_Url    AS avatarUrl,
                Bio           AS bio,
                Create_At     AS createAt,
                Role          AS role
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
                User_Id       AS userId,
                Customer_Code AS customerCode,
                User_Name     AS userName,
                Email         AS email,
                Phone         AS phone,
                Date_Of_Birth AS dateOfBirth,
                Gender        AS gender,
                Password      AS password,
                Google_Id     AS googleId,
                Avatar_Url    AS avatarUrl,
                Bio           AS bio,
                Create_At     AS createAt,
                Role          AS role
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
            u.User_Id       AS userId,
            u.Customer_Code AS customerCode,
            u.User_Name     AS userName,
            u.Phone         AS phone,
            u.Email         AS email,
            u.Date_Of_Birth AS dateOfBirth,
            u.Gender        AS gender,
            u.Avatar_Url    AS avatarUrl,
            u.Bio           AS bio,
            u.Create_At     AS createAt,
            COUNT(o.Order_Id) AS orderCount,
            COALESCE(SUM(o.Total_Price), 0) AS totalSpend
        FROM user u
        LEFT JOIN orders o
            ON u.User_Id = o.User_Id
            AND o.Status = 'COMPLETED'
        WHERE u.Role = 'USER'
        GROUP BY
            u.User_Id,
            u.Customer_Code,
            u.User_Name,
            u.Phone,
            u.Email,
            u.Date_Of_Birth,
            u.Gender,
            u.Avatar_Url,
            u.Bio,
            u.Create_At
        ORDER BY u.Create_At DESC
    """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .mapToBean(User.class)
                        .list()
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
        SELECT COUNT(*)
        FROM (
            SELECT
                u.User_Id,
                COALESCE(SUM(o.Total_Price), 0) AS totalSpend
            FROM user u
            LEFT JOIN orders o
                ON u.User_Id = o.User_Id
                AND o.Status = 'COMPLETED'
            WHERE u.Role = 'USER'
            GROUP BY u.User_Id
        ) c
        WHERE c.totalSpend >= 10000000
    """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
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

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .mapTo(Integer.class)
                        .one()
        );
    }


    public double getAverageSpendPerCustomer() {
        String sql = """
        SELECT COALESCE(
            SUM(CASE WHEN o.Status = 'COMPLETED' THEN o.Total_Price ELSE 0 END)
            / NULLIF(COUNT(DISTINCT u.User_Id), 0),
            0
        )
        FROM user u
        LEFT JOIN orders o ON u.User_Id = o.User_Id
        WHERE u.Role = 'USER'
    """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .mapTo(Double.class)
                        .one()
        );
    }
    public List<User> searchCustomers(String keyword) {
        String sql = """
        SELECT
            u.User_Id,
            u.Customer_Code,
            u.User_Name,
            u.Phone,
            u.Email,
            u.Date_Of_Birth,
            u.Gender,
            u.Avatar_Url,
            u.Bio,
            u.Create_At,
            COUNT(o.Order_Id) AS orderCount,
            COALESCE(SUM(o.Total_Price), 0) AS totalSpend
        FROM user u
        LEFT JOIN orders o
            ON u.User_Id = o.User_Id
            AND o.Status = 'COMPLETED'
        WHERE u.Role = 'USER'
          AND (
                :keyword = ''
                OR LOWER(COALESCE(u.User_Name, '')) LIKE CONCAT('%', LOWER(:keyword), '%')
                OR COALESCE(u.Phone, '') LIKE CONCAT('%', :keyword, '%')
                OR LOWER(COALESCE(u.Email, '')) LIKE CONCAT('%', LOWER(:keyword), '%')
                OR LOWER(COALESCE(u.Customer_Code, '')) LIKE CONCAT('%', LOWER(:keyword), '%')
          )
        GROUP BY
            u.User_Id,
            u.Customer_Code,
            u.User_Name,
            u.Phone,
            u.Email,
            u.Date_Of_Birth,
            u.Gender,
            u.Avatar_Url,
            u.Bio,
            u.Create_At
        ORDER BY u.Create_At DESC
    """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("keyword", keyword == null ? "" : keyword.trim())
                        .map((rs, ctx) -> mapCustomer(rs))
                        .list()
        );
    }

    private User mapCustomer(ResultSet rs) throws SQLException {
        User user = new User();

        user.setUserId(rs.getInt("User_Id"));
        user.setCustomerCode(rs.getString("Customer_Code"));
        user.setUserName(rs.getString("User_Name"));
        user.setPhone(rs.getString("Phone"));
        user.setEmail(rs.getString("Email"));

        if (rs.getDate("Date_Of_Birth") != null) {
            user.setDateOfBirth(rs.getDate("Date_Of_Birth").toLocalDate());
        }

        user.setGender(rs.getString("Gender"));
        user.setAvatarUrl(rs.getString("Avatar_Url"));
        user.setBio(rs.getString("Bio"));

        if (rs.getTimestamp("Create_At") != null) {
            user.setCreateAt(rs.getTimestamp("Create_At").toLocalDateTime());
        }

        user.setOrderCount(rs.getInt("orderCount"));
        user.setTotalSpend(rs.getBigDecimal("totalSpend"));

        return user;
    }
    public List<User> filterCustomers(String keyword, String customerType, int minOrders, int maxOrders) {
        String sql = """
        SELECT *
        FROM (
            SELECT
                u.User_Id,
                u.Customer_Code,
                u.User_Name,
                u.Phone,
                u.Email,
                u.Date_Of_Birth,
                u.Gender,
                u.Avatar_Url,
                u.Bio,
                u.Create_At,
                COUNT(o.Order_Id) AS orderCount,
                COALESCE(SUM(o.Total_Price), 0) AS totalSpend
            FROM user u
            LEFT JOIN orders o
                ON u.User_Id = o.User_Id
                AND o.Status = 'COMPLETED'
            WHERE u.Role = 'USER'
            GROUP BY
                u.User_Id,
                u.Customer_Code,
                u.User_Name,
                u.Phone,
                u.Email,
                u.Date_Of_Birth,
                u.Gender,
                u.Avatar_Url,
                u.Bio,
                u.Create_At
        ) c
        WHERE (
            :keyword = ''
            OR LOWER(COALESCE(c.User_Name, '')) LIKE CONCAT('%', LOWER(:keyword), '%')
            OR COALESCE(c.Phone, '') LIKE CONCAT('%', :keyword, '%')
            OR LOWER(COALESCE(c.Email, '')) LIKE CONCAT('%', LOWER(:keyword), '%')
            OR LOWER(COALESCE(c.Customer_Code, '')) LIKE CONCAT('%', LOWER(:keyword), '%')
        )
        AND (
            :customerType = ''
            OR (:customerType = 'vip' AND c.totalSpend >= 10000000)
            OR (:customerType = 'regular' AND c.totalSpend >= 3000000 AND c.totalSpend < 10000000)
            OR (:customerType = 'new' AND c.totalSpend < 3000000)
        )
        AND (:minOrders < 0 OR c.orderCount >= :minOrders)
        AND (:maxOrders < 0 OR c.orderCount <= :maxOrders)
        ORDER BY c.Create_At DESC
    """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("keyword", keyword == null ? "" : keyword.trim())
                        .bind("customerType", customerType == null ? "" : customerType.trim())
                        .bind("minOrders", minOrders)
                        .bind("maxOrders", maxOrders)
                        .map((rs, ctx) -> mapCustomer(rs))
                        .list()
        );
    }
    public User getCustomerDetail(int userId) {
        String sql = """
        SELECT
            u.User_Id,
            u.Customer_Code,
            u.User_Name,
            u.Phone,
            u.Email,
            u.Date_Of_Birth,
            u.Gender,
            u.Avatar_Url,
            u.Bio,
            u.Create_At,
            COUNT(o.Order_Id) AS orderCount,
            COALESCE(SUM(o.Total_Price), 0) AS totalSpend
        FROM user u
        LEFT JOIN orders o
            ON u.User_Id = o.User_Id
            AND o.Status = 'COMPLETED'
        WHERE u.User_Id = :id
          AND u.Role = 'USER'
        GROUP BY
            u.User_Id,
            u.Customer_Code,
            u.User_Name,
            u.Phone,
            u.Email,
            u.Date_Of_Birth,
            u.Gender,
            u.Avatar_Url,
            u.Bio,
            u.Create_At
        LIMIT 1
    """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("id", userId)
                        .map((rs, ctx) -> mapCustomer(rs))
                        .findOne()
                        .orElse(null)
        );
    }

    public void deleteCustomer(int userId) {
        getJdbi().useTransaction(handle -> {
            handle.createUpdate("DELETE FROM favorite_products WHERE User_Id = :id")
                    .bind("id", userId)
                    .execute();

            handle.createUpdate("DELETE FROM reviews WHERE User_Id = :id")
                    .bind("id", userId)
                    .execute();

            handle.createUpdate("DELETE FROM contact WHERE User_Id = :id")
                    .bind("id", userId)
                    .execute();

            handle.createUpdate("DELETE FROM user_address WHERE User_Id = :id")
                    .bind("id", userId)
                    .execute();

            handle.createUpdate("DELETE FROM user WHERE User_Id = :id AND Role = 'USER'")
                    .bind("id", userId)
                    .execute();
        });
    }
    public List<User> filterCustomers(String keyword,
                                      String customerType,
                                      int minOrders,
                                      int maxOrders,
                                      int page,
                                      int pageSize) {

        String sql = """
        SELECT *
        FROM (
            SELECT
                u.User_Id,
                u.Customer_Code,
                u.User_Name,
                u.Phone,
                u.Email,
                u.Date_Of_Birth,
                u.Gender,
                u.Avatar_Url,
                u.Bio,
                u.Create_At,
                COUNT(o.Order_Id) AS orderCount,
                COALESCE(SUM(o.Total_Price), 0) AS totalSpend
            FROM user u
            LEFT JOIN orders o
                ON u.User_Id = o.User_Id
                AND o.Status = 'COMPLETED'
            WHERE u.Role = 'USER'
            GROUP BY
                u.User_Id,
                u.Customer_Code,
                u.User_Name,
                u.Phone,
                u.Email,
                u.Date_Of_Birth,
                u.Gender,
                u.Avatar_Url,
                u.Bio,
                u.Create_At
        ) c
        WHERE (
            :keyword = ''
            OR LOWER(COALESCE(c.User_Name, '')) LIKE CONCAT('%', LOWER(:keyword), '%')
            OR COALESCE(c.Phone, '') LIKE CONCAT('%', :keyword, '%')
            OR LOWER(COALESCE(c.Email, '')) LIKE CONCAT('%', LOWER(:keyword), '%')
            OR LOWER(COALESCE(c.Customer_Code, '')) LIKE CONCAT('%', LOWER(:keyword), '%')
        )
        AND (
            :customerType = ''
            OR (:customerType = 'vip' AND c.totalSpend >= 10000000)
            OR (:customerType = 'regular' AND c.totalSpend >= 3000000 AND c.totalSpend < 10000000)
            OR (:customerType = 'new' AND c.totalSpend < 3000000)
        )
        AND (:minOrders < 0 OR c.orderCount >= :minOrders)
        AND (:maxOrders < 0 OR c.orderCount <= :maxOrders)
        ORDER BY c.Create_At DESC
        LIMIT :limit OFFSET :offset
    """;

        int safePage = Math.max(page, 1);
        int safePageSize = Math.max(pageSize, 1);
        int offset = (safePage - 1) * safePageSize;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("keyword", keyword == null ? "" : keyword.trim())
                        .bind("customerType", customerType == null ? "" : customerType.trim())
                        .bind("minOrders", minOrders)
                        .bind("maxOrders", maxOrders)
                        .bind("limit", safePageSize)
                        .bind("offset", offset)
                        .map((rs, ctx) -> mapCustomer(rs))
                        .list()
        );
    }

    public int countFilteredCustomers(String keyword,
                                      String customerType,
                                      int minOrders,
                                      int maxOrders) {

        String sql = """
        SELECT COUNT(*)
        FROM (
            SELECT
                u.User_Id,
                COUNT(o.Order_Id) AS orderCount,
                COALESCE(SUM(o.Total_Price), 0) AS totalSpend,
                u.User_Name,
                u.Phone,
                u.Email,
                u.Customer_Code
            FROM user u
            LEFT JOIN orders o
                ON u.User_Id = o.User_Id
                AND o.Status = 'COMPLETED'
            WHERE u.Role = 'USER'
            GROUP BY
                u.User_Id,
                u.User_Name,
                u.Phone,
                u.Email,
                u.Customer_Code
        ) c
        WHERE (
            :keyword = ''
            OR LOWER(COALESCE(c.User_Name, '')) LIKE CONCAT('%', LOWER(:keyword), '%')
            OR COALESCE(c.Phone, '') LIKE CONCAT('%', :keyword, '%')
            OR LOWER(COALESCE(c.Email, '')) LIKE CONCAT('%', LOWER(:keyword), '%')
            OR LOWER(COALESCE(c.Customer_Code, '')) LIKE CONCAT('%', LOWER(:keyword), '%')
        )
        AND (
            :customerType = ''
            OR (:customerType = 'vip' AND c.totalSpend >= 10000000)
            OR (:customerType = 'regular' AND c.totalSpend >= 3000000 AND c.totalSpend < 10000000)
            OR (:customerType = 'new' AND c.totalSpend < 3000000)
        )
        AND (:minOrders < 0 OR c.orderCount >= :minOrders)
        AND (:maxOrders < 0 OR c.orderCount <= :maxOrders)
    """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("keyword", keyword == null ? "" : keyword.trim())
                        .bind("customerType", customerType == null ? "" : customerType.trim())
                        .bind("minOrders", minOrders)
                        .bind("maxOrders", maxOrders)
                        .mapTo(Integer.class)
                        .one()
        );
    }
    public List<User> getAllAdminAccounts() {
        String sql = """
        SELECT
            User_Id       AS userId,
            Customer_Code AS customerCode,
            User_Name     AS userName,
            Email         AS email,
            Phone         AS phone,
            Date_Of_Birth AS dateOfBirth,
            Gender        AS gender,
            Password      AS password,
            Google_Id     AS googleId,
            Avatar_Url    AS avatarUrl,
            Bio           AS bio,
            Create_At     AS createAt,
            Role          AS role
        FROM user
        WHERE Role = 'ADMIN'
        ORDER BY Create_At DESC
    """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .mapToBean(User.class)
                        .list()
        );
    }
}
