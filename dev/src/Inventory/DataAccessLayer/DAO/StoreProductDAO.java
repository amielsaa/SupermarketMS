package Inventory.DataAccessLayer.DAO;

import Inventory.BuisnessLayer.Objects.*;
import Inventory.DataAccessLayer.DalController;
import Inventory.DataAccessLayer.IdentityMap.StoreProductIdentityMap;

import java.sql.*;
import java.util.*;
import java.util.Date;

public class StoreProductDAO extends DalController {
    private StoreProductIdentityMap storeProductIdentityMap;

    public StoreProductDAO(String tableName) {
        super(tableName);
        this.storeProductIdentityMap = new StoreProductIdentityMap();
    }

    public void deleteStoredData() {
        storeProductIdentityMap.deleteAll();
    }

    public void DeleteSP(String idColName, int id) {
        storeProductIdentityMap.deleteSP(id);
        this.Delete(idColName,id);
    }

    public StoreProduct InsertStoreProduct(int productid, int storeid, int quantityinstore, int quantityinwarehouse, Date expdate, List<Location> locations) {
        String sql;
        if(StoreProductExists(productid,storeid,expdate))
            return UpdateStoreProduct(productid, storeid, quantityinstore, quantityinwarehouse, expdate, locations);
        else
            sql = "INSERT INTO StoreProducts(productid, storeid,quantityinstore," +
                    "quantityinwarehouse, expdate, locations) " +
                    "VALUES(?,?,?,?,?,?)";



        try(Connection conn = this.makeConnection()){

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,productid);
            pstmt.setInt(2,storeid);
            pstmt.setInt(3,quantityinstore);
            pstmt.setInt(4,quantityinwarehouse);
            pstmt.setString(5, String.format("%d/%d/%d",expdate.getDate(),expdate.getMonth()+1,expdate.getYear()+1900));
            pstmt.setString(6, getLocationsByString(locations));

            pstmt.executeUpdate();

            return storeProductIdentityMap.addStoreProduct(productid,new StoreProduct(storeid,quantityinstore,quantityinwarehouse,expdate,locations)).get(0);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new IllegalArgumentException("Store product insertion to database failed, please try again.");
        }
    }

    public StoreProduct UpdateStoreProduct(int productid, int storeid, int quantityinstore, int quantityinwarehouse, Date expdate, List<Location> locations) {
        String sql = "UPDATE StoreProducts SET quantityinstore=(?)," +
                "quantityinwarehouse=(?),expdate=(?),locations=(?)" +
                " WHERE productid=(?) and storeid=(?) and expdate=(?)";
        try(Connection conn = this.makeConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1,quantityinstore);
            pstmt.setInt(2,quantityinwarehouse);
            pstmt.setString(3, String.format("%d/%d/%d",expdate.getDate(),expdate.getMonth()+1,expdate.getYear()+1900));
            pstmt.setString(4, getLocationsByString(locations));
            pstmt.setInt(5,productid);
            pstmt.setInt(6,storeid);
            pstmt.setString(7, String.format("%d/%d/%d",expdate.getDate(),expdate.getMonth()+1,expdate.getYear()+1900));

            pstmt.executeUpdate();

            return storeProductIdentityMap.addStoreProduct(productid,new StoreProduct(storeid,quantityinstore,quantityinwarehouse,expdate,locations)).get(0);

        }catch(SQLException e) {
            //System.out.println(e.getMessage());
            throw new IllegalArgumentException("Update failed.");
        }
    }

    public boolean StoreProductExists(int productid, int storeid, Date expdate) {
        if(storeProductIdentityMap.storeProductsExists(productid,storeid,expdate))
            return true;
        String sql = "SELECT * FROM StoreProducts WHERE productid = ? and storeid = ? and expdate = '"+String.format("%d/%d/%d",expdate.getDate(),expdate.getMonth()+1,expdate.getYear()+1900)+"'";

        try(Connection conn = this.makeConnection()) {
            //Connection conn = this.makeConnection();
            PreparedStatement stmt  = conn.prepareStatement(sql);
            stmt.setInt(1,productid);
            stmt.setInt(2,storeid);
            ResultSet rs    = stmt.executeQuery();
            while(rs.next())
                return 0 != rs.getInt(1);
            return false;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new IllegalArgumentException("Store products fetch failed.");
        }
    }

    public Map<Integer,List<StoreProduct>> SelectAll() {
        if(storeProductIdentityMap.isPulled_all_data())
            return storeProductIdentityMap.getStoreProductsMap();
        String sql = "SELECT * FROM StoreProducts";
        Map<Integer,List<StoreProduct>> spMap = new HashMap<>();
        try(Connection conn = this.makeConnection()) {
            //Connection conn = this.makeConnection();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);

            // loop through the result set
            while (rs.next()) {
                int prodid = rs.getInt("productid");
                StoreProduct sp = new StoreProduct(rs.getInt("storeid"),
                        rs.getInt("quantityinstore"),rs.getInt("quantityinwarehouse")
                        ,getDateByString(rs.getString("expdate")),getLocationListByString(rs.getString("locations")));
                spMap.put(prodid, storeProductIdentityMap.addStoreProduct(prodid,sp));
            }
            storeProductIdentityMap.setPulled_all_data(true);
            return spMap;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new IllegalArgumentException("Store products fetch failed.");
        }


    }

    private Date getDateByString(String expDate) {
        String[] res = expDate.split("/");
        if(res.length==3 && res[2].length()==4)
            return new Date(Integer.parseInt(res[2])-1900,Integer.parseInt(res[1])-1,Integer.parseInt(res[0]));
        throw new IllegalArgumentException("Date format isn't valid.");
    }

    private List<Location> getLocationListByString(String location) {
        List<Location> locationList = new ArrayList<>();
        String[] locations = location.split("&");
        for(String item : locations) {
            String[] itemComponent = item.split("-");
            Locations eLocation = Locations.valueOf(itemComponent[0].toUpperCase());
            locationList.add(new Location(eLocation,Integer.parseInt(itemComponent[1]),Integer.parseInt(itemComponent[2])));
        }
        return locationList;

    }

    private String getLocationsByString(List<Location> locations) {
        String res = "";
        for(Location loc : locations) {
            res+= loc.getLocationName() + "-" + loc.getAisle() + "-" + loc.getShelfNum() +"&";
        }
        return res.substring(0,res.length()-1);
    }


}
