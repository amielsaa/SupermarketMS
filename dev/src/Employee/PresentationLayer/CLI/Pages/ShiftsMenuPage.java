package Employee.PresentationLayer.CLI.Pages;

import Employee.BusinessLayer.*;
import Employee.BusinessLayer.*;
import Employee.ServiceLayer.Gateway;
import Utilities.Exceptions.CLIException;
import Utilities.Pair;
import Utilities.CLIUtil.PrettyTable;
import Utilities.Response;
import Utilities.ResponsePage;

import java.util.*;

import static Utilities.CLIUtil.PrettyInput.*;
import static Utilities.CLIUtil.PrettyPrint.*;

public class ShiftsMenuPage extends OptionsMenuPage
{
    ResponsePage<Boolean> pgEmptyFalse = new ReturnPage();
    ResponsePage<Boolean> pgAddShift;
    ResponsePage<Boolean> pgEditShift = new EditShiftPage();

    private int branchId;

    @Override
    public Boolean runWithResponse(Scanner input, Gateway g) throws CLIException
    {
        this.branchId = printAndWaitForLegalInt(input, "Enter branch id: ", (x) -> (x > 0), "Illegal branch id. ");
        return this.runWithResponse(input, g, this.branchId);
    }

    public Boolean runWithResponse(Scanner input, Gateway g, int branchId) throws CLIException {
        this.pgAddShift = new AddShiftPage(branchId);

        Response<List<Shift>> res_shifts = g.getShifts(branchId);
        if(!res_shifts.isSuccess()){
            System.out.println(makeErrorMessage(res_shifts.getMessage()));
            return true;
        }
        else {
            List<Shift> shifts = res_shifts.getData();
            System.out.println(makeBigTitle("Shifts for Branch #" + branchId));
            PrettyTable table = new PrettyTable("Date", "Time", "Manager Id", "Manager Name", "Workers");
            for(Shift s : shifts) {
                Map<Integer, List<String>> workers = s.getWorkers();
                String workersString = "";
                for (Integer eId : workers.keySet())
                {
                    Employee e = g.getEmployee(eId).getData();
                    workersString += e.getName() + ": [";
                    int i = 0;
                    List<String> qs = workers.get(e.getId());
                    for(String qName : qs) {
                        Qualification q = g.getQualification(qName).getData();
                        workersString += q.getName();
                        if(i < qs.size() - 1)
                        {
                            workersString += ", ";
                        }
                        i++;
                    }
                    workersString += "], ";
                }
                table.insert(s.getId().getDate().toLocalDate().toString(), s.getId().getShiftTime() == ShiftTime.DAY ? "DAY" : "NIGHT", Integer.toString(s.getShiftManager()), g.getEmployee(s.getShiftManager()).getData().getName(), workersString);
            }
            System.out.println(table.toString());
        }

        if(super.runWithResponse(input, g)) {
            runWithResponse(input, g, this.branchId);
        }
        return true;

//        Choose from the menu:\n" +
//        "1.Add shift\n" +
//                "2.Remove shift\n" +
//                "3.Add worker\n" +
//                "4.Remove worker\n" +
//                "6.Return to main menu");
    }

    @Override
    public Map<String, ResponsePage<Boolean>> getOptionsMap()
    {
        return new LinkedHashMap<String, ResponsePage<Boolean>>() {{
            put("Return", pgEmptyFalse);
            put("Add Shift", pgAddShift);
            put("Remove Shift", makeResponsePage((Pair<Scanner, Gateway> args) -> {
                ShiftId s_id = printAndWaitForLegalShiftId(args.getKey(), makeTitle("Enter shift information") + "\n");
                Response<Shift> res_rem_emp = args.getValue().removeShift(s_id);
                if(!res_rem_emp.isSuccess()){
                    System.out.println(makeErrorMessage(res_rem_emp.getMessage()));
                }
                else {
                    System.out.println(makeSuccessMessage());
                }
                return true;
            }));
            put("Add Worker to Shift", makeResponsePage((Pair<Scanner, Gateway> args) -> {
                try
                {
                    ShiftId shiftId = printAndWaitForLegalShiftId(args.getKey(), makeTitle("Enter shift information") + "\n");
                    int e_id = printAndWaitForLegalInt(args.getKey(), "Please provide the id of an employee: ");
                    Response<Employee> get_employee_res = args.getValue().getEmployee(e_id);
                    if (!get_employee_res.isSuccess())
                    {
                        throw new CLIException(get_employee_res.getMessage());
                    }
                    Employee employee = get_employee_res.getData();
                    ArrayList<String> qualifications_list = new ArrayList<String>();
                    List<String> e_qualifications_list = employee.getWorkingConditions().getQualifications();
                    int user_input = -2;
                    int counter = 0;
                    boolean done = false;
                    PrettyTable table = new PrettyTable("Index", "Qualification");
                    for (String qualificationName : e_qualifications_list)
                    {
                        table.insert(Integer.toString(counter), qualificationName);
                        counter++;
                    }
                    System.out.println(makeTitle("Please choose the qualifications indexes to add from the list above; to return insert -1: "));
                    System.out.println(table);
                    while (!done)
                    {
                        user_input = printAndWaitForLegalInt(args.getKey(), "Input> ", (x) -> (x < e_qualifications_list.size() && x >= -1), "Illegal input. ");
                        if (user_input == -1)
                        {
                            done = true;
                        }
                        else
                        {
                            qualifications_list.add(e_qualifications_list.get(user_input));
                        }
                    }
                    System.out.println(e_qualifications_list);
                    Response<Employee> add_worker_res = args.getValue().addWorker(shiftId, get_employee_res.getData(), qualifications_list);
                    if (!add_worker_res.isSuccess())
                    {
                        throw new CLIException(add_worker_res.getMessage());
                    }
                    System.out.println(makeSuccessMessage());
                } catch (Exception e) {
                    System.out.println(makeErrorMessage(e.getMessage()));
                } finally {
                    return true;
                }
            }));
            put("Remove Worker from Shift", makeResponsePage((Pair<Scanner, Gateway> args) -> {
                try
                {
                    ShiftId shiftId = printAndWaitForLegalShiftId(args.getKey(), makeTitle("Enter shift information") + "\n");
                    int e_id = printAndWaitForLegalInt(args.getKey(), "Please provide the id of an employee: ", (x) -> (x > 0), "Illegal id. ");
                    Response<Employee> get_employee_res = args.getValue().getEmployee(e_id);
                    if (!get_employee_res.isSuccess())
                    {
                        throw new CLIException(get_employee_res.getMessage());
                    }
                    Employee employee = get_employee_res.getData();
                    Response<Employee> rem_emp_res = args.getValue().removeWorker(shiftId, employee);
                    if (!rem_emp_res.isSuccess())
                    {
                        throw new CLIException(rem_emp_res.getMessage());
                    }
                    System.out.println(makeSuccessMessage());
                } catch (Exception e) {
                    System.out.println(makeErrorMessage(e.getMessage()));
                } finally {
                    return true;
                }
            }));
        }};
    }

    @Override
    public String getTitle()
    {
        return "Manage Shifts for Branch #" +this.branchId;
    }
}
