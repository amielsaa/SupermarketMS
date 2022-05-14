package Inventory.ServiceLayer.Objects.PresentationLayer;

import Inventory.ServiceLayer.Response;

import java.util.Scanner;

public class StoreSelection {

    private int storeId;
    private String stores;
    Scanner sc;

    public StoreSelection(String stores) {
        this.storeId = 0;
        this.stores = "Choose store: "+stores;
        this.sc = new Scanner(System.in);
    }

    public int initStore() {
        printStores(stores);
        return storeIdInput();
    }

    public void handleResponse(Response<Integer> res) {
        if(res.isSuccess())
            System.out.println();
    }

    private void printStores(String stores) {
        System.out.println(stores);
    }

    private int storeIdInput() {
        return sc.nextInt();
    }


}
