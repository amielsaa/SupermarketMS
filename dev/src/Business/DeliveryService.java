package Business;

import Business.Controllers.SitesController;

public class DeliveryService {
    private SitesController sitesController;
    public DeliveryService(){
        sitesController = new SitesController();

        //deliveries and trucks controllers creation will be implemented here
    }

    public void addSupplier(String address, String zone, String phoneNumber, String contactName) {
        sitesController.addSupplier(address, zone, phoneNumber, contactName);
    }
}
