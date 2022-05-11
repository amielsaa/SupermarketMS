package ServiceLayer.DummyObjects;

import BusinessLayer.QuantityAgreement;
import misc.Pair;

import java.util.HashMap;

public class DQuantityAgreement {


    private HashMap<Pair<String,String>, Double> item_Num_To_Price;
    private HashMap<Pair<String,String>,HashMap<Integer,Integer>> item_Num_To_Discount;



    public DQuantityAgreement(QuantityAgreement agreement) {
        item_Num_To_Price = new HashMap<Pair<String,String>, Double>();
        for(Pair x : agreement.getItem_To_Price().keySet()) {
            item_Num_To_Price.put(x, agreement.getItem_To_Price().get(x).doubleValue());
        }


        item_Num_To_Discount = new HashMap<Pair<String,String>,HashMap<Integer,Integer>>();
        for(Pair x : agreement.getItem_Num_To_Discount().keySet()) {
            item_Num_To_Discount.put(x, new HashMap<Integer,Integer>());
        }
        for(Pair x : agreement.getItem_Num_To_Discount().keySet()) {
            for(int y : agreement.getItem_Num_To_Discount().get(x).keySet()) {
                item_Num_To_Discount.get(x).put(y, agreement.getItem_Num_To_Discount().get(x).get(y));
            }
        }
        /*
        item_Num_To_Name = new HashMap<Integer,String>();
        for(int x : agreement.getItem_Num_To_Name().keySet()) {
            item_Num_To_Name.put(x, agreement.getItem_Num_To_Name().get(x));
        }

         */
    }

    public HashMap<Pair<String,String>, Double> getItem_Num_To_Price() {
        return item_Num_To_Price;
    }

    public HashMap<Pair<String,String>, HashMap<Integer, Integer>> getItem_Num_To_Discount() {
        return item_Num_To_Discount;
    }



    public String toString(){
        String ret = "\n";
        for(Pair x:getItem_Num_To_Price().keySet()){
            ret += ("Item Name: " + x.getFirst() + ", Item Producer: " + x.getSecond() + ", Item price: " + getItem_Num_To_Price().get(x)) + "\n";
        }
        return ret;
        //todo: add discount print?
    }
}
