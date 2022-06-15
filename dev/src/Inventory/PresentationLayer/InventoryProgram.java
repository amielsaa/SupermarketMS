package Inventory.PresentationLayer;

import SupplierInventory.SIService;

import java.util.Scanner;

public class InventoryProgram {
    SIService sis;

    public InventoryProgram(SIService sis) {
        this.sis = sis;
    }

    public void main(Scanner input) {
        Menu menu = new Menu(sis, input);
        StoreSelection ss = new StoreSelection("1-BSStore");
        while(!sis.SelectStore(ss.initStore()).isSuccess());
        //sis.LoadProducts();
        menu.mainLoop();


    }

}

