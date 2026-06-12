package dao;

import model.Review;
import model.ReviewReply;
import org.jdbi.v3.core.statement.Query;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ReviewDao extends BaseDao {

    public ReviewDao() {
        ensureTables();
    }

    public void ensureTables() {
        String createReviews = """
                CREATE TABLE IF NOT EXISTS reviews (
                    Review_Id INT AUTO_INCREMENT PRIMARY KEY,
                    Product_Id INT NOT NULL,
                    User_Id INT NOT NULL,
                    Rating INT NOT NULL,
                    Comment TEXT NOT NULL,
                    Status VARCHAR(30) DEFAULT 'APPROVED',
                    Shop_Reply TEXT NULL,
                    Create_At DATETIME DEFAULT CURRENT_TIMESTAMP
                )
                """;

        String createLikes = """
                CREATE TABLE IF NOT EXISTS review_likes (
                    Review_Like_Id INT AUTO_INCREMENT PRIMARY KEY,
                    Review_Id INT NOT NULL,
                    User_Id INT NOT NULL,
                    Created_At DATETIME DEFAULT CURRENT_TIMESTAMP,
                    UNIQUE KEY uq_review_like_user (Review_Id, User_Id)
                )
                """;

        String createReplies = """
                CREATE TABLE IF NOT EXISTS review_replies (
                    Reply_Id INT AUTO_INCREMENT PRIMARY KEY,
                    Review_Id INT NOT NULL,
                    User_Id INT NOT NULL,
                    Reply_Text TEXT NOT NULL,
                    Create_At DATETIME DEFAULT CURRENT_TIMESTAMP
                )
                """;

        getJdbi().useHandle(handle -> {
            handle.execute(createReviews);
            handle.execute(createLikes);
            handle.execute(createReplies);
        });

        tryAddColumn("ALTER TABLE reviews ADD COLUMN Status VARCHAR(30) DEFAULT 'APPROVED'");
        tryAddColumn("ALTER TABLE reviews ADD COLUMN Shop_Reply TEXT NULL");
    }

    private void tryAddColumn(String sql) {
        try {
            getJdbi().useHandle(handle -> handle.execute(sql));
        } catch (Exception ignored) {
        }
    }

    public List<Review> getReviewsByProductId(int productId, Integer rating, Integer currentUserId) {
        StringBuilder sql = new StringBuilder("""
                SELECT
                    r.Review_Id AS reviewId,
                    r.Product_Id AS productId,
                    r.User_Id AS userId,
                    COALESCE(u.User_Name, u.Email, 'Khách hàng') AS userName,
                    CAST(ROUND(COALESCE(r.Rating, 0)) AS SIGNED) AS rating,
                    r.Comment AS comment,
                    COALESCE(r.Status, 'APPROVED') AS status,
                    r.Shop_Reply AS shopReply,
                    r.Create_At AS createAt,
                    (SELECT COUNT(*) FROM review_likes rl WHERE rl.Review_Id = r.Review_Id) AS helpfulCount,
                    CASE WHEN :currentUserId > 0 AND EXISTS (
                        SELECT 1 FROM review_likes rl
                        WHERE rl.Review_Id = r.Review_Id AND rl.User_Id = :currentUserId
                    ) THEN TRUE ELSE FALSE END AS likedByCurrentUser
                FROM reviews r
                LEFT JOIN `user` u ON u.User_Id = r.User_Id
                WHERE r.Product_Id = :productId
                  AND COALESCE(r.Status, 'APPROVED') = 'APPROVED'
                """);

        if (rating != null && rating >= 1 && rating <= 5) {
            sql.append(" AND CAST(ROUND(COALESCE(r.Rating, 0)) AS SIGNED) = :rating");
        }

        sql.append(" ORDER BY r.Create_At DESC, r.Review_Id DESC");

        List<Review> reviews = getJdbi().withHandle(handle -> {
            Query query = handle.createQuery(sql.toString())
                    .bind("productId", productId)
                    .bind("currentUserId", currentUserId == null ? 0 : currentUserId);

            if (rating != null && rating >= 1 && rating <= 5) {
                query.bind("rating", rating);
            }

            return query.mapToBean(Review.class).list();
        });

        for (Review review : reviews) {
            review.setReplies(getRepliesByReviewId(review.getReviewId()));
        }

        return reviews;
    }

    public List<ReviewReply> getRepliesByReviewId(int reviewId) {
        String sql = """
                SELECT
                    rr.Reply_Id AS replyId,
                    rr.Review_Id AS reviewId,
                    rr.User_Id AS userId,
                    COALESCE(u.User_Name, u.Email, 'Khách hàng') AS userName,
                    rr.Reply_Text AS replyText,
                    rr.Create_At AS createAt
                FROM review_replies rr
                LEFT JOIN `user` u ON u.User_Id = rr.User_Id
                WHERE rr.Review_Id = :reviewId
                ORDER BY rr.Create_At ASC, rr.Reply_Id ASC
                """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("reviewId", reviewId)
                        .mapToBean(ReviewReply.class)
                        .list()
        );
    }

    public double getAverageRating(int productId) {
        String sql = """
                SELECT COALESCE(AVG(Rating), 0)
                FROM reviews
                WHERE Product_Id = :productId
                  AND COALESCE(Status, 'APPROVED') = 'APPROVED'
                """;

        BigDecimal avg = getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("productId", productId)
                        .mapTo(BigDecimal.class)
                        .one()
        );

        return avg == null ? 0 : Math.round(avg.doubleValue() * 10.0) / 10.0;
    }

    public int countReviews(int productId) {
        String sql = """
                SELECT COUNT(*)
                FROM reviews
                WHERE Product_Id = :productId
                  AND COALESCE(Status, 'APPROVED') = 'APPROVED'
                """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("productId", productId)
                        .mapTo(Integer.class)
                        .one()
        );
    }

    public Map<Integer, Integer> countReviewsByRating(int productId) {
        Map<Integer, Integer> result = new LinkedHashMap<>();

        for (int i = 5; i >= 1; i--) {
            result.put(i, 0);
        }

        String sql = """
                SELECT CAST(ROUND(COALESCE(Rating, 0)) AS SIGNED) AS rating,
                       COUNT(*) AS total
                FROM reviews
                WHERE Product_Id = :productId
                  AND COALESCE(Status, 'APPROVED') = 'APPROVED'
                GROUP BY CAST(ROUND(COALESCE(Rating, 0)) AS SIGNED)
                """;

        getJdbi().useHandle(handle ->
                handle.createQuery(sql)
                        .bind("productId", productId)
                        .map((rs, ctx) -> Map.entry(rs.getInt("rating"), rs.getInt("total")))
                        .forEach(entry -> {
                            int ratingValue = entry.getKey();

                            if (ratingValue >= 1 && ratingValue <= 5) {
                                result.put(ratingValue, entry.getValue());
                            }
                        })
        );

        return result;
    }

    public void addReview(int productId, int userId, int rating, String comment) {
        String sql = """
                INSERT INTO reviews (Product_Id, User_Id, Rating, Comment, Status, Create_At)
                VALUES (:productId, :userId, :rating, :comment, 'PENDING', NOW())
                """;

        getJdbi().useHandle(handle ->
                handle.createUpdate(sql)
                        .bind("productId", productId)
                        .bind("userId", userId)
                        .bind("rating", Math.max(1, Math.min(5, rating)))
                        .bind("comment", comment)
                        .execute()
        );
    }

    public void toggleLike(int reviewId, int userId) {
        boolean liked = getJdbi().withHandle(handle ->
                handle.createQuery("""
                                SELECT COUNT(*)
                                FROM review_likes
                                WHERE Review_Id = :reviewId AND User_Id = :userId
                                """)
                        .bind("reviewId", reviewId)
                        .bind("userId", userId)
                        .mapTo(Integer.class)
                        .one() > 0
        );

        if (liked) {
            getJdbi().useHandle(handle ->
                    handle.createUpdate("""
                                    DELETE FROM review_likes
                                    WHERE Review_Id = :reviewId AND User_Id = :userId
                                    """)
                            .bind("reviewId", reviewId)
                            .bind("userId", userId)
                            .execute()
            );
        } else {
            getJdbi().useHandle(handle ->
                    handle.createUpdate("""
                                    INSERT INTO review_likes (Review_Id, User_Id)
                                    VALUES (:reviewId, :userId)
                                    """)
                            .bind("reviewId", reviewId)
                            .bind("userId", userId)
                            .execute()
            );
        }
    }

    public void addReply(int reviewId, int userId, String replyText) {
        String sql = """
                INSERT INTO review_replies (Review_Id, User_Id, Reply_Text, Create_At)
                VALUES (:reviewId, :userId, :replyText, NOW())
                """;

        getJdbi().useHandle(handle ->
                handle.createUpdate(sql)
                        .bind("reviewId", reviewId)
                        .bind("userId", userId)
                        .bind("replyText", replyText)
                        .execute()
        );
    }

    public List<Review> getAdminReviews() {
        return getAdminReviews("", null, null);
    }

    public List<Review> getAdminReviews(String keyword, Integer rating, String status) {
        StringBuilder sql = new StringBuilder("""
                SELECT
                    r.Review_Id AS reviewId,
                    r.Product_Id AS productId,
                    r.User_Id AS userId,
                    COALESCE(u.User_Name, u.Email, 'Khách hàng') AS userName,
                    COALESCE(p.Product_Name, 'Sản phẩm đã xóa') AS productName,
                    CAST(ROUND(COALESCE(r.Rating, 0)) AS SIGNED) AS rating,
                    r.Comment AS comment,
                    COALESCE(r.Status, 'APPROVED') AS status,
                    r.Shop_Reply AS shopReply,
                    r.Create_At AS createAt,
                    (SELECT COUNT(*) FROM review_likes rl WHERE rl.Review_Id = r.Review_Id) AS helpfulCount
                FROM reviews r
                LEFT JOIN `user` u ON u.User_Id = r.User_Id
                LEFT JOIN products p ON p.Product_Id = r.Product_Id
                WHERE 1 = 1
                """);

        boolean hasKeyword = keyword != null && !keyword.isBlank();
        boolean hasRating = rating != null && rating >= 1 && rating <= 5;
        boolean hasStatus = status != null && !status.isBlank();

        if (hasKeyword) {
            sql.append("""
                     AND (
                        r.Comment LIKE CONCAT('%', :keyword, '%')
                        OR u.User_Name LIKE CONCAT('%', :keyword, '%')
                        OR u.Email LIKE CONCAT('%', :keyword, '%')
                        OR p.Product_Name LIKE CONCAT('%', :keyword, '%')
                    )
                    """);
        }

        if (hasRating) {
            sql.append(" AND CAST(ROUND(COALESCE(r.Rating, 0)) AS SIGNED) = :rating ");
        }

        if (hasStatus) {
            sql.append(" AND COALESCE(r.Status, 'APPROVED') = :status ");
        }

        sql.append("""
                 ORDER BY
                    CASE WHEN COALESCE(r.Status, 'APPROVED') = 'PENDING' THEN 0 ELSE 1 END,
                    r.Create_At DESC,
                    r.Review_Id DESC
                """);

        return getJdbi().withHandle(handle -> {
            Query query = handle.createQuery(sql.toString());

            if (hasKeyword) {
                query.bind("keyword", keyword.trim());
            }

            if (hasRating) {
                query.bind("rating", rating);
            }

            if (hasStatus) {
                query.bind("status", status.trim().toUpperCase());
            }

            return query.mapToBean(Review.class).list();
        });
    }

    public int countAllReviews() {
        String sql = "SELECT COUNT(*) FROM reviews";

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .mapTo(Integer.class)
                        .one()
        );
    }

    public int countReviewsByStatus(String status) {
        String sql = """
                SELECT COUNT(*)
                FROM reviews
                WHERE COALESCE(Status, 'APPROVED') = :status
                """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("status", status)
                        .mapTo(Integer.class)
                        .one()
        );
    }

    public double getAverageRatingAll() {
        String sql = "SELECT COALESCE(AVG(Rating), 0) FROM reviews";

        BigDecimal avg = getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .mapTo(BigDecimal.class)
                        .one()
        );

        return avg == null ? 0 : Math.round(avg.doubleValue() * 10.0) / 10.0;
    }

    public Map<Integer, Integer> countAllReviewsByRating() {
        Map<Integer, Integer> result = new LinkedHashMap<>();

        for (int i = 5; i >= 1; i--) {
            result.put(i, 0);
        }

        String sql = """
                SELECT CAST(ROUND(COALESCE(Rating, 0)) AS SIGNED) AS rating,
                       COUNT(*) AS total
                FROM reviews
                GROUP BY CAST(ROUND(COALESCE(Rating, 0)) AS SIGNED)
                """;

        getJdbi().useHandle(handle ->
                handle.createQuery(sql)
                        .map((rs, ctx) -> Map.entry(rs.getInt("rating"), rs.getInt("total")))
                        .forEach(entry -> {
                            int ratingValue = entry.getKey();

                            if (ratingValue >= 1 && ratingValue <= 5) {
                                result.put(ratingValue, entry.getValue());
                            }
                        })
        );

        return result;
    }

    public void updateReviewStatus(int reviewId, String status) {
        String normalizedStatus = status == null ? "" : status.trim().toUpperCase();

        if (!"PENDING".equals(normalizedStatus)
                && !"APPROVED".equals(normalizedStatus)
                && !"HIDDEN".equals(normalizedStatus)) {
            return;
        }

        String sql = """
                UPDATE reviews
                SET Status = :status
                WHERE Review_Id = :reviewId
                """;

        getJdbi().useHandle(handle ->
                handle.createUpdate(sql)
                        .bind("reviewId", reviewId)
                        .bind("status", normalizedStatus)
                        .execute()
        );
    }

    public void updateShopReply(int reviewId, String replyText) {
        String sql = """
                UPDATE reviews
                SET Shop_Reply = :replyText
                WHERE Review_Id = :reviewId
                """;

        getJdbi().useHandle(handle ->
                handle.createUpdate(sql)
                        .bind("reviewId", reviewId)
                        .bind("replyText", replyText)
                        .execute()
        );
    }
}