package Inventory.PresentationLayer;

import Inventory.ServiceLayer.Service;
import SupplierInventory.SIService;

public class InventoryProgram {
    SIService sis;

    public InventoryProgram(SIService sis) {
        this.sis = sis;
    }

    public void main() {
        // write your code here
        Service service = new Service();
        Menu menu = new Menu(service);
        StoreSelection ss = new StoreSelection("1-BSStore");
        while(!service.SelectStore(ss.initStore()).isSuccess());
        menu.mainLoop();


    }

}

