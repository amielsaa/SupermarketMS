package PresentationLayer;

import ServiceLayer.DeliveryService;

public class SiteCommandsHandler {
    private String[] COMMANDS =
            {"add_supplier", "add_branch", "edit_site_address", "edit_site_delivery_zone",
             "edit_site_contact_name", "edit_site_phone_number"};
    private DeliveryService service;
    public SiteCommandsHandler(DeliveryService service)
    {
        this.service = service;
    }
    public boolean isSiteCommand(String userCommand)
    {
        for (String command :COMMANDS)
            if (command.equals(userCommand))
                return true;
        return false;

    }
    public void executeCommand(String[] userData)
    {
        try {
            if (userData[0].equals("add_supplier"))
                handleAddSupplierWarehouse(userData);
            else if (userData[0].equals("add_branch"))
                handleAddBranch(userData);
            else if (userData[0].equals("edit_site_address"))
                handleEditSiteAddress(userData);
            else if (userData[0].equals("edit_site_delivery_zone"))
                handleEditSiteDeliveryZone(userData);
            else if (userData[0].equals("edit_site_contact_name"))
                handleEditSiteAddress(userData);
            else if (userData[0].equals("edit_site_phone_number"))
                handleEditSitePhoneNumber(userData);
        }
        catch (Exception ex){
            System.out.println(ex);
        }
    }

    private void handleAddSupplierWarehouse(String[] userData) throws Exception {
        if (userData.length == 5)
            service.addSupplierWarehouse(userData[1], parseInt(userData[2], "zone index"), userData[3], userData[4]);
        else
            printArgumentCountError("add_supplier", 4);
    }

    private void handleAddBranch(String[] userData) throws Exception {
        if (userData.length == 5)
            service.addSupplierWarehouse(userData[1], parseInt(userData[2], "zone index"), userData[3], userData[4]);
        else
            printArgumentCountError("add_branch", 4);
    }

    private void handleEditSiteAddress(String[] userData) throws Exception{
        if (userData.length == 3)
            service.editSiteAddress(parseInt(userData[1], "site id"), userData[2]);
        else
            printArgumentCountError("edit_site_address", 2);
    }

    private void handleEditSiteDeliveryZone(String[] userData) throws Exception{
        if (userData.length == 3)
            service.editSiteDeliveryZone(parseInt(userData[1], "site id"), parseInt(userData[2], "zone index"));
        else
            printArgumentCountError("edit_site_delivery_zone", 2);
    }

    private void handleEditSiteContactName(String[] userData) throws Exception{
        if (userData.length == 3)
            service.editSiteContactName(parseInt(userData[1], "site id"), userData[2]);
        else
            printArgumentCountError("edit_site_contact_name", 2);
    }

    private void handleEditSitePhoneNumber(String[] userData) throws Exception{
        if (userData.length == 3)
            service.editSitePhoneNumber(parseInt(userData[1], "site id"), userData[2]);
        else
            printArgumentCountError("edit_site_phone_number", 2);
    }

    private int parseInt(String data, String dataDescription) throws Exception
    {
        try{
            return Integer.parseInt(data);
        }
        catch (Exception e){
            throw new Exception("Illegal input: <" + dataDescription + "> is not a number");
        }
    }

    private void printArgumentCountError(String commandName, int count)
    {
        System.out.println(commandName + " command takes " + count + " arguments, different amount is given");
    }
}
