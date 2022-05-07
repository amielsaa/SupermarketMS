package BusinessLayer;

import misc.Days;
import misc.Pair;
import misc.PaymentDetails;

import javax.print.attribute.SetOfIntegerSyntax;
import java.util.*;


public class Supplier  {
    private String Name;
    private int Business_Num;
    private int Bank_Acc_Num;
    private PaymentDetails Payment_Details;
    private List<Contact> Contacts;
    private QuantityAgreement Quantity_Agreement;
    private boolean Delivery_By_Days; //if the supplier Delivers by days or not
    private boolean Self_Delivery_Or_Pickup;// if we need to pick-up or he delivers us
    private Set<Days> Days_To_Deliver;



    public Supplier(String name, int business_num, int bank_acc_num, String payment_details, String contactName, String contactPhone, HashMap item_num_to_price, HashMap item_num_to_discount, HashMap item_num_to_name, boolean self_delivery_or_pickup, Set<Integer> days_to_deliver) {
        Name = name;
        CheckLegalNumber(business_num);
        CheckLengthOfBusinessNumber(business_num);
        Business_Num = business_num;
        CheckLegalNumber(bank_acc_num);
        Bank_Acc_Num = bank_acc_num;
        Payment_Details =setPayment_Details(payment_details);
        Contacts = new LinkedList<Contact>();
        Contacts.add(new Contact(contactName, contactPhone));
        Delivery_By_Days = false;
        Self_Delivery_Or_Pickup = self_delivery_or_pickup;
        Quantity_Agreement = new QuantityAgreement(item_num_to_price, item_num_to_discount, item_num_to_name);
        if(!Delivery_By_Days)
            Days_To_Deliver = new LinkedHashSet<Days>();
        else
            Days_To_Deliver=setDays_To_Deliver(days_to_deliver);
    }

    public void setName(String name) {
        Name = name;
    }

    public String getName() {
        return Name;
    }

    public boolean isDelivery_By_Days() {
        return Delivery_By_Days;
    }

    public boolean isSelf_Delivery_Or_Pickup() {
        return Self_Delivery_Or_Pickup;
    }

    public int getBank_Acc_Num() {
        return Bank_Acc_Num;
    }

    public int getBusiness_Num() {
        return Business_Num;
    }

    public List<Contact> getContacts() {
        return Contacts;
    }

    public PaymentDetails getPayment_Details() {
        return Payment_Details;
    }

    public QuantityAgreement getQuantity_Agreement() {
        return Quantity_Agreement;
    }

    public Set<Days> getDays_To_Deliver() {
        return Days_To_Deliver;
    }

    public void setBank_Acc_Num(int bank_Acc_Num) {
        Bank_Acc_Num = bank_Acc_Num;
    }

    public void setBusiness_Num(int business_Num) {
        Business_Num = business_Num;
    }

    public void setContacts(List<Contact> contacts) {
        Contacts = contacts;
    }

    public Set<Days> setDays_To_Deliver(Set<Integer> days_To_Deliver) {
        Integer[] days = new Integer[days_To_Deliver.toArray().length];//gets the set to Array
        Set<Days> daysSet=new LinkedHashSet<Days>();
        for(int i=0; i<days.length;i++){
            days[i] = (Integer)days_To_Deliver.toArray()[i];
        }
        for (int i:days){
            daysSet.add(dayCovertor(i));
        }


        return daysSet;
    }

    public void setDelivery_By_Days(boolean delivery_By_Days) {
        Delivery_By_Days = delivery_By_Days;
    }

    public PaymentDetails setPayment_Details(String payment_Details) {
        if(payment_Details.equals("credit"))
            return PaymentDetails.credit;
        else if(payment_Details.equals("cash"))
            return PaymentDetails.cash;
        else if(payment_Details.equals("plus30"))
            return PaymentDetails.plus30;
        else if(payment_Details.equals("plus60"))
            return PaymentDetails.plus60;
        else if(payment_Details.equals("check"))
            return PaymentDetails.check;
        else
            throw new IllegalArgumentException("Payment Method isnt valid");


    }

    public void setQuantity_Agreement(QuantityAgreement quantity_Agreement) {
        Quantity_Agreement = quantity_Agreement;
    }

    public void setSelf_Delivery_Or_Pickup(boolean self_Delivery_Or_Pickup) {
        Self_Delivery_Or_Pickup = self_Delivery_Or_Pickup;
    }

    public void addSupplierContact(String name, String contactPhone) {
        Contacts.add(new Contact(name,contactPhone));
    }

    public void removeSupplierContact(String contactPhone) {
        int beginningSize=Contacts.size();
        for (int i=0;i<Contacts.size();i++){
            if(Contacts.get(i).getPhone_Num().equals(contactPhone))
                Contacts.remove(i);
        }
        if(Contacts.size()==beginningSize)
            throw new IllegalArgumentException("Contact could no be found");
    }
    public HashMap<Integer, Pair<String,Double>> makeOrder(HashMap<Integer,Integer> order){
        return Quantity_Agreement.makeOrder(order);

    }
    private void CheckLegalNumber(Integer number){
        if(number<0){
            throw new IllegalArgumentException("cannot accept negative numbers");
        }
        String numberToString=number.toString();
        for (int i=0;i<numberToString.length();i++){
            if ((numberToString.charAt(i)!='0'&&numberToString.charAt(i)!='1'&&numberToString.charAt(i)!='2'&&numberToString.charAt(i)!='3'&&numberToString.charAt(i)!='4'&&numberToString.charAt(i)!='5'&&numberToString.charAt(i)!='6'&&numberToString.charAt(i)!='7'&&numberToString.charAt(i)!='8'&&numberToString.charAt(i)!='9')){
                throw new IllegalArgumentException("cannot give Business number or bank account input other then numbers");
            }
        }
    }
    private void CheckLengthOfBusinessNumber(Integer number){
        String numberToString=number.toString();
        if(numberToString.length()!=9)
            throw new IllegalArgumentException("Business number should be 9 digits");
    }
    public void addSupplierDeliveryDay(int day){
        if(!Delivery_By_Days)
            Delivery_By_Days=true;
        Days dayToAdd=dayCovertor(day);
        if (Days_To_Deliver.contains(dayToAdd))
            throw new IllegalArgumentException("this Day is already in the Delivery days");
        Days_To_Deliver.add(dayToAdd);


    }
    public void removeSupplierDeliveryDay(int day){
        Days dayToRemove=dayCovertor(day);
        if(!Days_To_Deliver.contains(dayToRemove))
            throw new IllegalArgumentException("cannot delete day-day isn't in the delivery days");
        Days_To_Deliver.remove(dayToRemove);

    }
    private Days dayCovertor(int day){
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
    public void  updateSupplierBankAccount(int bank_acc){
        CheckLegalNumber(bank_acc);
        setBank_Acc_Num(bank_acc);
    }
    public void updateContactPhoneNumber(String oldPhoneNum,String newPhoneNum){
        boolean found=false;
        for(int i=0;i<Contacts.size()&&!found;i++){
            if(Contacts.get(i).getPhone_Num().equals(oldPhoneNum)){
                Contacts.get(i).setPhone_Num(newPhoneNum);
            }
        }
        if (!found){
            throw new IllegalArgumentException("contact was not found");
        }
    }



}
