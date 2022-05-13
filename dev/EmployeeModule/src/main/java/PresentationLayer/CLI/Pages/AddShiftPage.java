package PresentationLayer.CLI.Pages;

import BusinessLayer.*;
import ServiceLayer.Gateway;
import Utilities.CLIException;
import Utilities.PrettyTable;
import Utilities.Response;

import java.util.*;
import java.util.stream.Collectors;

import static Utilities.PrettyInput.*;
import static Utilities.PrettyPrint.*;

public class AddShiftPage extends ResponsePage<Boolean>
{
    private final int branchId;

    public AddShiftPage(int branchId) {
        this.branchId = branchId;
    }

    @Override
    public Boolean runWithResponse(Scanner input, Gateway g) throws CLIException
    {
        try
        {
            ShiftId shiftId = printAndWaitForLegalShiftId(input, makeTitle("Enter the new shift's information"), this.branchId);
            List<String> qualifications_required = Arrays.asList("Cashier", "WarehouseWorker", "StockClerk", "TruckDriver");
            List<String> qualifications_required_day = Arrays.asList("Cashier", "WarehouseWorker", "StockClerk", "TruckDriver", "Branch" + shiftId.getBranchId() + "Manager", "InventoryManager");
            Map<Integer, List<String>> shift_workers = new HashMap<>();
            if(shiftId.getShiftTime() == ShiftTime.DAY) {
                //Arrays.asList(qualifications_required, qualifications_required_day);
                qualifications_required = qualifications_required_day;
                //qualifications_required.addAll(qualifications_required_day);
            }
            for (String s : qualifications_required) {
                Qualification q = g.getQualification(s).getData();
                Employee e = chooseWorkerWithQualification(input, g, q, shiftId.getShiftTime(), shiftId.getBranchId());
                if(shift_workers.containsKey(e.getId())){
                    shift_workers.get(e.getId()).add(q.getName());
                }
                else{
                    ArrayList<String> l = new ArrayList<>();
                    l.add(q.getName());
                    shift_workers.put(e.getId(), l);
                }
            }
            Qualification q = g.getQualification("ShiftManager").getData();
            Employee shift_manager = chooseWorkerWithQualification(input, g, q, shiftId.getShiftTime(), shiftId.getBranchId());
            if(shift_manager == null){
                throw new CLIException("No available managers. ");
            }
            Response<Shift> res_add_shift = g.addShift(shiftId.getBranchId(), shiftId.getDate(), shift_manager, shift_workers, shiftId.getShiftTime());
            if(!res_add_shift.isSuccess()){
                throw new CLIException(res_add_shift.getMessage());
            }
            System.out.println(makeSuccessMessage());

        } catch (Exception e) {
            System.out.println(makeErrorMessage("Failed to add new shift, " + e.getMessage()));
        }
        return true;
    }

    private Employee chooseWorkerWithQualification(Scanner input, Gateway g, Qualification q, ShiftTime s, int branchId){
        Response<Map<Employee, int[]>> res = g.getEmployeesWithQualification(branchId, q);
        if(!res.isSuccess()){
            System.out.println(res.getMessage());
            return null;
        }
        Map<Employee, int[]> emp_map = res.getData();
        LinkedHashMap<Employee, int[]> emp_map_sorted = qualMapSorter(emp_map, s);
        System.out.println(makeTitle("Adding " + q.getName() + " Please choose an employee from the list: "));
        PrettyTable table = new PrettyTable("Index", "Name", "Shifts Done");
        int number = 0;
        for (Map.Entry<Employee, int[]> employeeEntry : emp_map_sorted.entrySet()) {
            table.insert(Integer.toString(number), employeeEntry.getKey().getName(), Integer.toString(employeeEntry.getValue()[s.ordinal()]));
            number++;
        }
        System.out.println(table);
        ArrayList<Employee> list = new ArrayList<>(emp_map_sorted.keySet());
        int choice = printAndWaitForLegalInt(input, "Please insert the index of the employee that you want to add: ", (x) -> (x >= 0 && x < list.size()), "The number is out of bounds, try again");
        return list.get(choice);
    }

    private LinkedHashMap<Employee, int[]> qualMapSorter(Map<Employee, int[]> unsorted, ShiftTime s_time){
        List<Map.Entry<Employee, int[]>> list = new LinkedList<>(unsorted.entrySet());
        list.sort((o1, o2) -> {
            if (o1.getKey().equals(o2.getKey())) {
                return 0;
            } else if (o1.getValue()[s_time.ordinal()] < o2.getValue()[s_time.ordinal()]) {
                return -1;
            } else {
                return 1;
            }
        });
        return list.stream().collect(
                Collectors.toMap(
                        Map.Entry::getKey, Map.Entry::getValue, (a, b) -> b, LinkedHashMap::new
                )
        );
    }
}
