package DAL;

import BusinessLayer.Supplier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

public class SupplierDAO extends DalController {
    private HashMap<Integer, Supplier> BN_To_Supplier;


    public SupplierDAO(){
        super("Suppliers");
        BN_To_Supplier = new HashMap<Integer, Supplier>();
    }

    public void select(){}

    //deliverbydays should be boolean
    //selfdelivery should be boolean
    //paymentdetails should be string
    public void insertSupplier(int bn, String name, int bankaccount, String paymentdetails,boolean deliverybydays, boolean selfdelivery) throws Exception {
        String sql = "INSERT INTO Suppliers(bn, name, bankaccount, paymentdetails, deliverybydays, selfdelivery) VALUES(?,?,?,?,?,?)";

        try{
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bn);
            pstmt.setString(2, name);
            pstmt.setInt(3, bankaccount);
            pstmt.setString(4, paymentdetails);
            pstmt.setBoolean(5, deliverybydays);
            pstmt.setBoolean(6, selfdelivery);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new Exception(e.getMessage()); //todo: idk
        }
    }

    public void deleteSupplier(int bn) throws Exception {
        String sql = "DELETE FROM Suppliers WHERE bn = ?";

        try{
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bn);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        }
    }

    public void insertQuantityAgreement(int bn, String itemname, String producer, int price) throws Exception {
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
            throw new Exception(e.getMessage());
        }
    }

    public void deleteQuantityAgreement(int bn, String itemname, String producer) throws Exception {
        String sql = "DELETE FROM QuantityAgreement WHERE bn = ?, itemname = ?, producer = ?";

        try{
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bn);
            pstmt.setString(2, itemname);
            pstmt.setString(3, producer);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        }
    }

    public void insertContact(int bn, String name, String phone) throws Exception {
        String sql = "INSERT INTO Contacts(bn, name, phone) VALUES(?,?,?)";

        try{
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bn);
            pstmt.setString(2, name);
            pstmt.setString(3, phone);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        }
    }

    public void deleteContact(int bn, String name) throws Exception {
        String sql = "DELETE FROM Contacts WHERE bn = ?, name = ?";

        try{
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bn);
            pstmt.setString(2, name);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        }
    }

    public void insertDaysToDeliver(int bn, int day) throws Exception {
        String sql = "INSERT INTO DaysToDeliver(bn, day) VALUES(?,?)";

        try{
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bn);
            pstmt.setInt(2, day);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        }
    }

    public void deleteDaysToDeliver(int bn, int day) throws Exception {
        String sql = "DELETE FROM DaysToDeliver WHERE bn = ?, day = ?";

        try{
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bn);
            pstmt.setInt(2, day);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        }
    }

    public void insertDiscounts(int bn, String itemname, String producer, int itemamount, int itemdiscount ) throws Exception {
        String sql = "INSERT INTO Discounts(bn, itemname, producer, itemamount, itemdiscount ) VALUES(?,?,?,?,?)";

        try{
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bn);
            pstmt.setString(2, itemname);
            pstmt.setString(3, producer);
            pstmt.setInt(4, itemamount);
            pstmt.setInt(5, itemdiscount);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        }
    }

    public void deleteDiscounts(int bn, String itemname, String producer, int itemamount) throws Exception {
        String sql = "DELETE FROM Discounts WHERE bn = ?, itemname = ?, producer = ?, itemamount = ?";

        try{
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bn);
            pstmt.setString(2, itemname);
            pstmt.setString(3, producer);
            pstmt.setInt(4, itemamount);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        }
    }

    public boolean containsSupplier(int bn){
        return BN_To_Supplier.containsKey(bn);
        //should add a check here if its in the dataBase, if it is-takes it and puts in in the map and returns true.
    }
    public void addSupplierToMap(Supplier supplier){
        BN_To_Supplier.put(supplier.getBusiness_Num(),supplier);
    }






}
