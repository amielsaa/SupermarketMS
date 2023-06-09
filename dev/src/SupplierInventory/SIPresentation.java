package SupplierInventory;

import Employee.ServiceLayer.Gateway;
import Inventory.PresentationLayer.InventoryProgram;
import Suppliers.PresentationLayer.PresentationMain;
import Utilities.Exceptions.CLIException;
import Utilities.Response;
import Utilities.ResponsePage;

import java.util.Scanner;

public class SIPresentation extends ResponsePage<Boolean>
{
    public Boolean runWithResponse(Scanner input, Gateway g) throws CLIException {
        Response<SIService> r1 = g.getSIService();
        if(!r1.isSuccess()) {
            System.out.println("Failed to get SI service. " + r1.getMessage());
            return true;
        }
        SIService mixesService = r1.getData();
        PresentationMain suppliersMain = new PresentationMain(mixesService);
        InventoryProgram inventoryMain = new InventoryProgram(mixesService);

        boolean running = true;

        while (running) { //main program loop
            System.out.print("Suppliers - 1, Inventory - 2\n(else - exit)\n");
            String inputString = input.nextLine();
            switch (inputString) {
                case ("1"): {
                    suppliersMain.main(input);
                    break;
                }
                case ("2"): {
                    inventoryMain.main(input);
                    break;
                }
                default: {
//                    System.out.println("SEE YA");
                    running = false;
                    //s.close(); Not Needed because of the new Gateway integration
//                    break;
                }
            }


        }
        // tells the parent menu it shouldn't quit after this menu.  
        return true;
    }
}
