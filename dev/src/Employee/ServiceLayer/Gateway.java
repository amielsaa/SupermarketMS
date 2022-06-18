package Employee.ServiceLayer;

import Delivery.ServiceLayer.DeliveryService;
import Employee.BusinessLayer.*;
import SupplierInventory.SIService;
import Utilities.Response;
import com.sun.istack.internal.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import org.jetbrains.annotations.NotNull;

public class Gateway
{
    private DeliveryService deliveryService;
    private SIService siService;

    private int loggedEmployeeId;
    private EmployeeController employeeController;
    private ShiftController shiftController;
    private QualificationController qualificationController;

    private final int ADMIN_UID = 580083434;

    public Gateway() {
        // INITIALIZE OTHER SERVICES
        this.deliveryService = new DeliveryService(this);
        this.siService = new SIService(this);
        // -------------------------

        this.loggedEmployeeId = -1;

        this.employeeController = new EmployeeController();
        this.shiftController = new ShiftController();
        this.qualificationController = new QualificationController();
    }


    public void removeAllData(){
        employeeController.clearCache();
        shiftController.clearCache();
        qualificationController.clearCache();
    }

    public void initDefaultData() throws Exception {
        // TODO DAL make this run once on database init and NOT delete the entire database on load.
        // Removes the employee module tables
        employeeController.clearDatabases();
        shiftController.clearDatabases();
        qualificationController.clearDatabases();

        deliveryService.clearDatabases(); // Removes the delivery module tables
        siService.deleteAllData(); // Removes the inventory module tables
        siService.DeleteAll(); // Removes the supplier module tables
        // INIT PERMISSIONS AND QUALIFICATIONS

        String[] permissions = {"ManageBranch1", "ManageBranch2", "ManageBranch0",
                "ViewEmployees", "ManageEmployees",                 //Employee module
                "ViewQualifications", "ManageQualifications",       //
                "ManageShift", "ViewShift",                         //
                "ManageDeliveries", "ViewDeliveries",               //Delivery module Manage: log. manager
                "ManageInventory", "ViewInventory",                 //Inventory module Manage: inventory manager
                "ManageSuppliers", "ViewSuppliers",                 //Supplier module Manage: inventory manager and branch manager
                "ManageOrders", "ViewOrders"};                      //
        for (String p : permissions)
        {
            qualificationController.addPermission(p);
        }
        //HR
        String[] permissionsHR = {"ViewEmployees", "ManageEmployees", "ViewQualifications", "ManageQualifications", "ManageShift", "ViewShift"};
        Qualification qualificationHR = qualificationController.addQualification("HR");
        for(String p : permissionsHR) {
            qualificationController.addPermissionToQualification(p, qualificationHR.getName());
        }

        //String[] permissionsAssistant = {"ViewEmployees", "ViewQualifications"};

        //Managers ------------------------------------------------
        Qualification qualificationBranch0Manager = qualificationController.addQualification("Branch0Manager");
        String[] permissionsBranch0Manager = {"ManageBranch0", "ViewEmployees", "ViewQualifications", "ViewShift",
                "ViewDeliveries", "ViewInventory", "ViewSuppliers", "ManageSuppliers", "ViewOrders"};
        for(String p : permissionsBranch0Manager) {
            qualificationController.addPermissionToQualification(p, qualificationBranch0Manager.getName());
        }


        Qualification qualificationBranch1Manager = qualificationController.addQualification("Branch1Manager");
        String[] permissionsBranch1Manager = {"ManageBranch1", "ViewEmployees", "ViewQualifications", "ViewShift",
                "ViewDeliveries", "ViewInventory", "ViewSuppliers", "ManageSuppliers", "ViewOrders"};
        for(String p : permissionsBranch1Manager) {
            qualificationController.addPermissionToQualification(p, qualificationBranch1Manager.getName());
        }


        Qualification qualificationBranch2Manager = qualificationController.addQualification("Branch2Manager");
        String[] permissionsBranch2Manager = {"ManageBranch2", "ViewEmployees", "ViewQualifications", "ViewShift",
                "ViewDeliveries", "ViewInventory", "ViewSuppliers", "ManageSuppliers", "ViewOrders"};
        for(String p : permissionsBranch2Manager) {
            qualificationController.addPermissionToQualification(p, qualificationBranch2Manager.getName());
        }
        //--------------------------------------------------------------

        Qualification qualificationShiftManager = qualificationController.addQualification("ShiftManager");
        Qualification qualificationCashier = qualificationController.addQualification("Cashier");
        Qualification qualificationWarehouse = qualificationController.addQualification("WarehouseWorker");
        Qualification qualificationStock = qualificationController.addQualification("StockClerk");
        Qualification qualificationCleaner = qualificationController.addQualification("Cleaner");
        Qualification qualificationInventoryManager = qualificationController.addQualification("InventoryManager");
        Qualification qualificationLogisticsManager = qualificationController.addQualification("LogisticsManager");
        Qualification qualificationDriver = qualificationController.addQualification("Driver");

        //qualificationController.addPermissionToQualification("ManageShift", "ShiftManager");
        qualificationController.addPermissionToQualification("ManageInventory", "InventoryManager");
        qualificationController.addPermissionToQualification("ViewInventory", "InventoryManager");
        qualificationController.addPermissionToQualification("ManageSuppliers", "InventoryManager");
        qualificationController.addPermissionToQualification("ViewSuppliers", "InventoryManager");
        qualificationController.addPermissionToQualification("ManageDeliveries", "LogisticsManager");
        qualificationController.addPermissionToQualification("ViewDeliveries", "LogisticsManager");

        // INIT EMPLOYEES
        BankAccountDetails defaultBankAccountDetails = new BankAccountDetails(0, 0, 0, "Bank", "Branch", "Bob");
        employeeController.addEmployee(ADMIN_UID, "Admin", defaultBankAccountDetails, 0, LocalDateTime.now(), "");
        employeeController.addEmployee(123, "Admin2", defaultBankAccountDetails, 0, LocalDateTime.now(), "");

        employeeController.addEmployee(1, "Bob The Cashier", defaultBankAccountDetails, 0, LocalDateTime.now(), "");
        employeeController.addEmployee(2, "Alice The Warehouse Worker", defaultBankAccountDetails, 0, LocalDateTime.now(), "");
        employeeController.addEmployee(3, "Cat The Stock Clerk", defaultBankAccountDetails, 0, LocalDateTime.now(), "");
        employeeController.addEmployee(4, "Dan The Truck Driver", defaultBankAccountDetails, 0, LocalDateTime.now(), "");
        employeeController.addEmployee(5, "Manny The Manager", defaultBankAccountDetails, 0, LocalDateTime.now(), "");
        employeeController.addEmployee(6, "InManny The InManager", defaultBankAccountDetails, 0, LocalDateTime.now(), "");
        employeeController.addEmployee(7, "LogManny The LogManager", defaultBankAccountDetails, 0, LocalDateTime.now(), "");




        employeeController.employeeAddQualification(1, qualificationCashier);
        employeeController.employeeAddQualification(2, qualificationWarehouse);
        employeeController.employeeAddQualification(3, qualificationStock);
        //employeeController.employeeAddQualification(4, qualificationTruck);
        employeeController.employeeAddQualification(6, qualificationInventoryManager);
        employeeController.employeeAddQualification(7, qualificationLogisticsManager);

        employeeController.employeeAddQualification(1, qualificationShiftManager);
        employeeController.employeeAddQualification(2, qualificationShiftManager);
        employeeController.employeeAddQualification(3, qualificationShiftManager);
        employeeController.employeeAddQualification(4, qualificationShiftManager);

        employeeController.employeeAddQualification(5, qualificationBranch0Manager);
        employeeController.employeeAddQualification(5, qualificationBranch1Manager);
        employeeController.employeeAddQualification(6, qualificationShiftManager);



        employeeController.employeeAddQualification(ADMIN_UID, qualificationHR);
        employeeController.employeeAddQualification(123, qualificationHR);

        //employeeController.addEmployee(200000001, "C1 driver 1", defaultBankAccountDetails, 0, LocalDateTime.now(), "");
        //employeeController.addEmployee(200000002, "C1 driver 2", defaultBankAccountDetails, 0, LocalDateTime.now(), "");
        //employeeController.addEmployee(200000003, "C1 driver 3", defaultBankAccountDetails, 0, LocalDateTime.now(), "");
        //employeeController.addEmployee(200000004, "C driver 1", defaultBankAccountDetails, 0, LocalDateTime.now(), "");
        //employeeController.addEmployee(200000005, "C driver 2", defaultBankAccountDetails, 0, LocalDateTime.now(), "");
        //employeeController.addEmployee(200000006, "C driver 3", defaultBankAccountDetails, 0, LocalDateTime.now(), "");

        loggedEmployeeId = ADMIN_UID;
        driverAddQualification(4, "C");
        //driverAddQualification(200000001, "C1");
        //driverAddQualification(200000002, "C1");
        //driverAddQualification(200000003, "C1");
        //driverAddQualification(200000004, "C");
        //driverAddQualification(200000005, "C");
        //driverAddQualification(200000006, "C");
        loggedEmployeeId = -1;

        DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        //HashMap hm1 = new HashMap<Integer, List<String>>(){{
        //    put(200000001, Arrays.asList("Driver"));
        //    put(2, Arrays.asList("WarehouseWorker"));
        //}};
        //HashMap hm2 = new HashMap<Integer, List<String>>(){{
        //    put(200000004, Arrays.asList("Driver"));
        //    put(2, Arrays.asList("WarehouseWorker"));
        //}};
        //shiftController.addShift(1, LocalDateTime.parse("13-10-2023 11:00",dateTimeFormatter) , employeeController.getEmployee(1), hm1, ShiftTime.DAY);
        //shiftController.addShift(1, LocalDateTime.parse("14-10-2023 11:00",dateTimeFormatter) , employeeController.getEmployee(4), hm2, ShiftTime.DAY);

        deliveryService.load();

        loggedEmployeeId = ADMIN_UID;
        debugAddTodayShift();
        loggedEmployeeId = -1;
    }

