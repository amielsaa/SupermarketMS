package Inventory.PresentationLayer;

import Inventory.ServiceLayer.Service;
import SupplierInventory.SIService;

public class InventoryProgram {
    SIService sis;

    public InventoryProgram(SIService sis) {
        this.sis = sis;
    }

    public void main() {
        Menu menu = new Menu(sis);
        StoreSelection ss = new StoreSelection("1-BSStore");
        while(!sis.SelectStore(ss.initStore()).isSuccess());
        //sis.LoadProducts();
        menu.mainLoop();


    }

}

