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

    public RoutineOrder(int supplier_bn, int order_id, double final_price, String order_date, Set<Integer> days_To_Deliver) {
        super(supplier_bn, order_id, final_price, order_date);
        Days_To_Deliver = setDays_To_Deliver(days_To_Deliver);
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
}