    public void initDefaultDataTests() throws Exception {
        // Removes the employee module tables
        employeeController.clearDatabases();
        shiftController.clearDatabases();
        qualificationController.clearDatabases();

        deliveryService.clearDatabases(); // Removes the delivery module tables
        siService.deleteAllData(); // Removes the inventory module tables
        siService.DeleteAll(); // Removes the supplier module tables
        // INIT PERMISSIONS AND QUALIFICATIONS

        String[] permissions = {"ViewEmployees", "ManageEmployees", "ViewQualifications", "ManageQualifications", "ManageBranch1", "ManageBranch2", "ManageShift", "ManageDeliveries", "ManageInventory"};
        for (String p : permissions)
        {
            qualificationController.addPermission(p);
        }

        String[] permissionsHR = {"ViewEmployees", "ManageEmployees", "ViewQualifications", "ManageQualifications"};


        Qualification qualificationHR = qualificationController.addQualification("HR");
        String[] permissionsAssistant = {"ViewEmployees", "ViewQualifications"};
        for(String p : permissionsHR) {
            qualificationController.addPermissionToQualification(p, qualificationHR.getName());
        }

        Qualification qualificationBranch1Manager = qualificationController.addQualification("Branch1Manager");
        String[] permissionsBranch1Manager = {"ManageBranch1", "ManageShift", "ViewQualifications", "ViewEmployees", "ManageDeliveries", "ManageInventory"};
        for(String p : permissionsBranch1Manager) {
            qualificationController.addPermissionToQualification(p, qualificationBranch1Manager.getName());
        }

        Qualification qualificationBranch2Manager = qualificationController.addQualification("Branch2Manager");
        String[] permissionsBranch2Manager = {"ManageBranch2", "ManageShift", "ViewQualifications", "ViewEmployees", "ManageDeliveries", "ManageInventory"};
        for(String p : permissionsBranch2Manager) {
            qualificationController.addPermissionToQualification(p, qualificationBranch2Manager.getName());
        }


        Qualification qualificationShiftManager = qualificationController.addQualification("ShiftManager");
        Qualification qualificationCashier = qualificationController.addQualification("Cashier");
        Qualification qualificationWarehouse = qualificationController.addQualification("WarehouseWorker");
        Qualification qualificationStock = qualificationController.addQualification("StockClerk");
        Qualification qualificationTruck = qualificationController.addQualification("TruckDriver");
        Qualification qualificationCleaner = qualificationController.addQualification("Cleaner");
        Qualification qualificationInventoryManager = qualificationController.addQualification("InventoryManager");
        Qualification qualificationLogisticsManager = qualificationController.addQualification("LogisticsManager");
        Qualification qualificationDriver = qualificationController.addQualification("Driver");

        qualificationController.addPermissionToQualification("ManageShift", "ShiftManager");
        qualificationController.addPermissionToQualification("ManageInventory", "InventoryManager");
        qualificationController.addPermissionToQualification("ManageDeliveries", "LogisticsManager");

        // INIT EMPLOYEES
        BankAccountDetails defaultBankAccountDetails = new BankAccountDetails(0, 0, 0, "Bank", "Branch", "Bob");
        employeeController.addEmployee(ADMIN_UID, "Admin", defaultBankAccountDetails, 0, LocalDateTime.now(), "");
        employeeController.addEmployee(123, "Admin2", defaultBankAccountDetails, 0, LocalDateTime.now(), "");

        employeeController.addEmployee(1, "Bob The Cashier", defaultBankAccountDetails, 0, LocalDateTime.now(), "");
        employeeController.addEmployee(2, "Alice The Warehouse Worker", defaultBankAccountDetails, 0, LocalDateTime.now(), "");
        employeeController.addEmployee(3, "Cat The Stock Clerk", defaultBankAccountDetails, 0, LocalDateTime.now(), "");
        employeeController.addEmployee(4, "Dan The Truck Driver", defaultBankAccountDetails, 0, LocalDateTime.now(), "");
        employeeController.addEmployee(5, "Manny The Manager", defaultBankAccountDetails, 0, LocalDateTime.now(), "");
        employeeController.addEmployee(6, "InManny The InManager", defaultBankAccountDetails, 0, LocalDateTime.now(), "");
        employeeController.addEmployee(7, "LogManny The LogManager", defaultBankAccountDetails, 0, LocalDateTime.now(), "");

        employeeController.employeeAddQualification(1, qualificationCashier);
        employeeController.employeeAddQualification(2, qualificationWarehouse);
        employeeController.employeeAddQualification(3, qualificationStock);
        employeeController.employeeAddQualification(4, qualificationTruck);
        employeeController.employeeAddQualification(6, qualificationInventoryManager);
        employeeController.employeeAddQualification(7, qualificationLogisticsManager);

        employeeController.employeeAddQualification(1, qualificationShiftManager);
        employeeController.employeeAddQualification(2, qualificationShiftManager);
        employeeController.employeeAddQualification(3, qualificationShiftManager);
        employeeController.employeeAddQualification(4, qualificationShiftManager);

        employeeController.employeeAddQualification(5, qualificationBranch1Manager);
        employeeController.employeeAddQualification(6, qualificationShiftManager);



        employeeController.employeeAddQualification(ADMIN_UID, qualificationHR);
        employeeController.employeeAddQualification(123, qualificationHR);

        employeeController.addEmployee(200000001, "C1 driver 1", defaultBankAccountDetails, 0, LocalDateTime.now(), "");
        employeeController.addEmployee(200000002, "C1 driver 2", defaultBankAccountDetails, 0, LocalDateTime.now(), "");
        employeeController.addEmployee(200000003, "C1 driver 3", defaultBankAccountDetails, 0, LocalDateTime.now(), "");
        employeeController.addEmployee(200000004, "C driver 1", defaultBankAccountDetails, 0, LocalDateTime.now(), "");
        employeeController.addEmployee(200000005, "C driver 2", defaultBankAccountDetails, 0, LocalDateTime.now(), "");
        employeeController.addEmployee(200000006, "C driver 3", defaultBankAccountDetails, 0, LocalDateTime.now(), "");

        loggedEmployeeId = ADMIN_UID;
        driverAddQualification(200000001, "C1");
        driverAddQualification(200000002, "C1");
        driverAddQualification(200000003, "C1");
        driverAddQualification(200000004, "C");
        driverAddQualification(200000005, "C");
        driverAddQualification(200000006, "C");
        loggedEmployeeId = -1;

        DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        HashMap hm1 = new HashMap<Integer, List<String>>(){{
            put(200000001, Arrays.asList("Driver"));
            put(2, Arrays.asList("WarehouseWorker"));
        }};
        HashMap hm2 = new HashMap<Integer, List<String>>(){{
            put(200000004, Arrays.asList("Driver"));
            put(2, Arrays.asList("WarehouseWorker"));
        }};
        shiftController.addShift(1, LocalDateTime.parse("13-10-2023 11:00",dateTimeFormatter) , employeeController.getEmployee(1), hm1, ShiftTime.DAY);
        shiftController.addShift(1, LocalDateTime.parse("14-10-2023 11:00",dateTimeFormatter) , employeeController.getEmployee(4), hm2, ShiftTime.DAY);

        deliveryService.load();
    }


