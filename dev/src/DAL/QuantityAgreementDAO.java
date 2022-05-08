package DAL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class QuantityAgreementDAO extends DalController{
    public QuantityAgreementDAO() {
        super("QuantityAgreement");
    }

    public boolean insertQuantityAgreement(int bn, String itemname, String producer, int price)  {
        String sql = "INSERT INTO QuantityAgreement(bn, itemname, producer, price) VALUES(?,?,?,?)";

        try{
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bn);
            pstmt.setString(2, itemname);
            pstmt.setString(3, producer);
            pstmt.setDouble(4, price);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    public boolean deleteQuantityAgreement(int bn, String itemname, String producer)  {
        String sql = "DELETE FROM QuantityAgreement WHERE bn = ?, itemname = ?, producer = ?";

        try{
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bn);
            pstmt.setString(2, itemname);
            pstmt.setString(3, producer);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }




}
