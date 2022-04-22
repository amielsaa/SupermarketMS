package BusinessLayer;


import misc.Pair;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;


public class QuantityAgreement {
    private HashMap<Integer, Double> item_Num_To_Price;
    private HashMap<Integer,HashMap<Integer,Integer>> item_Num_To_Quantity_To_Discount;
    private HashMap<Integer,String> item_Num_To_Name;


    public QuantityAgreement(HashMap<Integer,Double> item_num_to_price, HashMap item_num_to_discount, HashMap item_num_to_name) {
        PriceValidationChecker(item_num_to_price);
        item_Num_To_Price = item_num_to_price;
        DiscountsValidationCheck(item_num_to_discount);
        item_Num_To_Quantity_To_Discount = item_num_to_discount;
        item_Num_To_Name = item_num_to_name;
        if(item_num_to_price.keySet().size()!=item_num_to_name.keySet().size())
            throw new IllegalArgumentException("The size list of Products Names should match the size of the list of Products price");
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
        if(!item_Num_To_Price.containsKey(itemNum))
            throw new IllegalArgumentException("item number"+itemNum+"is not in the supplier catalog");
    }
    private void PriceValidationChecker(HashMap<Integer,Double> item_num_to_price){
        Set<Integer> keys=item_num_to_price.keySet();
        //checks if the price is negative
        for(Integer i:keys){
            if (item_num_to_price.get(i)<0)
                throw new IllegalArgumentException("Negative price Detected on item "+ i);
            //check if the price are only numbers
            if(!CheckLegalNumberForDouble(item_num_to_price.get(i)))
                throw new IllegalArgumentException("Price should be a legal number");

        }
    }
    private void DiscountsValidationCheck(HashMap<Integer,HashMap<Integer,Integer>> item_Num_To_Quantity_To_Discount){
        Set<Integer> keys=item_Num_To_Quantity_To_Discount.keySet();

        for(Integer i:keys){
            //check if the item is in the catalog (can't give discounts for items we don't offer)
         CheckIfItemExists(i);
            //check if the discount is Valid
            Set<Integer> keysOfQuantities=item_Num_To_Quantity_To_Discount.get(i).keySet();
            for(Integer j:keysOfQuantities){
                if(!CheckLegalNumber(j)) {
                    throw new IllegalArgumentException("Quantity should be a legal number");
                }
                    if(!CheckLegalNumber(item_Num_To_Quantity_To_Discount.get(i).get(j))||item_Num_To_Quantity_To_Discount.get(i).get(j)>100)
                        throw new IllegalArgumentException("Discount should be a legal number between 0 and 100");


            }
        }



    }
    private boolean CheckLegalNumber(Integer number){
        if(number<0){
           return false;
        }
        String numberToString=number.toString();
        for (int i=0;i<numberToString.length();i++){
            if (numberToString.charAt(i)!='0'||numberToString.charAt(i)!='1'||numberToString.charAt(i)!='2'||numberToString.charAt(i)!='3'||numberToString.charAt(i)!='4'||numberToString.charAt(i)!='5'||numberToString.charAt(i)!='6'||numberToString.charAt(i)!='7'||numberToString.charAt(i)!='8'||numberToString.charAt(i)!='9'){
                return false;
            }
        }
        return true;
    }
    private boolean CheckLegalNumberForDouble(Double number) {
        if (number < 0) {
            return false;
        }
        String numberToString = number.toString();
        boolean DecimalPoint = false;
        for (int i = 0; i < numberToString.length(); i++) {
            if (numberToString.charAt(i) != '.' && numberToString.charAt(i) != '0' && numberToString.charAt(i) != '1' && numberToString.charAt(i) != '2' && numberToString.charAt(i) != '3' && numberToString.charAt(i) != '4' && numberToString.charAt(i) != '5' && numberToString.charAt(i) != '6' && numberToString.charAt(i) != '7' && numberToString.charAt(i) != '8' && numberToString.charAt(i) != '9') {
                return false;
            }
            if (numberToString.charAt(i) == '.' && DecimalPoint == false) {
                DecimalPoint = true;
            } else if (numberToString.charAt(i) == '.' && DecimalPoint == true)
                return false;

        }
        return true;
    }

}

