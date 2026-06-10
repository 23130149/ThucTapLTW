package dao;

import model.Contact;

import java.util.List;

public class ContactDao extends BaseDao {

    public void insert(Contact contact) {

        String sql = """
            INSERT INTO contact
            (Contact_Name, Contact_Email, Phone, Subject, Message, User_Id, Status, Create_At, Update_At)
            VALUES
            (:name, :email, :phone, :subject, :message, :userId, 'NEW', NOW(), NOW())
        """;

        getJdbi().withHandle(handle ->
                handle.createUpdate(sql)
                        .bind("name", contact.getContactName())
                        .bind("email", contact.getContactEmail())
                        .bind("phone", contact.getPhone())
                        .bind("subject", contact.getSubject())
                        .bind("message", contact.getMessage())
                        .bind("userId", contact.getUserId())
                        .execute()
        );
    }
    public List<Contact> findAll() {
        String sql = """
        SELECT
            Contact_Id     AS contactId,
            Contact_Name   AS contactName,
            Contact_Email  AS contactEmail,
            Phone          AS phone,
            Subject        AS subject,
            Message        AS message,
            User_Id        AS userId,
            Create_At      AS createAt,
            Update_At      AS updateAt,
            COALESCE(Status, 'NEW') AS status
        FROM contact
        ORDER BY Create_At DESC
    """;

        return getJdbi().withHandle(h ->
                h.createQuery(sql)
                        .mapToBean(Contact.class)
                        .list()
        );
    }


    public int count() {
        return getJdbi().withHandle(h ->
                h.createQuery("SELECT COUNT(*) FROM contact")
                        .mapTo(int.class)
                        .one()
        );
    }

    public void delete(int id) {
        getJdbi().withHandle(h ->
                h.createUpdate("DELETE FROM contact WHERE Contact_Id = :id")
                        .bind("id", id)
                        .execute()
        );
    }
    public List<Contact> search(String keyword) {
        String sql = """
        SELECT
            Contact_Id     AS contactId,
            Contact_Name   AS contactName,
            Contact_Email  AS contactEmail,
            Phone          AS phone,
            Subject        AS subject,
            Message        AS message,
            User_Id        AS userId,
            Create_At      AS createAt,
            Update_At      AS updateAt,
            COALESCE(Status, 'NEW') AS status
        FROM contact
        WHERE
            Contact_Name LIKE :kw
            OR Contact_Email LIKE :kw
            OR Subject LIKE :kw
        ORDER BY Create_At DESC
    """;

        return getJdbi().withHandle(h ->
                h.createQuery(sql)
                        .bind("kw", "%" + keyword + "%")
                        .mapToBean(Contact.class)
                        .list()
        );
    }
    public Contact findById(int contactId) {
        String sql = """
        SELECT
            Contact_Id     AS contactId,
            Contact_Name   AS contactName,
            Contact_Email  AS contactEmail,
            Phone          AS phone,
            Subject        AS subject,
            Message        AS message,
            User_Id        AS userId,
            Create_At      AS createAt,
            Update_At      AS updateAt,
            COALESCE(Status, 'NEW') AS status
        FROM contact
        WHERE Contact_Id = :contactId
    """;

        return getJdbi().withHandle(h ->
                h.createQuery(sql)
                        .bind("contactId", contactId)
                        .mapToBean(Contact.class)
                        .findOne()
                        .orElse(null)
        );
    }
    public int countByStatus(String status) {
        String sql = """
        SELECT COUNT(*)
        FROM contact
        WHERE COALESCE(Status, 'NEW') = :status
    """;

        return getJdbi().withHandle(h ->
                h.createQuery(sql)
                        .bind("status", status)
                        .mapTo(int.class)
                        .one()
        );
    }

    public void updateStatus(int contactId, String status) {
        String sql = """
        UPDATE contact
        SET Status = :status,
            Update_At = NOW()
        WHERE Contact_Id = :contactId
    """;

        getJdbi().withHandle(h ->
                h.createUpdate(sql)
                        .bind("status", status)
                        .bind("contactId", contactId)
                        .execute()
        );
    }
    public List<Contact> findContacts(String keyword, String status) {
        StringBuilder sql = new StringBuilder("""
        SELECT
            Contact_Id     AS contactId,
            Contact_Name   AS contactName,
            Contact_Email  AS contactEmail,
            Phone          AS phone,
            Subject        AS subject,
            Message        AS message,
            User_Id        AS userId,
            Create_At      AS createAt,
            Update_At      AS updateAt,
            COALESCE(Status, 'NEW') AS status
        FROM contact
        WHERE 1 = 1
    """);

        if (keyword != null && !keyword.isBlank()) {
            sql.append("""
            AND (
                Contact_Name LIKE :keyword
                OR Contact_Email LIKE :keyword
                OR Phone LIKE :keyword
                OR Subject LIKE :keyword
                OR Message LIKE :keyword
            )
        """);
        }

        if (status != null && !status.isBlank()) {
            sql.append(" AND COALESCE(Status, 'NEW') = :status ");
        }

        sql.append(" ORDER BY Create_At DESC ");

        return getJdbi().withHandle(h -> {
            var query = h.createQuery(sql.toString());

            if (keyword != null && !keyword.isBlank()) {
                query.bind("keyword", "%" + keyword.trim() + "%");
            }

            if (status != null && !status.isBlank()) {
                query.bind("status", status.trim());
            }

            return query.mapToBean(Contact.class).list();
        });
    }
}
