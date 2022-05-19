package Inventory.DataAccessLayer.DAO;

import Inventory.BuisnessLayer.Objects.*;
import Inventory.DataAccessLayer.DalController;
import Inventory.DataAccessLayer.Mappers.StoreProductMapper;

import java.sql.*;
import java.util.*;
import java.util.Date;

public class StoreProductDAO extends DalController {
    private StoreProductMapper storeProductMapper;

    public StoreProductDAO(String tableName) {
        super(tableName);
        this.storeProductMapper = new StoreProductMapper();
    }

    public void DeleteSP(String idColName, int id) {
        storeProductMapper.deleteSP(id);
        this.Delete(idColName,id);
    }

    public StoreProduct InsertStoreProduct(int productid, int storeid, int quantityinstore, int quantityinwarehouse, Date expdate, List<Location> locations) {
        String sql;
        if(storeProductMapper.storeProductsExists(productid,storeid,expdate))
            sql = "UPDATE StoreProducts SET productid=(?), storeid=(?), quantityinstore=(?)," +
                    "quantityinwarehouse=(?),expdate=(?),locations=(?)";
        else
            sql = "INSERT INTO StoreProducts(productid, storeid,quantityinstore," +
                    "quantityinwarehouse, expdate, locations) " +
                    "VALUES(?,?,?,?,?,?)";



        try{

            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, productid);
            pstmt.setInt(2,storeid);
            pstmt.setInt(3,quantityinstore);
            pstmt.setInt(4,quantityinwarehouse);
            pstmt.setString(5, String.format("%d/%d/%d",expdate.getDate(),expdate.getMonth()+1,expdate.getYear()+1900));
            pstmt.setString(6, getLocationsByString(locations));

            pstmt.executeUpdate();

            return storeProductMapper.addStoreProduct(productid,new StoreProduct(storeid,quantityinstore,quantityinwarehouse,expdate,locations)).get(0);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new IllegalArgumentException("Store product insertion to database failed, please try again.");
        }
    }

    public Map<Integer,List<StoreProduct>> SelectAll() {
        if(storeProductMapper.isPulled_all_data())
            return storeProductMapper.getStoreProductsMap();
        String sql = "SELECT * FROM StoreProducts";
        Map<Integer,List<StoreProduct>> spMap = new HashMap<>();
        try {
            Connection conn = this.makeConnection();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);

            // loop through the result set
            while (rs.next()) {
                int prodid = rs.getInt("productid");
                StoreProduct sp = new StoreProduct(rs.getInt("storeid"),
                        rs.getInt("quantityinstore"),rs.getInt("quantityinwarehouse")
                        ,getDateByString(rs.getString("expdate")),getLocationListByString(rs.getString("locations")));
                spMap.put(prodid,storeProductMapper.addStoreProduct(prodid,sp));
            }
            storeProductMapper.setPulled_all_data(true);
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
