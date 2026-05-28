package dao;

import model.Contact;

import java.util.List;
import java.sql.Connection;

public class ContactDao extends BaseDao {

    public void insert(Contact contact) {

        String sql = """
            INSERT INTO contact
            (Contact_Name, Contact_Email, Phone, Subject, Message, User_Id, Create_At, Update_At)
            VALUES
            (:name, :email, :phone, :subject, :message, :userId, NOW(), NOW())
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
            User_Id        AS userId
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
            Contact_Id    AS contactId,
            Contact_Name  AS contactName,
            Contact_Email AS contactEmail,
            Phone         AS phone,
            Subject       AS subject,
            Message       AS message,
            User_Id       AS userId
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

}
