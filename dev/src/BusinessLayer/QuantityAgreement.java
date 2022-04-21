package BusinessLayer;


import misc.Pair;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;


public class QuantityAgreement {
    private HashMap<Integer, Double> item_Num_To_Price;
    private HashMap<Integer,HashMap<Integer,Integer>> item_Num_To_Quantity_To_Discount;
    private HashMap<Integer,String> item_Num_To_Name;


    public QuantityAgreement(HashMap item_num_to_price, HashMap item_num_to_discount, HashMap item_num_to_name) {
        item_Num_To_Price = item_num_to_price;
        item_Num_To_Quantity_To_Discount = item_num_to_discount;
        item_Num_To_Name = item_num_to_name;
    }

    public HashMap<Integer, Double> getItem_Num_To_Price() {
        return item_Num_To_Price;
    }

    public HashMap<Integer, HashMap<Integer, Integer>> getItem_Num_To_Discount() {
        return item_Num_To_Quantity_To_Discount;
    }

    public void setItem_Num_To_Discount(HashMap<Integer, HashMap<Integer, Integer>> item_Num_To_Discount) {
        this.item_Num_To_Quantity_To_Discount = item_Num_To_Discount;
    }

    public HashMap<Integer, String> getItem_Num_To_Name() {
        return item_Num_To_Name;
    }

    public void setItem_Num_To_Name(HashMap<Integer, String> item_Num_To_Name) {

        this.item_Num_To_Name = item_Num_To_Name;
    }

    public void setItem_Num_To_Price(HashMap<Integer, Double> item_Num_To_Price) {
        this.item_Num_To_Price = item_Num_To_Price;
    }
    public HashMap<Integer, Pair<String,Double>> makeOrder(HashMap<Integer,Integer> order){
        HashMap<Integer,Pair<String,Double>> fixedOrder= new HashMap<Integer, Pair<String, Double>>();// the fixed Order to return
        Integer[] orderKeys = new Integer[order.keySet().toArray().length];// array of the keys of the order
        for(int i=0; i<orderKeys.length;i++){
            orderKeys[i] = (Integer)order.keySet().toArray()[i];
        }


        for(int i=0;i<orderKeys.length;i++){
            CheckIfItemExists(orderKeys[i]);
            if(item_Num_To_Quantity_To_Discount.containsKey(orderKeys[i])){
                int currentDiscount=0;
                Integer[] quantities= new Integer[item_Num_To_Quantity_To_Discount.get(orderKeys[i]).keySet().toArray().length];
                for(int j=0; j<quantities.length;j++){
                    quantities[j] = (Integer)item_Num_To_Quantity_To_Discount.get(orderKeys[i]).keySet().toArray()[j];
                }
                for(int j=0;i<quantities.length;j++) {
                    if((quantities[j]<order.get(orderKeys[i]))&&(currentDiscount<item_Num_To_Quantity_To_Discount.get(orderKeys[i]).get(quantities[j])))
                        currentDiscount = item_Num_To_Quantity_To_Discount.get(orderKeys[i]).get(quantities[j]);

                }

                fixedOrder.put(orderKeys[i],new Pair(item_Num_To_Name.get(orderKeys[i]),item_Num_To_Price.get(orderKeys[i])*order.get(orderKeys[i])*((currentDiscount)/100)));
            }
            else{
                fixedOrder.put(orderKeys[i],new Pair(item_Num_To_Name.get(orderKeys[i]),item_Num_To_Price.get(orderKeys[i])*order.get(orderKeys[i])));
            }


        }
        return fixedOrder;
    }
    private void CheckIfItemExists(int itemNum){
        if(!item_Num_To_Name.containsKey(itemNum))
            throw new IllegalArgumentException("item number"+itemNum+"is not in the supplier catalog");
    }

}

