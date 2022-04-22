package ServiceLayer.DummyObjects;

import BusinessLayer.QuantityAgreement;

import java.util.HashMap;

public class DQuantityAgreement {

    private HashMap<Integer, Double> item_Num_To_Price;
    private HashMap<Integer,HashMap<Integer,Integer>> item_Num_To_Discount;
    private HashMap<Integer,String> item_Num_To_Name;


    public DQuantityAgreement(QuantityAgreement agreement) {
        item_Num_To_Price = new HashMap<Integer, Double>();
        for(int x : agreement.getItem_Num_To_Price().keySet()) {
            item_Num_To_Price.put(x, agreement.getItem_Num_To_Price().get(x).doubleValue());
        }


        item_Num_To_Discount = new HashMap<Integer,HashMap<Integer,Integer>>();
        for(int x : agreement.getItem_Num_To_Discount().keySet()) {
            item_Num_To_Discount.put(x, new HashMap<Integer,Integer>());
        }
        for(int x : agreement.getItem_Num_To_Discount().keySet()) {
            for(int y : agreement.getItem_Num_To_Discount().get(x).keySet()) {
                item_Num_To_Discount.get(x).put(y, agreement.getItem_Num_To_Discount().get(x).get(y));
            }
        }

        item_Num_To_Name = new HashMap<Integer,String>();
        for(int x : agreement.getItem_Num_To_Name().keySet()) {
            item_Num_To_Name.put(x, agreement.getItem_Num_To_Name().get(x));
        }
    }

    public HashMap<Integer, Double> getItem_Num_To_Price() {
        return item_Num_To_Price;
    }

    public HashMap<Integer, HashMap<Integer, Integer>> getItem_Num_To_Discount() {
        return item_Num_To_Discount;
    }

    public HashMap<Integer, String> getItem_Num_To_Name() {
        return item_Num_To_Name;
    }

    public String toString(){
        String ret = "\n";
        for(int x:getItem_Num_To_Name().keySet()){
            ret += ("Item ID: " + x + ", Item name: " + getItem_Num_To_Name().get(x) + ", Item price: " + getItem_Num_To_Price().get(x)) + "\n";
        }
        return ret;
        //todo: add discount print?
    }
}
