package Inventory.DataAccessLayer.DAO;

import Inventory.BuisnessLayer.Objects.Category;
import Inventory.BuisnessLayer.Objects.Product;
import Inventory.BuisnessLayer.Objects.StoreProduct;
import Inventory.DataAccessLayer.DalController;
import Inventory.DataAccessLayer.Mappers.ProductMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductDAO extends DalController {

    private ProductMapper productMapper;

    public ProductDAO(String tableName) {
        super(tableName);
        productMapper = new ProductMapper();
    }


    public void DeleteProduct(String idColeName,int id) {
        productMapper.deleteProduct(id);
        this.Delete(idColeName,id);
    }
    //TODO: DELETE PRODUCTS
    public Product InsertProduct(int Id, String name, String producer, double buyingPrice, double sellingPrice, double discount, String discountExpDate, List<Category> categories, int minQuantity) {
        String sql = "INSERT INTO Products(id, name,producer," +
                "buyingprice, sellingprice, discount,discountDate," +
                "categories, minquantity) " +
                "VALUES(?,?,?,?,?,?,?,?,?)";

        try{

            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Id);
            pstmt.setString(2,name);
            pstmt.setString(3,producer);
            pstmt.setDouble(4, buyingPrice);
            pstmt.setDouble(5, sellingPrice);
            pstmt.setDouble(6, discount);
            pstmt.setString(7,discountExpDate);
            pstmt.setString(8,categoriesToString(categories));
            pstmt.setInt(9, minQuantity);


            pstmt.executeUpdate();

            return productMapper.addProduct(new Product(Id,name,producer,buyingPrice,sellingPrice,categories,minQuantity));
        } catch (SQLException e) {
            //System.out.println(e.getMessage());
            throw new IllegalArgumentException("Product insertion to database failed, please try again.");
        }
    }

    //public void UpdateCategory

    public Product SelectProductById(int productid) {
        Product pExists = productMapper.getProduct(productid);
        if(pExists!=null)
            return pExists;
        String sql = "SELECT * FROM Products WHERE id=?";
        try {
            Connection conn = this.makeConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,productid);
            ResultSet rs = pstmt.executeQuery();

            Product p = null;
            while (rs.next()) {
                p = new Product(rs.getInt("id"),rs.getString("name"),
                        rs.getString("producer"),rs.getDouble("buyingprice"),
                        rs.getDouble("sellingprice"),
                        stringToCategoryList(rs.getString("categories")),rs.getInt("minquantity"));
                p.setDiscount(rs.getInt("discount"),getDateByString(rs.getString("discountDate")));
            }
            return productMapper.addProduct(p);
        } catch (SQLException e) {
            //System.out.println(e.getMessage());
            throw new IllegalArgumentException("Products fetch failed.");
        }
    }

    public List<Product> SelectAll() {
        if(productMapper.isPulled_all_data())
            return productMapper.getProducts();
        String sql = "SELECT * FROM Products";
        List<Product> products = new ArrayList<>();
        try {
            Connection conn = this.makeConnection();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);

            // loop through the result set
            while (rs.next()) {
                Product p = new Product(rs.getInt("id"),rs.getString("name"),
                        rs.getString("producer"),rs.getDouble("buyingprice"),
                        rs.getDouble("sellingprice"),
                        stringToCategoryList(rs.getString("categories")),rs.getInt("minquantity"));
                p.setDiscount(rs.getInt("discount"),getDateByString(rs.getString("discountDate")));
                products.add(productMapper.addProduct(p));
            }
            productMapper.setPulled_all_data(true);
            return products;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new IllegalArgumentException("Products fetch failed.");
        }


    }

    private java.util.Date getDateByString(String expDate) {
        if(expDate.equals("Unknown"))
            return null;
        String[] res = expDate.split("/");
        if(res.length==3 && res[2].length()==4)
            return new Date(Integer.parseInt(res[2])-1900,Integer.parseInt(res[1])-1,Integer.parseInt(res[0]));
        throw new IllegalArgumentException("Date format isn't valid.");
    }


    private List<Category> stringToCategoryList(String categories) {
        String[] split = categories.split(",");
        List<Category> categoryList = new ArrayList<>();
        for(int i=0;i<split.length;i++){
            categoryList.add(new Category(split[i]));
        }
        return categoryList;
    }


    private String categoriesToString(List<Category> categories) {
        String acc = "";
        for(Category c : categories)
            acc = acc + "," +c.getCategoryName();
        return acc.substring(1);
    }

    public void UpdateMapper(Product product) {
        productMapper.addProduct(product);
    }


}
