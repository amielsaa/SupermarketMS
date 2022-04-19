package BusinessLayer;

import java.util.Dictionary;
import java.util.HashMap;

public class QuantityAgrreement {
    private HashMap<Integer, Double> item_Num_To_Price;
    private HashMap<Integer,Integer> item_Num_To_Discount;
    private HashMap<Integer,String> item_Num_To_Name;


    public QuantityAgrreement(HashMap item_num_to_price, HashMap item_num_to_discount, HashMap item_num_to_name) {
        item_Num_To_Price = item_num_to_price;
        item_Num_To_Discount = item_num_to_discount;
        item_Num_To_Name = item_num_to_name;
    }

    public HashMap<Integer, Double> getItem_Num_To_Price() {
        return item_Num_To_Price;
    }

    public HashMap<Integer, Integer> getItem_Num_To_Discount() {
        return item_Num_To_Discount;
    }

    public HashMap<Integer, String> getItem_Num_To_Name() {
        return item_Num_To_Name;
    }

    public void setItem_Num_To_Discount(HashMap<Integer, Integer> item_Num_To_Discount) {
        this.item_Num_To_Discount = item_Num_To_Discount;
    }

    public void setItem_Num_To_Name(HashMap<Integer, String> item_Num_To_Name) {
        this.item_Num_To_Name = item_Num_To_Name;
    }

    public void setItem_Num_To_Price(HashMap<Integer, Double> item_Num_To_Price) {
        this.item_Num_To_Price = item_Num_To_Price;
    }

}

