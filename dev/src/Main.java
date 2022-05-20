import Inventory.PresentationLayer.InventoryProgram;
import SupplierInventory.SIPresentation;
import Suppliers.DAL.ContactDAO;
import Suppliers.DAL.DalController;
import Suppliers.DAL.OrderDAO;
import Suppliers.PresentationLayer.PresentationMain;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        SIPresentation si = new SIPresentation();
        si.main();
        System.out.println("see ya");
//        ContactDAO a = new ContactDAO();
//        a.deleteAll();




    }
}
