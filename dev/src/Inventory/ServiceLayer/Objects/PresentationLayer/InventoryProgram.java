package Inventory.ServiceLayer.Objects.PresentationLayer;

import Inventory.ServiceLayer.Service;

public class InventoryProgram {
    public static void main(String[] args) {
        // write your code here
        Service service = new Service();
        Menu menu = new Menu(service);
        StoreSelection ss = new StoreSelection("1-BSStore");
        while(!service.SelectStore(ss.initStore()).isSuccess());
        menu.mainLoop();


    }

}

