package ServiceLayer.DummyObjects;

import BusinessLayer.QuantityAgreement;

import java.util.HashMap;

public class DQuantityAgreement {

    private HashMap<Integer, Double> item_Num_To_Price;
    private HashMap<Integer,HashMap<Integer,Integer>> item_Num_To_Discount;
    private HashMap<Integer,String> item_Num_To_Name;


    public DQuantityAgreement(QuantityAgreement agreement) {
        item_Num_To_Price = new HashMap<Integer, Double>();
        agreement.getItem_Num_To_Price().forEach((k, v) -> {
            item_Num_To_Price.put(k,v);
        });

        item_Num_To_Discount = new HashMap<Integer,HashMap<Integer,Integer>>();
        for(int x : agreement.getItem_Num_To_Discount().keySet()) {
            item_Num_To_Discount.put(x, new HashMap<Integer,Integer>());
        }
        agreement.getItem_Num_To_Discount().forEach((k, v) -> {
            v.forEach((k1, v1) -> {
                agreement.getItem_Num_To_Discount().get(k).put(k1,v1);
            });
        });

        item_Num_To_Name = new HashMap<Integer,String>();
        agreement.getItem_Num_To_Name().forEach((k, v) -> {
            item_Num_To_Name.put(k,v);
        });
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
}