    // TODO change to user-pass authentication
    public Response<Employee> login(int id) {
        try
        {
            Employee e = employeeController.getEmployee(id);
            this.loggedEmployeeId = id;
            return Response.makeSuccess(e);
        } catch (Exception e) {
            return Response.makeFailure(e.getMessage());
        }
    }
    public Response<Employee> getLoggedUser() {
        if(loggedEmployeeId == -1) {
            return Response.makeFailure("No user is logged in. ");
        }
        try {
            Employee e = employeeController.getEmployee(loggedEmployeeId);
            return Response.makeSuccess(e);
        } catch (Exception e){
            return Response.makeFailure(e.getMessage());
        }
    }
    public void logout() {
        this.loggedEmployeeId = -1;
    }

    private void checkAuth(String permission) throws Exception
    {
        if (loggedEmployeeId == -1)
        {
            throw new Exception("Not logged in.");
        }
        List<String> qualNames = employeeController.getEmployee(loggedEmployeeId).getWorkingConditions().getQualifications();
        Permission p = qualificationController.getPermission(permission);
        Permission p2 = qualificationController.checkPermission(qualNames, p);
    }

    // EMPLOYEE FUNCTIONS

    public Response<Employee> getEmployee(int id) {
        try
        {
            //checkAuth("ViewEmployees");
            return Response.makeSuccess(employeeController.getEmployee(id));
        } catch (Exception e) {
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response<List<Employee>> getEmployees() {
        try
        {
            //checkAuth("ViewEmployees");

            return Response.makeSuccess(employeeController.getEmployees());
        } catch (Exception e) {
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response<Employee> addEmployee(int id, @NotNull String name, @NotNull BankAccountDetails bankAccountDetails,
                                          double salary, @NotNull LocalDateTime workStartingDate, @NotNull String workingConditionsDescription) {
        try
        {
            checkAuth("ManageEmployees");

            return Response.makeSuccess(employeeController.addEmployee(id, name, bankAccountDetails, salary, workStartingDate, workingConditionsDescription));
        } catch (Exception e)
        {
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response<Employee> removeEmployee(int id) {
        try
        {
            checkAuth("ManageEmployees");
            Response<Boolean> r1 = employeeHasQualification(id, "Driver");
            if(r1.isSuccess() && r1.getData()) {
                Response r2 = deliveryService.deleteDriver(id);
                if (!r2.isSuccess())
                    return Response.makeFailure(r2.getMessage());
            }
            return Response.makeSuccess(employeeController.removeEmployee(id));
        } catch (Exception e)
        {
            return Response.makeFailure(e.getMessage());
        }
    }



    public Response<String> updateEmployeeName(int id, @NotNull String newName) {
        // added so each employee can change their own name
        try {
            // check if not updating myself
            if(loggedEmployeeId != id) {
                checkAuth("ManageEmployees");
            }

            // Check if updating other services is needed
            Response<Boolean> r1 = employeeHasQualification(id, "Driver");
            if(!r1.isSuccess()) {
                return Response.makeFailure(r1.getMessage());
            }
            // if is driver
            if(r1.getData()) {
                Response r2 = deliveryService.editDriverName(id, newName);
                if(!r2.isSuccess()) {
                    return r2;
                }
            }

            return Response.makeSuccess(employeeController.updateEmployeeName(id, newName));
        } catch (Exception e) {
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response<Double> updateEmployeeSalary(int id, double newSalary) {
        try
        {
            checkAuth("ManageEmployees");

            return Response.makeSuccess(employeeController.updateEmployeeSalary(id, newSalary));
        } catch (Exception e) {
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response<BankAccountDetails> updateEmployeeBankAccountDetails(int id, @NotNull BankAccountDetails newBankAccountDetails) {
        try
        {
            checkAuth("ManageEmployees");

            return Response.makeSuccess(employeeController.updateEmployeeBankAccountDetails(id, newBankAccountDetails));
        } catch (Exception e) {
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response<TimeInterval> employeeAddWorkingHour(int id, @NotNull LocalDateTime start, @NotNull LocalDateTime end)
    {
        try
        {
            // added so each employee can add their hours
            if (loggedEmployeeId != id)
            {
                checkAuth("ManageEmployees");
            }
            return Response.makeSuccess(employeeController.employeeAddWorkingHour(id, start, end));
        } catch (Exception e) {
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response<TimeInterval> employeeRemoveWorkingHour(int id, @NotNull LocalDateTime start) {
        try
        {
            // added so each employee can remove their hours
            if (loggedEmployeeId != id)
            {
                checkAuth("ManageEmployees");
            }

            return Response.makeSuccess(employeeController.employeeRemoveWorkingHour(id, start));
        } catch (Exception e) {
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response<Qualification> employeeAddQualification(int id, Qualification qualification)
    {
        try {
            checkAuth("ManageEmployees");

            if(qualification.getName().equals("Driver")) {
                return Response.makeFailure("Cannot make an employee a driver. ");
            }

            return Response.makeSuccess(employeeController.employeeAddQualification(id, qualification));
        } catch (Exception e) {
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response<Qualification> employeeRemoveQualification(int id, @NotNull String name) {
        try
        {
            checkAuth("ManageEmployees");

            if(name.equals("Driver")) {
                return Response.makeFailure("Cannot remove the driver qualification. ");
            }

            Qualification toRemove = qualificationController.getQualification(name);
            employeeController.employeeRemoveQualification(id, name);
            return Response.makeSuccess(toRemove);
        } catch (Exception e) {
            return Response.makeFailure(e.getMessage());
        }
    }

    // -----------------------------

    // SHIFTS FUNCTIONS

    public Response<List<Shift>> getShifts(int branchId) {
        try
        {
            //checkAuth("ManageBranch" + branchId);

            return Response.makeSuccess(shiftController.getShifts(branchId));
        } catch (Exception e) {
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response<Shift> addShift(int branchId, @NotNull LocalDateTime date, @NotNull Employee shiftManager,
                                    @NotNull Map<Integer, List<String>> workers, @NotNull ShiftTime shiftTime) {
        try
        {
            checkAuth("ManageShift");

            return Response.makeSuccess(shiftController.addShift(branchId, date, shiftManager, workers, shiftTime));
        } catch (Exception e) {
            return Response.makeFailure(e.getMessage());
        }
    }

    public void debugAddTodayShift() throws Exception {
        try{
            int branchId = 0;
            ShiftTime st = ShiftTime.DAY;
            Employee shiftManager = employeeController.getEmployee(1);
            Arrays.asList("Cashier");
            LocalDate date = LocalDate.now().plusDays(1);
            LocalDateTime dt = LocalDateTime.parse(date + "T06:00:00");
            HashMap<Integer, List<String>> workersDAY = new HashMap<Integer, List<String>>(){{
                put(1, Arrays.asList("Cashier"));
                put(2, Arrays.asList("WarehouseWorker"));
                put(3, Arrays.asList("StockClerk"));
                put(4, Arrays.asList("Driver"));
                put(5, Arrays.asList("Branch0Manager"));
                put(6, Arrays.asList("InventoryManager"));
            }};
            HashMap<Integer, List<String>> workersNIGHT = new HashMap<Integer, List<String>>(){{
                put(1, Arrays.asList("Cashier"));
                put(2, Arrays.asList("WarehouseWorker"));
                put(3, Arrays.asList("StockClerk"));
                put(4, Arrays.asList("Driver"));
            }};
            for (LocalDateTime i = dt; i.isBefore(dt.plusDays(30)); i = i.plusDays(1)){
                addShift(branchId, i, shiftManager, workersDAY, ShiftTime.DAY);
                addShift(branchId, i, shiftManager, workersNIGHT, ShiftTime.NIGHT);
            }
        }
        catch (Exception ignored){}
    }

    public Response<Shift> removeShift(@NotNull ShiftId shiftId){
        try
        {
            checkAuth("ManageShift");

            return Response.makeSuccess(shiftController.removeShift(shiftId));
        } catch (Exception e) {
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response<Employee> addWorker(@NotNull ShiftId shiftId, @NotNull Employee worker, @NotNull List<String> qualifications){
        try
        {
            checkAuth("ManageShift");

            return Response.makeSuccess(shiftController.addWorker(shiftId, worker, qualifications));
        } catch (Exception e) {
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response<Employee> removeWorker(@NotNull ShiftId shiftId, @NotNull Employee worker){
        try
        {
            checkAuth("ManageShift");

            return Response.makeSuccess(shiftController.removeWorker(shiftId, worker));
        } catch (Exception e) {
            return Response.makeFailure(e.getMessage());
        }
    }

    // -----------------------------

    // SHIFTS FUNCTIONS

    public Response<Qualification> getQualification(String name) {
        try
        {
            //checkAuth("ViewQualifications");

            return Response.makeSuccess(qualificationController.getQualification(name));
        } catch (Exception e) {
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response<List<Qualification>> getQualifications() {
        try
        {
            //checkAuth("ViewQualifications");

            return Response.makeSuccess(qualificationController.getQualifications());
        } catch (Exception e) {
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response<Qualification> addQualification(@NotNull String name){
        try
        {
            checkAuth("ManageQualifications");

            return Response.makeSuccess(qualificationController.addQualification(name));
        } catch (Exception e) {
            return Response.makeFailure(e.getMessage());
        }
    }

//    public Response<Qualification> renameQualification(@NotNull String name, @NotNull String newName){
//        Response<String> r = checkAuth("ManageQualifications");
//
//        return qualificationController.renameQualification(name, newName);
//    }
    public Response<Qualification> removeQualification(@NotNull String name) {
        try
        {
            checkAuth("ManageQualifications");

            Qualification removedQualification = qualificationController.removeQualification(name);
            List<Employee> employees = employeeController.getEmployees();
            employeeController.removeQualificationForAll(name);
            shiftController.removeQualification(name);
            return Response.makeSuccess(removedQualification);
        } catch (Exception e) {
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response<List<Permission>> getPermissions() {
        try
        {
            //checkAuth("ViewQualifications");

            return Response.makeSuccess(qualificationController.getPermissions());
        } catch (Exception e) {
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response<Permission> addPermission(@NotNull String name){
        try
        {
            checkAuth("ManageQualifications");

            return Response.makeSuccess(qualificationController.addPermission(name));
        } catch (Exception e) {
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response<Permission> removePermission(@NotNull String name)
    {
        try {
            checkAuth("ManageQualifications");

            return Response.makeSuccess(qualificationController.removePermission(name));
        } catch (Exception e) {
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response<Qualification> addPermissionToQualification(String permName, String qualName){
        try
        {
            checkAuth("ManageQualifications");

            return Response.makeSuccess(qualificationController.addPermissionToQualification(permName, qualName));
        } catch (Exception e) {
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response<Qualification> removePermissionFromQualification(String permName, String qualName){
        try
        {
            checkAuth("ManageQualifications");

            return Response.makeSuccess(qualificationController.removePermissionFromQualification(permName, qualName));
        } catch (Exception e) {
            return Response.makeFailure(e.getMessage());
        }
    }

    // -----------------------------

    // HR Functions

    public Response<Map<Employee, int[]>> getEmployeesWithQualification(int branchId, Qualification qualification) {
        try
        {
            //checkAuth("ManageBranch" + branchId);

            Map<Employee, int[]> m = new HashMap<>();

            // using this to bypass authentication
            List<Employee> employees = employeeController.getEmployees();

            // init zeros
            for (Employee e : employees)
            {
                if (e.getWorkingConditions().getQualifications().contains(qualification.getName()))
                {
                    int[] a = {0, 0};
                    m.put(e, a);
                }
            }

            Response<List<Shift>> r3 = getShifts(branchId);
            if (!r3.isSuccess())
            {
                return Response.makeFailure(r3.getMessage());
            }

            for (Shift s : r3.getData())
            {
                for (Integer employeeId : s.getWorkers().keySet())
                {
                    Employee employee = employeeController.getEmployee(employeeId);
                    int[] a = m.get(employee);
                    if (a!=null && s.getId().getShiftTime() == ShiftTime.DAY)
                    {
                        // DAY
                        a[0]++;
                    }
                    else if(a!=null)
                    {
                        // NIGHT
                        a[1]++;
                    }
                }
            }

            // Uncomment to remove workers that didn't work yet.
//        Iterator<Employee> i = employees.iterator();
//        while(i.hasNext()) {
//            Employee e = i.next();
//            int[] a = m.get(e);
//            if(a[0] == 0 && a[1] == 0) {
//                m.remove(e);
//                i.remove();
//            }
//        }

            return Response.makeSuccess(m);
        } catch (Exception e) {
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response<Boolean> employeeHasQualification(int id, String qualName) {
        try {
            return Response.makeSuccess(employeeController.getEmployee(id).getWorkingConditions().getQualifications().contains(qualName));
        } catch(Exception e) {
            return Response.makeFailure(e.getMessage());
        }
    }

    // -----------------------------

    // DeliverySystem Functions
    public Response<DeliveryService> getDeliveryService() {
        return Response.makeSuccess(this.deliveryService);
    }

    public Response<Qualification> driverAddQualification(int id, String licenseType) {
        try
        {
            checkAuth("ManageEmployees");

            Response<Employee> r1 = this.getEmployee(id);
            if(!r1.isSuccess()) {
                return Response.makeFailure(r1.getMessage());
            }
            Employee e = r1.getData();


            Response r2 = this.deliveryService.addDriver(e.getId(), e.getName(), licenseType);
            if(!r2.isSuccess()) {
                return r2;
            }

            Qualification driverQualification = qualificationController.getQualification("Driver");
            Qualification q = employeeController.employeeAddQualification(id, driverQualification);

            return Response.makeSuccess(q);
        } catch (Exception e)
        {
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response<Qualification> driverRemoveQualification(int id) {
        try {
            checkAuth("ManageEmployees");

            Response<Employee> r1 = this.getEmployee(id);
            if(!r1.isSuccess()) {
                return Response.makeFailure(r1.getMessage());
            }
            Employee e = r1.getData();


            Response r2 = this.deliveryService.deleteDriver(id);
            if(!r2.isSuccess()) {
                return r2;
            }

            String s = employeeController.employeeRemoveQualification(id, "Driver");

            Response<Qualification> r3 = this.getQualification(s);
            if(!r3.isSuccess()) {
                return r3;
            }

            return Response.makeSuccess(r3.getData());
        } catch(Exception e) {
            return Response.makeFailure(e.getMessage());
        }
    }

    public Response<Boolean> driverAvailableOnShift(LocalDateTime shiftDate, int employeeId)
    {
        try {
            Shift dayShift = shiftController.getEmployeeShiftOnDay(employeeId, shiftDate, ShiftTime.DAY);
            Shift nightShift = shiftController.getEmployeeShiftOnDay(employeeId, shiftDate, ShiftTime.NIGHT);
            //if(dayShift == null && nightShift == null) {
            //    return Response.makeSuccess(true);
            //}
            //return Response.makeSuccess(false);
            if(dayShift != null || nightShift != null){
                return Response.makeSuccess(true);
            }
            return Response.makeSuccess(false);
        } catch(Exception e) {
            return Response.makeFailure(e.getMessage());
        }
    }
    // -----------------------------

    // DB functions
    public void clearDatabases()
    {
        employeeController.clearDatabases();
        shiftController.clearDatabases();
        qualificationController.clearDatabases();
    }

    // Permission checks
    public Response<Boolean> canManageEmployees() {
        try {
            checkAuth("ManageEmployees");

            return Response.makeSuccess(true);
        } catch(Exception e) {
            return Response.makeSuccess(false);
        }
    }

    public Response<Boolean> canViewEmployees() {
        try {
            checkAuth("ViewEmployees");

            return Response.makeSuccess(true);
        } catch(Exception e) {
            return Response.makeSuccess(false);
        }
    }

    public Response<Boolean> canManageShift() {
        try {
            checkAuth("ManageShift");

            return Response.makeSuccess(true);
        } catch(Exception e) {
            return Response.makeSuccess(false);
        }
    }

    public Response<Boolean> canManageQualifications() {
        try {
            checkAuth("ManageQualifications");

            return Response.makeSuccess(true);
        } catch(Exception e) {
            return Response.makeSuccess(false);
        }
    }

    public Response<Boolean> canViewQualifications() {
        try {
            checkAuth("ViewQualifications");

            return Response.makeSuccess(true);
        } catch(Exception e) {
            return Response.makeSuccess(false);
        }
    }

    public Response<Boolean> canManageDeliveries() {
        try {
            checkAuth("ManageDeliveries");

            return Response.makeSuccess(true);
        } catch(Exception e) {
            return Response.makeSuccess(false);
        }
    }

    public Response<Boolean> canManageInventory() {
        try {
            checkAuth("ManageInventory");

            return Response.makeSuccess(true);
        } catch(Exception e) {
            return Response.makeSuccess(false);
        }
    }

    public Response<Boolean> canViewInventory() {
        try {
            checkAuth("ViewInventory");

            return Response.makeSuccess(true);
        } catch(Exception e) {
            return Response.makeSuccess(false);
        }
    }

    public Response<Boolean> canViewDeliveries() {
        try {
            checkAuth("ViewDeliveries");

            return Response.makeSuccess(true);
        } catch(Exception e) {
            return Response.makeSuccess(false);
        }
    }

    public Response<Boolean> canViewSuppliers() {
        try {
            checkAuth("ViewSuppliers");

            return Response.makeSuccess(true);
        } catch(Exception e) {
            return Response.makeSuccess(false);
        }
    }

    public Response<Boolean> canManageOrders() {
        try {
            checkAuth("ManageOrders");

            return Response.makeSuccess(true);
        } catch(Exception e) {
            return Response.makeSuccess(false);
        }
    }


    // SI Service functions
    public Response<SIService> getSIService() {
        return Response.makeSuccess(this.siService);
    }
}
