package EmployeeModule.PresentationLayer.CLI.Pages;

import EmployeeModule.ServiceLayer.Gateway;
import Utilities.Exceptions.CLIException;
import Utilities.ResponsePage;

import java.util.Map;
import java.util.Scanner;

import static Utilities.CLIUtil.PrettyPrint.interactWithOptions;

public abstract class OptionsMenuPage extends ResponsePage<Boolean>
{
    @Override
    public Boolean runWithResponse(Scanner input, Gateway g) throws CLIException
    {
        Map<String, ResponsePage<Boolean>> optionsMap = this.getOptionsMap();
        String[] options = optionsMap.keySet().toArray(new String[optionsMap.size()]);
        String chosenOption = interactWithOptions(input, this.getTitle(), options);

        if(optionsMap.containsKey(chosenOption)) {
            ResponsePage<Boolean> page = optionsMap.get(chosenOption);
            return page.runWithResponse(input , g);
        }
        else {
            throw new CLIException("Not supported case. ");
        }
    }
    // The pair indicates if the option should retrigger the previous page
    public abstract Map<String, ResponsePage<Boolean>> getOptionsMap();
    public abstract String getTitle();
}
