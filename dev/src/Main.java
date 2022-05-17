import Suppliers.PresentationLayer.PresentationMain;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
//        System.out.println( (LocalDate.now().getDayOfWeek().getValue() + 1) % 7);

        PresentationMain suppliersMain = new PresentationMain();
        Scanner s = new Scanner(System.in);

        while(true){ //main program loop
            System.out.print("Suppliers - 1, Inventory - 2\n(else - exit)");
            String input = s.nextLine();
            s.close();
            switch (input){
                case ("1"): {
                    running = false;
                    s.close();
                    break;
                }
                case ("2"): {
                    createSupplier(s);
                    break;
                }
                default: {
                    System.out.println("SEE YA");
                    break;
                }





        suppliersMain.main();

/*

        //supplier 1!
        SupplierFacade supplierFacade=new SupplierFacade();
        HashMap<Pair<String,String>,Double> item_To_Price=new HashMap<>();
        Pair pair=new Pair("banana","tnuva");//QA
        item_To_Price.put(pair,(double)1);
        HashMap<Pair<String,String>,HashMap<Integer,Integer>> item_Num_To_Quantity_To_Discount=new HashMap<>();//Discounts
        item_Num_To_Quantity_To_Discount.put(pair,new HashMap<Integer,Integer>());
        item_Num_To_Quantity_To_Discount.get(pair).put(10,5);
        item_Num_To_Quantity_To_Discount.get(pair).put(100,10);
        item_Num_To_Quantity_To_Discount.get(pair).put(500,20);
        String name="ari";
        String phone="123";
        Set<Integer> a=new LinkedHashSet<>();
        a.add(1);
        //ADDING SUPPLIER WITH 1 ITEM <banana,tnuva>
//        supplierFacade.addSupplier(name,111111111,1,"check",a,name,phone,item_To_Price,item_Num_To_Quantity_To_Discount,true);

        //supplier 2!
    /*    HashMap<Pair<String,String>,Double> item_To_Price2=new HashMap<>();
        Pair pair2=new Pair("banana","tnuva");//QA
        item_To_Price2.put(pair2,(double)1);
        HashMap<Pair<String,String>,HashMap<Integer,Integer>> item_Num_To_Quantity_To_Discount2=new HashMap<>();//Discounts
        item_Num_To_Quantity_To_Discount2.put(pair2,new HashMap<Integer,Integer>());
        item_Num_To_Quantity_To_Discount2.get(pair2).put(10,5);
        item_Num_To_Quantity_To_Discount2.get(pair2).put(100,10);
        item_Num_To_Quantity_To_Discount2.get(pair2).put(500,30);
        String name2="ari2";
        String phone2="1232";
        Set<Integer> a2=new LinkedHashSet<>();
        a2.add(1);
        //ADDING SUPPLIER WITH 1 ITEM <banana,tnuva>
        Response<DSupplier> theResponse= supplierFacade.addSupplier(name,111111112,2,"check",a2,name2,phone2,item_To_Price2,item_Num_To_Quantity_To_Discount2,true);

        HashMap<Pair<String,String>,Integer> orderToSuppliers=new HashMap<>();
        orderToSuppliers.put(pair,700);
      //  orderToSuppliers.put(pair2,700);\
   //     Response<DRoutineOrder> routineOrder=supplierFacade.makeRoutineOrder(111111111,orderToSuppliers,a);
        Response orders=supplierFacade.getAllRoutineOrders();

/*
        Response b=supplierFacade.MakeOrderToSuppliers(orderToSuppliers);



        HashMap<Pair<String,String>,Integer> order=new HashMap<>();
        order.put(pair,500);
        supplierFacade.makeRoutineOrder(111111111,order,a);
        Response<DRoutineOrder> response=supplierFacade.deleteItemFromRoutineOrder(111111111,0,"banana","tnuva");
*/





    }
}
