package Presentation;

import Business.DeliveryService;

public class SiteCommandsHandler {
    private String[] COMMANDS = {"add_supplier"};
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
        if (userData[0].equals("add_supplier"))
            handleAddSupplier(userData);
    }
    private void handleAddSupplier(String[] userData)
    {
        if (userData.length == 5)
            service.addSupplier(userData[1], userData[2], userData[3], userData[4]);

    }
}
