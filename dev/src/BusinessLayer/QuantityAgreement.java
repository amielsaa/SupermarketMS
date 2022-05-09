package BusinessLayer;


import misc.Pair;

import java.util.HashMap;
import java.util.Set;


public class QuantityAgreement {
    private HashMap<Pair<String,String>, Double> item_To_Price;
    private HashMap<Pair<String,String>,HashMap<Integer,Integer>> item_Num_To_Quantity_To_Discount;


    //todo: remove item_Num_To_Name, change Integer to Pair<String,String>


    public QuantityAgreement(HashMap<Pair<String,String>,Double> item_num_to_price, HashMap item_num_to_discount, HashMap item_num_to_name) {
        //todo: check if there are no items at all
        PriceValidationChecker(item_num_to_price);
        item_To_Price = item_num_to_price;
        DiscountsValidationCheck(item_num_to_discount);
        item_Num_To_Quantity_To_Discount = item_num_to_discount;


    }

    public QuantityAgreement(){}//only for DAL constructor!


    public HashMap<Pair<String,String>, Double> getItem_To_Price() {
        return item_To_Price;
    }

    public HashMap<Pair<String,String>, HashMap<Integer, Integer>> getItem_Num_To_Discount() {
        return item_Num_To_Quantity_To_Discount;
    }

    public void setItem_Num_To_Discount(HashMap<Pair<String,String>, HashMap<Integer, Integer>> item_Num_To_Discount) {
        this.item_Num_To_Quantity_To_Discount = item_Num_To_Discount;
    }


    public void setItem_To_Price(HashMap<Pair<String,String>, Double> item_To_Price) {
        this.item_To_Price = item_To_Price;
    }
    public HashMap<Pair<String,String>, Pair<Double,Double>> makeOrder(HashMap<Pair<String,String>,Integer> order){
        HashMap<Pair<String,String>,Pair<Double,Double>> fixedOrder= new HashMap<Pair<String,String>, Pair<Double, Double>>();// the fixed Order to return
        Pair[] orderKeys = new Pair[order.keySet().toArray().length];// array of the keys of the order
        //first loop that goes all over the items in the order.
        for(int i=0; i<orderKeys.length;i++){
            orderKeys[i] = (Pair)order.keySet().toArray()[i];
            // checks if the quantity is a legal number
            if(!CheckLegalNumber(order.get(orderKeys[i]))){
                throw new IllegalArgumentException("quantity of the order cannot be negative number on item number "+orderKeys[i]);

            }
        }
        for(int i=0;i<orderKeys.length;i++){
            CheckIfItemExists(orderKeys[i]);
            if(item_Num_To_Quantity_To_Discount.containsKey(orderKeys[i])){
                int currentDiscount=0;
                Integer[] quantities= new Integer[item_Num_To_Quantity_To_Discount.get(orderKeys[i]).keySet().toArray().length];
                for(int j=0; j<quantities.length;j++){
                    quantities[j] = (Integer)item_Num_To_Quantity_To_Discount.get(orderKeys[i]).keySet().toArray()[j];
                }
                for(int j=0;j<quantities.length;j++) {
                    if((quantities[j]<=order.get(orderKeys[i]))&&(currentDiscount<item_Num_To_Quantity_To_Discount.get(orderKeys[i]).get(quantities[j])))
                        currentDiscount = item_Num_To_Quantity_To_Discount.get(orderKeys[i]).get(quantities[j]);

                }
                int actualDiscount=100-currentDiscount;
                Double pricePerOne= item_To_Price.get(orderKeys[i]);
                int quantity=order.get(orderKeys[i]);
                Double PriceWithoutDiscount=((pricePerOne)*(quantity));
                Double PriceWithDiscountAfterMath=((pricePerOne)*(quantity)*(actualDiscount)/100);
                fixedOrder.put(orderKeys[i],new Pair(PriceWithoutDiscount,PriceWithDiscountAfterMath));
            }
            else{
                fixedOrder.put(orderKeys[i],new Pair(item_To_Price.get(orderKeys[i])*order.get(orderKeys[i]), item_To_Price.get(orderKeys[i])*order.get(orderKeys[i])));
            }


        }
        return fixedOrder;
    }
    private void CheckIfItemExists(Pair itemName){
        if(!item_To_Price.containsKey(itemName))
            throw new IllegalArgumentException("item name "+itemName.getFirst()+"from the producer"+itemName.getSecond()+"is not in the supplier catalog");
    }
    private void PriceValidationChecker(HashMap<Pair<String,String>,Double> item_num_to_price){
        Set<Pair<String,String>> keys=item_num_to_price.keySet();
        //checks if the price is negative
        for(Pair i:keys){
            if (item_num_to_price.get(i)<0)
                throw new IllegalArgumentException("Negative price Detected on item "+ i);
            //check if the price are only numbers
            if(!CheckLegalNumberForDouble(item_num_to_price.get(i)))
                throw new IllegalArgumentException("Price should be a legal number");

        }
    }
    private void DiscountsValidationCheck(HashMap<Pair<String,String>,HashMap<Integer,Integer>> item_Num_To_Quantity_To_Discount){
        Set<Pair<String,String>> keys=item_Num_To_Quantity_To_Discount.keySet();

        for(Pair i:keys){
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
            if (numberToString.charAt(i)!='0'&&numberToString.charAt(i)!='1'&&numberToString.charAt(i)!='2'&&numberToString.charAt(i)!='3'&&numberToString.charAt(i)!='4'&&numberToString.charAt(i)!='5'&&numberToString.charAt(i)!='6'&&numberToString.charAt(i)!='7'&&numberToString.charAt(i)!='8'&&numberToString.charAt(i)!='9'){
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

