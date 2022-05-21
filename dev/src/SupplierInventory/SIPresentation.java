package SupplierInventory;

import Inventory.PresentationLayer.InventoryProgram;
import Suppliers.PresentationLayer.PresentationMain;

import java.util.Scanner;

public class SIPresentation {
    public void main() {
        SIService mixesService = new SIService();
        PresentationMain suppliersMain = new PresentationMain(mixesService);
        InventoryProgram inventoryMain = new InventoryProgram(mixesService);

        mixesService.deleteAllData();
        mixesService.DeleteAll();
        Scanner s = new Scanner(System.in);
        boolean running = true;

        while (running) { //main program loop
            System.out.print("Suppliers - 1, Inventory - 2\n(else - exit)\n");
            String input = s.nextLine();
            switch (input) {
                case ("1"): {
                    suppliersMain.main();
                    break;
                }
                case ("2"): {
                    inventoryMain.main();
                    break;
                }
                default: {
//                    System.out.println("SEE YA");
                    running = false;
                    s.close();
//                    break;
                }
            }


        }




    }
}
