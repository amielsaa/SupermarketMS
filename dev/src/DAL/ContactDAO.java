package DAL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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

    public boolean deleteContact(int bn, String name)  {
        String sql = "DELETE FROM Contacts WHERE bn = ?, name = ?";

        try{
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bn);
            pstmt.setString(2, name);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }


}
