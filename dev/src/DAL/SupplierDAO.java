package DAL;

import BusinessLayer.Supplier;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class SupplierDAO extends DalController {
    private HashMap<Integer, Supplier> BN_To_Supplier;


    public SupplierDAO(){
        super("Suppliers");
        BN_To_Supplier = new HashMap<Integer, Supplier>();
    }


    private boolean insertSupplier(int bn, String name, int bankaccount, String paymentdetails, int selfdelivery)  {
        String sql = "INSERT INTO Suppliers(bn, name, bankaccount, paymentdetails, selfdelivery) VALUES(?,?,?,?,?)";

        try{
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bn);
            pstmt.setString(2, name);
            pstmt.setInt(3, bankaccount);
            pstmt.setString(4, paymentdetails);
            pstmt.setInt(5, selfdelivery);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    private boolean deleteSupplier(int bn)  {
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


    public boolean addNewSupplier(Supplier supplier){
        return insertSupplier(supplier.getBusiness_Num(), supplier.getName(), supplier.getBank_Acc_Num(), supplier.getPayment_Details().toString(), boolToInt(supplier.isSelf_Delivery_Or_Pickup()));
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
        return checkSupplierInDB(bn);

    }

    // only checks if supplier in DB
    // and adds supplier to HM
    private boolean checkSupplierInDB(int bn) {
        Supplier b = selectSupplier(bn);
        if(b != null) {
            insertSupplierToHM(b);
            return true;
        }
        return false;
    }

    public Supplier selectSupplier(int bn){
        String sql = "select * from Suppliers where bn = ?";

        try{
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,bn);
            ResultSet rs = pstmt.executeQuery();
            Supplier supp = null;
            while (rs.next()) {
                supp = new Supplier(rs.getInt("bn"), rs.getString("name"),rs.getInt("bankaccount"),rs.getString("paymentdetails"),rs.getInt("selfdelivery"));

            }
            return supp; //we know its not null because of containsSupplier() function

        } catch (SQLException e) {
            return null; //todo
        }

    }

    public boolean updateSupplierSelfDelivery(int business_num, boolean selfDelivery){
        return update(this.tableName, "bn", "selfdelivery", business_num, boolToInt(selfDelivery));
    }


    private int boolToInt(boolean b){
        int x = 0;
        if(b) x++;
        return x;
    }

    public boolean updateSupplierPaymentDetails(int bn, String paymentDetails){
        return update(tableName, "bn", "paymentdetails", bn, paymentDetails);
    }

    public boolean updateSupplierBankAccount(int bn, int bank_account){
        return update(tableName, "bn", "bankaccount", bn, bank_account);
    }


    public void insertSupplierToHM(Supplier s){
        //we assert there is no supplier copies because of all the checks prior to this function
        BN_To_Supplier.put(s.getBusiness_Num(),s);
    }

    public void loadAllSuppliers(){
        String sql = "select * from Suppliers";

        try{
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            Supplier supp = null;
            while (rs.next()) {
                supp = new Supplier(rs.getInt("bn"), rs.getString("name"),rs.getInt("bankaccount"),rs.getString("paymentdetails"),rs.getInt("selfdelivery"));
                if(!BN_To_Supplier.containsKey(supp.getBusiness_Num()))
                    insertSupplierToHM(supp);
            }

        } catch (SQLException e) {

        }
    }

    public Collection<Supplier> getAllSuppliers(){
        return BN_To_Supplier.values();
    }




}
