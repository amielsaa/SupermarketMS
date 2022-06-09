package Employee.PresentationLayer.CLI.Pages;

import Delivery.PresentationLayer.UserTerminal;
import Employee.ServiceLayer.Gateway;
import SupplierInventory.SIPresentation;
import Utilities.Exceptions.CLIException;
import Utilities.ResponsePage;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class MenuPage extends OptionsMenuPage
{
    ResponsePage<Boolean> pgEmployeesMenu = new EmployeesMenuPage();
    ResponsePage<Boolean> pgShiftsMenu = new ShiftsMenuPage();
    ResponsePage<Boolean> pgQualificationsMenu = new QualificationsMenuPage();
    ResponsePage<Boolean> pgEmptyFalse = new ResponsePage<Boolean>()
    {
        @Override
        public Boolean runWithResponse(Scanner input, Gateway g) throws CLIException
        {
            return false;
        }
        @Override
        public void run(Scanner input, Gateway g) throws CLIException
        {
            runWithResponse(input, g);
        }
    };

    UserTerminal deliverySystemTerminal = new UserTerminal();
    SIPresentation si = new SIPresentation();
    @Override
    public Map<String, ResponsePage<Boolean>> getOptionsMap()
    {
        return new LinkedHashMap<String, ResponsePage<Boolean>>() {{
            put("Logout", pgEmptyFalse);
            put("Manage Employees", pgEmployeesMenu);
            put("Manage Shifts", pgShiftsMenu);
            put("Manage Qualifications", pgQualificationsMenu);
            put("Open Delivery System", deliverySystemTerminal);
            put("Open Inventory System", si);
        }};
    }

    @Override
    public String getTitle()
    {
        return "Main Menu";
    }
}
