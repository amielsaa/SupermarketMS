package EmployeeModule.PresentationLayer.CLI.Pages;

import EmployeeModule.ServiceLayer.Gateway;
import Utilities.Exceptions.CLIException;

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

    @Override
    public Map<String, ResponsePage<Boolean>> getOptionsMap()
    {
        return new LinkedHashMap<String, ResponsePage<Boolean>>() {{
            put("Logout", pgEmptyFalse);
            put("Manage Employees", pgEmployeesMenu);
            put("Manage Shifts", pgShiftsMenu);
            put("Manage Qualifications", pgQualificationsMenu);
        }};
    }

    @Override
    public String getTitle()
    {
        return "Main Menu";
    }
}
