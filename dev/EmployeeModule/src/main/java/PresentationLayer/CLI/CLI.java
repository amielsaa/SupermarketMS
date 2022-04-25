package PresentationLayer.CLI;


import BusinessLayer.*;
import ServiceLayer.Gateway;
import Utilities.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

public class CLI {

    private BufferedReader input;
    private InputStreamReader r;
    private final Gateway gateway;

    public static void main(String[] args) {
        CLI cli = new CLI();
        cli.run();
    }

    public CLI() {
        r = new InputStreamReader(System.in);
        input = new BufferedReader(r);


        //this.input = new Scanner(System.in);
        this.gateway = new Gateway();
    }

    private void run(){
        //TODO: initiation of default data; remove after adding DAL
        gateway.initDefaultData();
        System.out.println("____    __    ____  _______  __        ______   ______   .___  ___.  _______    .___________.  ______          _______. __    __  .______    _______ .______       __       __  \n" +
                           "\\   \\  /  \\  /   / |   ____||  |      /      | /  __  \\  |   \\/   | |   ____|   |           | /  __  \\        /       ||  |  |  | |   _  \\  |   ____||   _  \\     |  |     |  | \n" +
                           " \\   \\/    \\/   /  |  |__   |  |     |  ,----'|  |  |  | |  \\  /  | |  |__      `---|  |----`|  |  |  |      |   (----`|  |  |  | |  |_)  | |  |__   |  |_)  |    |  |     |  | \n" +
                           "  \\            /   |   __|  |  |     |  |     |  |  |  | |  |\\/|  | |   __|         |  |     |  |  |  |       \\   \\    |  |  |  | |   ___/  |   __|  |      /     |  |     |  | \n" +
                           "   \\    /\\    /    |  |____ |  `----.|  `----.|  `--'  | |  |  |  | |  |____        |  |     |  `--'  |   .----)   |   |  `--'  | |  |      |  |____ |  |\\  \\----.|  `----.|  | \n" +
                           "    \\__/  \\__/     |_______||_______| \\______| \\______/  |__|  |__| |_______|       |__|      \\______/    |_______/     \\______/  | _|      |_______|| _| `._____||_______||__| \n" +
                           " _______ .___  ___. .______    __        ______   ____    ____  _______  _______    .___  ___.   ______    _______   __    __   __       _______                                \n" +
                           "|   ____||   \\/   | |   _  \\  |  |      /  __  \\  \\   \\  /   / |   ____||   ____|   |   \\/   |  /  __  \\  |       \\ |  |  |  | |  |     |   ____|                               \n" +
                           "|  |__   |  \\  /  | |  |_)  | |  |     |  |  |  |  \\   \\/   /  |  |__   |  |__      |  \\  /  | |  |  |  | |  .--.  ||  |  |  | |  |     |  |__                                  \n" +
                           "|   __|  |  |\\/|  | |   ___/  |  |     |  |  |  |   \\_    _/   |   __|  |   __|     |  |\\/|  | |  |  |  | |  |  |  ||  |  |  | |  |     |   __|                                 \n" +
                           "|  |____ |  |  |  | |  |      |  `----.|  `--'  |     |  |     |  |____ |  |____    |  |  |  | |  `--'  | |  '--'  ||  `--'  | |  `----.|  |____                                \n" +
                           "|_______||__|  |__| | _|      |_______| \\______/      |__|     |_______||_______|   |__|  |__|  \\______/  |_______/  \\______/  |_______||_______|                               \n" +
                           "                                                                                                                                                             ");
        System.out.println("to proceed, provide your ID number or -1 to cancel");
        int userId = getIntInput(input);
        if(userId == -1){
            return;
        }
        boolean loggedOut = false;
        Response<Employee> loginRes = gateway.login(userId);
        while(!loginRes.isSuccess()){
            System.out.println("Such id does not exist in the system, try again");
            userId = getIntInput(input);
            loginRes = gateway.login(userId);
        }
        while(!loggedOut){
            System.out.println("Welcome, " + loginRes.getData().getName());
            System.out.println("To manage employees press 1");
            System.out.println("To manage shifts press 2");
            System.out.println("To manage qualifications press 3");
            System.out.println("To logout, press 4");
            int user_input = getIntInput(input);
            final int employees = 1, shifts = 2, qualifications = 3, logout = 4;
            switch (user_input){
                case employees: {
                    employeeManagement();
                    break;
                }
                case shifts: {
                    shiftManagement();
                    break;
                }
                case qualifications:{
                    qualificationManagement();
                    break;
                }
                case logout: {
                    gateway.logout();
                    loggedOut = true;
                    break;
                }
                default:{
                    System.out.println("Wrong input, try again");
                }
            }
        }
        try{
            input.close();
            r.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void employeeManagement(){
        System.out.println("Choose from the menu:\n" +
                           "1.Add employee\n" +
                           "2.Remove employee\n" +
                           "3.Get employee (Note:leave it be or not?)\n" +
                           "4.Update employee name\n" +
                           "5.Update employee salary\n" +
                           "6.Update employee bank account details\n" +
                           "7.Add employee working hours\n" +
                           "8.Remove employee working hours\n" +
                           "9.Add employee qualifications\n" +
                           "10.Remove employee qualifications\n" +
                           "11.Get employees list\n" +
                           "12.Return to main menu");
        final int addEmployee = 1, removeEmployee = 2, getEmployee = 3, updateEmployeeName = 4, updateEmployeeSalary = 5, updateEmployeeBankAccountDetails = 6,
                addEmployeeWorkingHours = 7, removeEmployeeWorkingHours = 8, addEmployeeQualifications = 9, removeEmployeeQualifications = 10, getEmployeesList = 11, returnToMainMenu = 12;
        int choice =  getIntInput(input);
        int e_id = 0;
        if(choice == removeEmployee || choice == getEmployee || choice == updateEmployeeName|| choice == updateEmployeeSalary|| choice == updateEmployeeBankAccountDetails|| choice == addEmployeeWorkingHours||
           choice == removeEmployeeWorkingHours || choice == addEmployeeQualifications || choice == removeEmployeeQualifications){
            System.out.println("Please provide the employee's id number");
            e_id = getIntInput(input);
        }
        switch (choice){
            case addEmployee:
                addEmployee();
                break;
            case removeEmployee:
                Response<Employee> res_rem_emp = gateway.removeEmployee(e_id);
                if(!res_rem_emp.isSuccess()){
                    System.out.println(res_rem_emp.getMessage());
                    break;
                }
                System.out.println("Success");
                break;
            case getEmployee:
                Response<Employee> r_emp = gateway.getEmployee(e_id);
                if(!r_emp.isSuccess()){
                    System.out.println(r_emp.getMessage());
                    break;
                }
                Employee e = r_emp.getData();
                System.out.println(e);
                break;
            case updateEmployeeName:
                System.out.println("Please provide the employee's new name");
                String new_name = getStringInput(input);
                Response<String> r_emp_name = gateway.updateEmployeeName(e_id, new_name);
                if(!r_emp_name.isSuccess()){
                    System.out.println(r_emp_name.getMessage());
                    break;
                }
                System.out.println("Success");
                break;
            case updateEmployeeSalary:
                System.out.println("Please provide the employee's salary");
                double new_salary = getDoubleInput(input);
                Response<Double> r_sal = gateway.updateEmployeeSalary(e_id, new_salary);
                if(!r_sal.isSuccess()){
                    System.out.println(r_sal.getMessage());
                    break;
                }
                System.out.println("Success");
                break;
            case updateEmployeeBankAccountDetails:
                System.out.println("Please provide new bank account details:");
                System.out.println("Bank id");
                int b_id = getIntInput(input);
                System.out.println("Bank branch id");
                int b_b_id = getIntInput(input);
                System.out.println("Account id");
                int acc_id = getIntInput(input);
                System.out.println("Bank name");
                String b_name = getStringInput(input);
                System.out.println("Bank branch name");
                String b_b_name = getStringInput(input);
                System.out.println("Account owner name");
                String acc_o_name = getStringInput(input);
                BankAccountDetails bad = new BankAccountDetails(b_id, b_b_id, acc_id, b_name, b_b_name, acc_o_name);
                Response<BankAccountDetails> r_bad = gateway.updateEmployeeBankAccountDetails(e_id, bad);
                if(!r_bad.isSuccess()){
                    System.out.println(r_bad.getMessage());
                    break;
                }
                System.out.println("Success");
                break;
            case addEmployeeWorkingHours:
                System.out.println("Please provide starting time in the next format: yyyy-MM-ddThh:mm:ss");
                LocalDateTime start = getDateInput(input);
                System.out.println("Please provide ending time in the next format: yyyy-MM-ddThh:mm:ss");
                LocalDateTime finish = getDateInput(input);
                Response<TimeInterval> r_time = gateway.employeeAddWorkingHour(e_id, start, finish);
                if(!r_time.isSuccess()){
                    System.out.println(r_time.getMessage());
                    break;
                }
                System.out.println("Success");
                break;
            case removeEmployeeWorkingHours:
                System.out.println("Please provide starting time in the next format: yyyy-MM-ddThh:mm:ss");
                start = getDateInput(input);
                System.out.println("Please provide ending time in the next format: yyyy-MM-ddThh:mm:ss");
                finish = getDateInput(input);
                r_time = gateway.employeeAddWorkingHour(e_id, start, finish);
                if(!r_time.isSuccess()){
                    System.out.println(r_time.getMessage());
                    break;
                }
                System.out.println("Success");
                break;
            case addEmployeeQualifications:
                System.out.println("Please provide the name of qualification to add");
                String qual_name_adding = getStringInput(input);
                Response<Qualification> q_res_adding = gateway.getQualification(qual_name_adding);
                if(!q_res_adding.isSuccess()){
                    System.out.println(q_res_adding.getMessage());
                    break;
                }
                Response<Qualification> q_add_res = gateway.employeeAddQualification(e_id, q_res_adding.getData());
                if(!q_add_res.isSuccess()){
                    System.out.println(q_add_res.getMessage());
                    break;
                }
                System.out.println("Success");
                break;
            case removeEmployeeQualifications:
                System.out.println("Please provide the name of qualification to delete");
                String qual_name = getStringInput(input);
                Response<Qualification> q_res = gateway.employeeRemoveQualification(e_id, qual_name);
                if(!q_res.isSuccess()){
                    System.out.println(q_res.getMessage());
                    break;
                }
                System.out.println("Success");
                break;
            case getEmployeesList:
                Response<List<Employee>> res_employees = gateway.getEmployees();
                if(!res_employees.isSuccess()){
                    System.out.println(res_employees.getMessage());
                    break;
                }
                List<Employee> employees = res_employees.getData();
                for (Employee employee : employees) {
                    System.out.println(employee);
                }
            case returnToMainMenu:
                return;
            default:
                System.out.println("The input is not recognized, try again");
                employeeManagement();
                break;
        }
    }

    private void addEmployee() {
        System.out.println("Adding the new employee. Please provide the new employee's:");
        System.out.println("ID number");
        int id = getIntInput(input);
        System.out.println("Full name");
        String e_name = getStringInput(input);
        System.out.println("Bank id");
        int b_id = getIntInput(input);
        System.out.println("Bank branch id");
        int b_b_id = getIntInput(input);
        System.out.println("Account id");
        int acc_id = getIntInput(input);
        System.out.println("Bank name");
        String b_name = getStringInput(input);
        System.out.println("Bank branch name");
        String b_b_name = getStringInput(input);
        System.out.println("Account owner name");
        String acc_o_name = getStringInput(input);
        BankAccountDetails bad = new BankAccountDetails(b_id, b_b_id, acc_id, b_name, b_b_name, acc_o_name);
        System.out.println("Salary");
        double salary = getDoubleInput(input);
        System.out.println("Work starting date in the next format yyyy-MM-dd");
        String date = getStringInput(input) + "T06:00:00";
        LocalDateTime st_date = LocalDateTime.parse(date);
        System.out.println("Add the working conditions descriptions");
        String w_c_desc = getStringInput(input);
        Response<Employee> res_add_emp = gateway.addEmployee(id, e_name, bad, salary, st_date, w_c_desc);
        if(!res_add_emp.isSuccess()){
            System.out.println(res_add_emp.getMessage());
            return;
        }
        System.out.println("Success");
    }


    private int getIntInput(BufferedReader sc){
        while(true){
            String in = getStringInput(input);
            if(in.matches("-?\\d+")){
                return Integer.parseInt(in);
            }
            else{
                System.out.println("Only a whole number can be received");
            }
        }
    }

    private boolean isDouble(String s){
        try
        {
            Double.parseDouble(s);
        }
        catch (NumberFormatException e)
        {
            return false;
        }
        return true;
    }

    private double getDoubleInput(BufferedReader sc){
        while(true){
            String in = getStringInput(input);
            if(isDouble(in)){
                return Double.parseDouble(in);
            }
            else{
                System.out.println("Only a whole number can be received");
            }
        }
    }




    private LocalDateTime getDateInput(BufferedReader sc){
        System.out.println("Please provide the date in the following format: 'yyyy-MM-dd', for example: 2022-03-29;\n" +
                           "If you want you can add the full time in format 'yyyy-MM-ddThh:mm:ss', for example: 2022-03-29T21:08:30");
        while (true){
            String in = getStringInput(input);
            if(isDateAndTime(in + "T06:00:00")){
                return LocalDateTime.parse(in + "T06:00:00");
            }
            else if(isDateAndTime(in)){
                return LocalDateTime.parse(in);
            }
            else{
                System.out.println("Wrong input, try again");
            }
        }
    }

    private boolean isDateAndTime(String s){
        try{
            LocalDateTime.parse(s);
        }
        catch (DateTimeParseException e){
            return false;
        }
        return true;
    }

    private String getStringInput(BufferedReader br){
        String line = null;
        try {
            line = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return line;
    }


    private void shiftManagement(){
        System.out.println("Choose from the menu:\n" +
                           "1.Add shift\n" +
                           "2.Remove shift\n" +
                           "3.Add worker\n" +
                           "4.Remove worker\n" +
                           "5.Get shifts\n" +
                           "6.Return to main menu");
        final int addShift = 1, removeShift = 2, addWorker = 3, removeWorker = 4, getShifts = 5, returnToMainMenu = 6;
        int choice =  getIntInput(input);
        switch (choice){
            case addShift:
                ShiftId shiftId = getShiftIdInput(input);
                List<String> qualifications_required = Arrays.asList("Cashier", "WarehouseWorker", "StockClerk", "TruckDriver");
                List<String> qualifications_required_day = Arrays.asList("Cashier", "WarehouseWorker", "StockClerk", "TruckDriver", "Branch"+shiftId.getBranchId()+"Manager", "InventoryManager");
                Map<Employee, List<Qualification>> shift_workers = new HashMap<>();
                if(shiftId.getShiftTime() == ShiftTime.DAY) {
                    //Arrays.asList(qualifications_required, qualifications_required_day);
                    qualifications_required = qualifications_required_day;
                    //qualifications_required.addAll(qualifications_required_day);
                }
                for (String s : qualifications_required) {
                    Qualification q = gateway.getQualification(s).getData();
                    Employee e = chooseWorkerWithQualification(q, shiftId.getShiftTime(), shiftId.getBranchId());
                    if(shift_workers.containsKey(e)){
                        shift_workers.get(e).add(q);
                    }
                    else{
                        ArrayList<Qualification> l = new ArrayList<Qualification>();
                        l.add(q);
                        shift_workers.put(e, l);
                    }
                }
                Qualification q = gateway.getQualification("ShiftManager").getData();
                Employee shift_manager = chooseWorkerWithQualification(q, shiftId.getShiftTime(), shiftId.getBranchId());
                if(shift_manager == null){
                    System.out.println("No available managers. Returning");
                    break;
                }
                Response<Shift> res_add_shift = gateway.addShift(shiftId.getBranchId(), shiftId.getDate(), shift_manager, shift_workers, shiftId.getShiftTime());
                if(!res_add_shift.isSuccess()){
                    System.out.println(res_add_shift.getMessage());
                    break;
                }
                System.out.println("Success");
                break;
            case removeShift:
                shiftId = getShiftIdInput(input);
                Response<Shift> res_rem_shift = gateway.removeShift(shiftId);
                if(!res_rem_shift.isSuccess()){
                    System.out.println(res_rem_shift.getMessage());
                    break;
                }
                System.out.println("Success");
                break;
            case addWorker:
                shiftId = getShiftIdInput(input);
                System.out.println("Please provide the id of an employee");
                int e_id = getIntInput(input);
                Response<Employee> get_employee_res = gateway.getEmployee(e_id);
                if(!get_employee_res.isSuccess()){
                    System.out.println(get_employee_res.getMessage());
                    break;
                }
                Employee employee = get_employee_res.getData();
                ArrayList<Qualification> qualifications_list = new ArrayList<Qualification>();
                List<Qualification> e_qualifications_list = employee.getWorkingConditions().getQualifications();
                int user_input = -2;
                int counter = 0;
                boolean done = false;
                System.out.println("Please choose the qualifications to add from the list below; to return insert -1");
                for (Qualification qualification : e_qualifications_list) {
                    System.out.println(counter + "." + qualification.getName());
                }
                while (!done){
                    user_input = getIntInput(input);
                    if(user_input > e_qualifications_list.size()-1 || user_input < -1){
                        System.out.println("Illegal input, try again");
                    }
                    else if(user_input == -1){
                        done = true;
                    }
                    else{
                        qualifications_list.add(e_qualifications_list.get(user_input));
                    }
                }
                System.out.println(e_qualifications_list);
                Response<Employee> add_worker_res = gateway.addWorker(shiftId, get_employee_res.getData(), qualifications_list);
                if(!add_worker_res.isSuccess()){
                    System.out.println(add_worker_res.getMessage());
                    break;
                }
                System.out.println("Success");
                break;
            case removeWorker:
                shiftId = getShiftIdInput(input);
                System.out.println("Please provide the id of an employee");
                e_id = getIntInput(input);
                get_employee_res = gateway.getEmployee(e_id);
                if(!get_employee_res.isSuccess()){
                    System.out.println(get_employee_res.getMessage());
                    break;
                }
                employee = get_employee_res.getData();
                Response<Employee> rem_emp_res = gateway.removeWorker(shiftId, employee);
                if(!rem_emp_res.isSuccess()){
                    System.out.println(rem_emp_res.getMessage());
                    break;
                }
                System.out.println("Success");
                break;
            case getShifts:
                System.out.println("Please provide the branch number");
                int b_number = getIntInput(input);
                Response<List<Shift>> res_shifts = gateway.getShifts(b_number);
                if(!res_shifts.isSuccess()){
                    System.out.println(res_shifts.getMessage());
                    break;
                }
                System.out.println(res_shifts.getData());
                break;
            case returnToMainMenu:
                return;
            default:
                System.out.println("Wrong input, try again");
                shiftManagement();
                break;
        }
    }

    private ShiftId getShiftIdInput(BufferedReader sc){
        System.out.println("Please provide the branch id");
        int branchId = getIntInput(input);
        System.out.println("Please provide the date of a shift");
        LocalDateTime date = getDateInput(input);
        System.out.println("Please provide the shift time: DAY or NIGHT");
        ShiftTime time = null;
        while (time == null){
            String _sh_time = getStringInput(input);
            time = _sh_time.equals("DAY") ? ShiftTime.DAY : (_sh_time.equals("NIGHT") ? ShiftTime.NIGHT : null);
            if(time == null)
                System.out.println("Wrong input. Try again");
        }
        return new ShiftId(branchId, date, time);
    }

    private Employee chooseWorkerWithQualification(Qualification q, ShiftTime s, int branchId){
        Response<Map<Employee, int[]>> res = gateway.getEmployeesWithQualification(branchId, q);
        if(!res.isSuccess()){
            System.out.println(res.getMessage());
            return null;
        }
        Map<Employee, int[]> emp_map = res.getData();
        LinkedHashMap<Employee, int[]> emp_map_sorted = qualMapSorter(emp_map, s);
        System.out.println("Adding " + q.getName() + " Please choose an employee from the list");
        int number = 0;
        for (Map.Entry<Employee, int[]> employeeEntry : emp_map_sorted.entrySet()) {
            String line = number + " " + employeeEntry.getKey().getName() + " " + "number of shifts done: " + employeeEntry.getValue()[s.ordinal()];
            number++;
            System.out.println(line);
        }
        System.out.println("Please insert the number of the employee that you want to add");
        int choice = -1;
        ArrayList<Employee> list = new ArrayList<>(emp_map_sorted.keySet());
        while(choice == -1){
            choice = getIntInput(input);
            if(choice<0 || choice>list.size()-1){
                System.out.println("The number is out of bounds, try again");
                choice = -1;
            }
        }
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

    private void qualificationManagement(){
        System.out.println("Choose from the menu:\n" +
                           "1.Get the full list of qualifications\n" +
                           "2.Get the full list of permissions\n" +
                           "2.Add qualification\n" +
                           "3.Add permission\n" +
                           "4.Remove permission\n" +
                           "5.Add permission to qualification\n" +
                           "6.Remove permission from qualification\n" +
                           "7.Return to main menu");
        final int getQualificationsList = 1, getPermissionList = 2, addQualification = 3, addPermission = 4, removePermission = 5, addPermissionToQual = 6, removePermissionFromQual = 7, returnToMainMenu = 8;
        int choice =  getIntInput(input);
        switch (choice){
            case getQualificationsList:
                Response<List<Qualification>> res_quals = gateway.getQualifications();
                if(!res_quals.isSuccess()){
                    System.out.println(res_quals.getMessage());
                    break;
                }
                System.out.println(res_quals.getData());
                break;
            case addQualification:
                System.out.println("Please provide a name for qualification");
                String qual_name = getStringInput(input);

                Response<Qualification> res_qual = gateway.addQualification(qual_name);
                if(!res_qual.isSuccess()){
                    System.out.println(res_qual.getMessage());
                    break;
                }
                System.out.println("Success");
                break;
            case getPermissionList:
                Response<List<Permission>> res_perms = gateway.getPermissions();
                if(!res_perms.isSuccess()){
                    System.out.println(res_perms.getMessage());
                    break;
                }
                System.out.println(res_perms.getData());
                break;
                /*
            case 3:
                System.out.println("Please provide the old name of qualification");
                String old_qual_name = getStringInput(input);
                System.out.println("Please provide a new name for qualification");
                String new_qual_name = getStringInput(input);
                Response<Qualification> res_qual_rename = gateway.renameQualification(old_qual_name, new_qual_name);
                if(!res_qual_rename.isSuccess()){
                    System.out.println(res_qual_rename.getMessage());
                    break;
                }
                System.out.println("Success");
                break;

                 */
            case addPermission:
                System.out.println("Please provide a name for permission");
                String perm_name = getStringInput(input);
                Response<Permission> res_perm = gateway.addPermission(perm_name);
                if(!res_perm.isSuccess()){
                    System.out.println(res_perm.getMessage());
                    break;
                }
                System.out.println("Success");
                break;
            case removePermission:
                System.out.println("Please provide a name of a permission you want to remove");
                String perm_name_rem = getStringInput(input);
                Response<Permission> res_perm_rem = gateway.removePermission(perm_name_rem);
                if(!res_perm_rem.isSuccess()){
                    System.out.println(res_perm_rem.getMessage());
                    break;
                }
                System.out.println("Success");
                break;
            case addPermissionToQual:
                System.out.println("Please provide a name of a permission you want to add");
                String perm_name_add = getStringInput(input);
                System.out.println("Please provide a name of a qualification you want to add the permission to");
                String qual_name_add = getStringInput(input);
                Response<Qualification> res_perm_add = gateway.addPermissionToQualification(perm_name_add, qual_name_add);
                if(!res_perm_add.isSuccess()){
                    System.out.println(res_perm_add.getMessage());
                    break;
                }
                System.out.println("Success");
                break;
            case removePermissionFromQual:
                System.out.println("Please provide a name of a permission you want to remove");
                String perm_name_rem2 = getStringInput(input);
                System.out.println("Please provide a name of a qualification you want to remove the permission from");
                String qual_name_rem = getStringInput(input);
                Response<Qualification> res_perm_rem2 = gateway.removePermissionFromQualification(perm_name_rem2, qual_name_rem);
                if(!res_perm_rem2.isSuccess()){
                    System.out.println(res_perm_rem2.getMessage());
                    break;
                }
                System.out.println("Success");
                break;
            case returnToMainMenu:
                return;
            default:
                qualificationManagement();
                break;
        }

    }

}