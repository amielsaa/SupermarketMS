package Suppliers.BusinessLayer;

import misc.Days;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.HashSet;
import java.util.Set;

public class mainfortry {
    public static void main(String[] args){
        Set<Days> days=new HashSet<>();
        days.add(Days.sunday);
        days.add(Days.monday);
        Set<LocalDate> output;
        output=getDatesForDelivery(days);
        for (LocalDate i:output
             ) {
            System.out.println(i);
        }
    }

    public static Set<LocalDate> getDatesForDelivery(Set<Days> days) {
        //arranging the days
        Set<Integer> daysInNumber=new HashSet<Integer>();
        for (Days i:days) {
            daysInNumber.add(ReversedayConvertor(i));
        }
        Set<LocalDate> toReturn=new HashSet<LocalDate>();
        for (Integer i:daysInNumber) {
            LocalDate date = LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.of(((i)))));
            toReturn.add(date);
        }

        return toReturn;
    }
    private static int ReversedayConvertor(Days day){
        if(day==Days.sunday)
            return 7;
        else if(day==Days.monday)
            return 1;
        else if(day==Days.tuesday)
            return 2;
        else if(day==Days.wednesday)
            return 3;
        else if(day==Days.thursday)
            return 4;
        else if(day==Days.friday)
            return 5;
        else if(day==Days.saturday)
            return 6;
        else
            throw new IllegalArgumentException("day is not valid");

    }

}
