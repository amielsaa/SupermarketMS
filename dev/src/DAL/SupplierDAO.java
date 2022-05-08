package DAL;

import BusinessLayer.Contact;
import BusinessLayer.QuantityAgreement;
import BusinessLayer.Supplier;
import misc.Days;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

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
    public boolean insertSupplier(int bn, String name, int bankaccount, String paymentdetails,int deliverybydays, int selfdelivery)  {
        String sql = "INSERT INTO Suppliers(bn, name, bankaccount, paymentdetails, deliverybydays, selfdelivery) VALUES(?,?,?,?,?,?)";

        try{
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bn);
            pstmt.setString(2, name);
            pstmt.setInt(3, bankaccount);
            pstmt.setString(4, paymentdetails);
            pstmt.setInt(5, deliverybydays);
            pstmt.setInt(6, selfdelivery);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    public boolean deleteSupplier(int bn)  {
        String sql = "DELETE FROM Suppliers WHERE bn = ?";

        try{
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bn);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }


    public boolean insertDiscounts(int bn, String itemname, String producer, int itemamount, int itemdiscount )  {
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
            return false;
        }
        return true;
    }

    public boolean deleteDiscounts(int bn, String itemname, String producer, int itemamount)  {
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
            return false;
        }
        return true;
    }


    public boolean addNewSupplier(Supplier supplier){

        if(insertSupplier(supplier.getBusiness_Num(), supplier.getName(), supplier.getBank_Acc_Num(), supplier.getPayment_Details().toString(), boolToInt(supplier.isDelivery_By_Days()), boolToInt(supplier.isSelf_Delivery_Or_Pickup()))) {
            return true;
        }
        return false;
    }

    public Supplier getSupplier(int bn){
        return BN_To_Supplier.get(bn);
    }

    public boolean removeSupplier(int bn){
        if(deleteSupplier(bn)) {
            if (BN_To_Supplier.containsKey(bn)) {
                BN_To_Supplier.remove(bn);
            }
            return true;
        }
        return false;
    }
    public boolean containsSupplier(int bn){
        if(BN_To_Supplier.containsKey(bn))
            return true;
        return selectSupplier(bn);

        //should add a check here if its in the dataBase, if it is-takes it and puts in in the map and returns true else, returns false.
        //this function is the most important of all!! when I check for a supplier and it doesnt exists on the map its your responsibility
        //that it would show up on the map from the database! so when i ever wanna like get the agreement of this supplier it will already be
        //in the map!
    }

    private boolean selectSupplier(int bn) {
        //gets bn, checks db if bn exists.
        //if it does, adds it to HM and return true
        //if it doesn't, returns false

        return false;
    }

    public boolean updateSupplierDeliveryDays(int bn, Set<Integer> days){
        //remove old days, insert new ones
        try{
            Supplier supp = getSupplier(bn);
            for(Days d : supp.getDays_To_Deliver()){
                deleteDaysToDeliver(bn, dayConverter(d));
            }
            for(int d : days){
                insertDaysToDeliver(bn, d);
            }
            return true;
        }
        catch(Exception e){
            return false;
        }
    }
    public boolean updateSupplierSelfDelivery(int business_num, boolean selfDelivery){
        // UwU
        int selfDeliveryInt = 0;
        if(selfDelivery)
            selfDeliveryInt=1;
        if(update(this.tableName, "selfdelivery", business_num, selfDeliveryInt))
            return true;
        return false;
    }

    private int dayConverter(Days day) {
        if (day==Days.sunday)
            return 1;
        else if (day==Days.monday)
            return 2;
        else if (day==Days.tuesday)
            return 3;
        else if (day==Days.wednesday)
            return 4;
        else if (day==Days.thursday)
            return 5;
        else if (day==Days.friday)
            return 6;
        else if (day==Days.saturday)
            return 7;
        return 0;
    }

    private int boolToInt(boolean b){
        int x = 0;
        if(b) x++;
        return x;
    }






}
