package Suppliers.DAL;

import Suppliers.BusinessLayer.Contact;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class ContactDAO extends DalController{
    public ContactDAO() {
        super("Contacts");
    }

    public boolean insertContact(int bn, String name, String phone)  {
        String sql = "INSERT INTO Contacts(bn, name, phone) VALUES(?,?,?)";

        try{
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bn);
            pstmt.setString(2, name);
            pstmt.setString(3, phone);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    public boolean deleteContact(int bn, String phone)  {
        String sql = "DELETE FROM Contacts WHERE bn = ? and phone = ?";

        try{
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bn);
            pstmt.setString(2, phone);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    public List<Contact> selectAllContacts(int bn){
        String sql = "select bn, phone, name from Contacts where bn = ?";

        try{
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,bn);
            ResultSet rs = pstmt.executeQuery();
            List<Contact> cc = new LinkedList<>();
            Contact c;
            while (rs.next()) {
                c = new Contact(rs.getString("name"),rs.getString("phone"));
                cc.add(c);
            }
            return cc;

        } catch (SQLException e) {
            return null; //todo
        }

    }


}
