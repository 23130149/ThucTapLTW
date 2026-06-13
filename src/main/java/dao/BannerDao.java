package dao;

import model.Banner;

import java.util.List;

public class BannerDao extends BaseDao {

    public List<Banner> findActiveBanners() {
        String sql = """
            SELECT
                Banner_Id AS bannerId,
                Title_Line_1 AS titleLine1,
                Title_Line_2 AS titleLine2,
                Subtitle AS subtitle,
                Image_Url AS imageUrl,
                Target_Url AS targetUrl,
                COALESCE(Status, 'ACTIVE') AS status,
                COALESCE(Sort_Order, 0) AS sortOrder,
                Create_At AS createAt,
                Update_At AS updateAt
            FROM banner
            WHERE COALESCE(Status, 'ACTIVE') = 'ACTIVE'
            ORDER BY Sort_Order ASC, Create_At DESC
        """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .mapToBean(Banner.class)
                        .list()
        );
    }

    public List<Banner> getAdminBanners(String keyword, String status) {
        keyword = keyword == null ? "" : keyword.trim();
        status = status == null ? "" : status.trim();

        String sql = """
            SELECT
                Banner_Id AS bannerId,
                Title_Line_1 AS titleLine1,
                Title_Line_2 AS titleLine2,
                Subtitle AS subtitle,
                Image_Url AS imageUrl,
                Target_Url AS targetUrl,
                COALESCE(Status, 'ACTIVE') AS status,
                COALESCE(Sort_Order, 0) AS sortOrder,
                Create_At AS createAt,
                Update_At AS updateAt
            FROM banner
            WHERE
                (:status = '' OR COALESCE(Status, 'ACTIVE') = :status)
                AND (
                    :keyword = ''
                    OR Title_Line_1 LIKE CONCAT('%', :keyword, '%')
                    OR Title_Line_2 LIKE CONCAT('%', :keyword, '%')
                    OR Subtitle LIKE CONCAT('%', :keyword, '%')
                    OR Image_Url LIKE CONCAT('%', :keyword, '%')
                    OR Target_Url LIKE CONCAT('%', :keyword, '%')
                )
            ORDER BY Sort_Order ASC, Create_At DESC
        """;

        String finalKeyword = keyword;
        String finalStatus = status;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("keyword", finalKeyword)
                        .bind("status", finalStatus)
                        .mapToBean(Banner.class)
                        .list()
        );
    }

    public Banner getBannerById(int bannerId) {
        String sql = """
            SELECT
                Banner_Id AS bannerId,
                Title_Line_1 AS titleLine1,
                Title_Line_2 AS titleLine2,
                Subtitle AS subtitle,
                Image_Url AS imageUrl,
                Target_Url AS targetUrl,
                COALESCE(Status, 'ACTIVE') AS status,
                COALESCE(Sort_Order, 0) AS sortOrder,
                Create_At AS createAt,
                Update_At AS updateAt
            FROM banner
            WHERE Banner_Id = :bannerId
        """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("bannerId", bannerId)
                        .mapToBean(Banner.class)
                        .findOne()
                        .orElse(null)
        );
    }

    public int countAllBanners() {
        String sql = "SELECT COUNT(*) FROM banner";

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .mapTo(Integer.class)
                        .one()
        );
    }

    public int countBannersByStatus(String status) {
        String sql = """
            SELECT COUNT(*)
            FROM banner
            WHERE COALESCE(Status, 'ACTIVE') = :status
        """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("status", status)
                        .mapTo(Integer.class)
                        .one()
        );
    }

    public boolean insert(Banner banner) {
        String sql = """
            INSERT INTO banner (
                Title_Line_1,
                Title_Line_2,
                Subtitle,
                Image_Url,
                Target_Url,
                Status,
                Sort_Order,
                Create_At,
                Update_At
            ) VALUES (
                :titleLine1,
                :titleLine2,
                :subtitle,
                :imageUrl,
                :targetUrl,
                :status,
                :sortOrder,
                NOW(),
                NOW()
            )
        """;

        return getJdbi().withHandle(handle ->
                handle.createUpdate(sql)
                        .bindBean(banner)
                        .execute() > 0
        );
    }

    public boolean update(Banner banner) {
        String sql = """
            UPDATE banner
            SET Title_Line_1 = :titleLine1,
                Title_Line_2 = :titleLine2,
                Subtitle = :subtitle,
                Image_Url = :imageUrl,
                Target_Url = :targetUrl,
                Status = :status,
                Sort_Order = :sortOrder,
                Update_At = NOW()
            WHERE Banner_Id = :bannerId
        """;

        return getJdbi().withHandle(handle ->
                handle.createUpdate(sql)
                        .bindBean(banner)
                        .execute() > 0
        );
    }

    public boolean updateStatus(int bannerId, String status) {
        String sql = """
            UPDATE banner
            SET Status = :status,
                Update_At = NOW()
            WHERE Banner_Id = :bannerId
        """;

        return getJdbi().withHandle(handle ->
                handle.createUpdate(sql)
                        .bind("bannerId", bannerId)
                        .bind("status", status)
                        .execute() > 0
        );
    }

    public boolean delete(int bannerId) {
        String sql = """
            DELETE FROM banner
            WHERE Banner_Id = :bannerId
        """;

        return getJdbi().withHandle(handle ->
                handle.createUpdate(sql)
                        .bind("bannerId", bannerId)
                        .execute() > 0
        );
    }
}