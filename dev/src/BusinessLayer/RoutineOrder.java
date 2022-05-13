package BusinessLayer;

import misc.Days;
import misc.Pair;

import java.sql.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

public class RoutineOrder extends Order{
    private Set<Days> Days_To_Deliver;


    public RoutineOrder(int supplier_bn, int order_id, HashMap<Pair<String,String>, OrderItem> item_num_to_orderItem,double priceBeforeDiscount, double final_price, Date order_date, Set<Integer> days_To_Deliver) {
        super(supplier_bn, order_id, item_num_to_orderItem,priceBeforeDiscount, final_price, order_date);
        Days_To_Deliver = setDays_To_Deliver(days_To_Deliver);
    }


    public RoutineOrder(Order o, Set<Integer> days) {
        super(o.getSupplier_BN(),o.getOrder_Id(),o.getItem_Num_To_OrderItem(),o.getPriceBeforeDiscount(),o.getFinal_Price(),o.getOrder_Date());
        Days_To_Deliver = setDays_To_Deliver(days);
    }

    public Set<Days> setDays_To_Deliver(Set<Integer> days_To_Deliver) {
        Integer[] days = new Integer[days_To_Deliver.toArray().length];//gets the set to Array
        Set<Days> daysSet=new LinkedHashSet<Days>();
        for(int i=0; i<days.length;i++){
            days[i] = (Integer)days_To_Deliver.toArray()[i];
        }
        for (int i:days){
            daysSet.add(dayConvertor(i));
        }
        return daysSet;
    }

    private Days dayConvertor(int day){
        if(day==1)
            return Days.sunday;
        else if(day==2)
            return Days.monday;
        else if(day==3)
            return Days.tuesday;
        else if(day==4)
            return Days.wednesday;
        else if(day==5)
            return Days.thursday;
        else if(day==6)
            return Days.friday;
        else if(day==7)
            return Days.saturday;
        else
            throw new IllegalArgumentException("day is not valid");

    }

    public Set<Days> getDays_To_Deliver() {
        return Days_To_Deliver;
    }


    public boolean addOrUpdateRoutineOrder(Pair<String,String> i, Double PriceBeforeDiscount, Double FinalPrice,int Quantity) {
        boolean updateOrAdd=false;//if false-update if true-add
        if(getItem_Num_To_OrderItem().containsKey(i)){
            OrderItem orderItem=getItem_Num_To_OrderItem().get(i);
            double OldPriceBeforeDiscount=orderItem.getItem_Original_Price();
            double OldPriceAfterDiscount=orderItem.getItem_Price();
            orderItem.setItem_Original_Price(PriceBeforeDiscount);
            orderItem.setItem_Price(FinalPrice);
            orderItem.setItem_Amount(Quantity);
            System.out.println(getPriceBeforeDiscount());
            System.out.println(OldPriceBeforeDiscount);
            System.out.println(PriceBeforeDiscount);
            setPriceBeforeDiscount(getPriceBeforeDiscount()-OldPriceBeforeDiscount+PriceBeforeDiscount);
            setFinal_Price(getFinal_Price()-OldPriceAfterDiscount+FinalPrice);
            return updateOrAdd;
        }
        else{
            getItem_Num_To_OrderItem().put(i,new OrderItem(getOrder_Id(),i.getFirst(),i.getSecond(),PriceBeforeDiscount,FinalPrice,Quantity));
            setPriceBeforeDiscount(getPriceBeforeDiscount()+PriceBeforeDiscount);
            setFinal_Price(getFinal_Price()+FinalPrice);
            updateOrAdd=true;
            return updateOrAdd;
        }
    }

    public boolean deleteItemFromRoutineOrder(String itemName, String itemProducer) {
        Pair<String,String> key=new Pair<String,String>(itemName,itemProducer);
        if(!getItem_Num_To_OrderItem().containsKey(key))
            return false;
        else{
            OrderItem toremove=getItem_Num_To_OrderItem().remove(key);
            setPriceBeforeDiscount(getPriceBeforeDiscount()-toremove.getItem_Original_Price());
            setFinal_Price(getFinal_Price()-toremove.getItem_Price());
            return true;
        }
    }
}
