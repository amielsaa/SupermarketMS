package Service;

import Business.SitesController;

public class DeliveryService {
    private SitesController sitesController;
    public DeliveryService(){
        sitesController = new SitesController();

        //deliveries and trucks controllers creation will be implemented here
    }

    public Response addSupplier(String address, int zone, String phoneNumber, String contactName) {
        try {
            sitesController.addSupplier(address, zone, phoneNumber, contactName);
            return Response.makeSuccess(0);
        }
        catch (Exception ex){
            return Response.makeFailure(ex.getMessage());
        }
    }
}
