package ServiceLayer;

import java.util.Scanner;

public class MainFacade {

    public MainFacade(){}

    public void main() {
        SupplierFacade fSupplier = new SupplierFacade();
        Boolean running = true;
        while(running){ //main program loop
            Scanner s = new Scanner(System.in);
            System.out.print("Enter command (0 - Exit, 1 - Add supplier): ");
            String input = s.nextLine();

            switch (input){
                case ("0"): {
                    running = false;
                    s.close();
                }
                case ("1"): {
                    createSupplier();
                }
            }

        }
        System.out.println("Goodbye, come again!");



    }

    private void createSupplier(){
        //todo: String name, int business_num, int bank_acc_num, String payment_details, Contact contact, QuantityAgreement quantity_agreement, boolean delivery_by_days, boolean self_delivery_or_pickup, Set<Integer> days_to_deliver
        Scanner s = new Scanner(System.in);
        System.out.print("Enter supplier name: ");
        String supplierName = s.nextLine();
        System.out.print("Enter supplier business number: ");
        String businessNumber = s.nextLine();
        System.out.print("Enter supplier bank account number: ");
        String bankNumber = s.nextLine();
        System.out.print("Enter supplier payment option (1 - Credit, 2 - Cash, 3 - Plus30, 4 - Plus60, 5 - Check): ");
        String paymentDetail = s.nextLine();
        paymentDetail = paymentDetailNumberToString(paymentDetail);




    }

    private String paymentDetailNumberToString(String input){
        switch (input){
            case("1"):return "credit";
            case("2"):return "cash";
            case("3"):return "plus30";
            case("4"):return "plus60";
            case("5"):return "check";
        }
        return "none";
    }
}
