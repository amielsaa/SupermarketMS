package Employee.BusinessLayer;

import Employee.DataAccessLayer.ShiftDAO;
import Utilities.Exceptions.*;


import java.time.LocalDateTime;
import java.util.*;

public class ShiftController
{

    public Map<ShiftId, Shift> shifts;
    private ShiftDAO sDao;

    public ShiftController() {
        sDao = new ShiftDAO("Shifts");
        shifts = new HashMap<>();
    }

    public void clearCache(){
        sDao.clearCache();
    }

    public List<Shift> getShifts(int branchId) throws DatabaseAccessException
    {
        List<Shift> sList = sDao.Read(branchId);
        if(sList == null){
            throw new DatabaseAccessException("Failed to load all shifts from branch " + branchId + " database");
        }
        return sList;
    }

    public Shift getEmployeeShiftOnDay(int eid, LocalDateTime date, ShiftTime shiftTime) throws Exception {
        List<Shift> sList = sDao.ReadAll();
        if(sList == null){
            throw new DatabaseAccessException("Failed to load all shifts from database");
        }
        for(Shift s : sList) {
            // TODO fix date comparison between shifts to be internal in ShiftId
            if(s.getId().getShiftTime() == shiftTime &&
                    date.getDayOfYear() == s.getId().getDate().getDayOfYear() && date.getYear() == s.getId().getDate().getYear() &&
                    s.getWorkers().containsKey(eid)) {
                return s;
            }
        }
        return null;
    }

    public Shift addShift(int branchId, LocalDateTime date, Employee shiftManager,
                                    Map<Integer, List<String>> workers, ShiftTime shiftTime) throws Exception {
        /*
        // using for each for reasons...

        for(ShiftId s : shifts.keySet()) {
            if(s.equals(newShift))
                throw new ObjectAlreadyExistsException("this shift already exists");
        }
        */
//        if(shifts.containsKey(new ShiftId(branchId, date, shiftTime))){
//            return Response.makeFailure("this shift already exists");
//        }
        if(!date.isAfter(LocalDateTime.now().plusDays(1))){
            //Won't be allowed to add a shift a day after
            throw new LegalTimeException("illegal starting time");
        }
        ShiftId shiftId = new ShiftId(branchId, date, shiftTime);
        Shift existingShift = sDao.Read(shiftId);
        if(existingShift != null){
            throw new ObjectAlreadyExistsException("this shift already exists");
        }
        Shift shift = new Shift(shiftId, workers, shiftManager.getId());
        boolean response = sDao.Create(shift);
        if(!response){
            throw new DatabaseAccessException("Failed to insert a new employee to the database");
        }
        return shift;
    }

    public Shift removeShift(ShiftId shiftId) throws Exception
    {
        //TODO: check if it should be possible to remove today's shift
        //TODO: update after implementing Response, DAL, Employee
        if(!shiftId.getDate().isAfter(LocalDateTime.now().plusDays(1))){
            //Won't be allowed to remove a shift a day after
            throw new LegalTimeException("too late to remove");
        }
        /*
        ShiftId toRemoveId = null;
        for(ShiftId s : shifts.keySet()) {
            if(s.equals(shiftId))
            {
                toRemoveId = s;
                break;
            }
        }
        */
        Shift toRemove = sDao.Read(shiftId);
        if(toRemove == null) {
            throw new ObjectNotFoundException("No shift with such id. ");
        }
        if(!sDao.Delete(shiftId)){
            throw new DatabaseAccessException("Failed to remove shift from database");
        }
//        if(!shifts.containsKey(shiftId)){
//            throew new ObjectNotFoundException("no shift with such id");
//        }
        return toRemove;
    }

    public Shift getShift(ShiftId shiftId) throws Exception
    {
        Shift shift = sDao.Read(shiftId);
        if(shift == null){
            throw new ObjectNotFoundException("no shift with such id");
        }
        return shift;
        /*
        for(ShiftId s : shifts.keySet()) {
            if(s.equals(shiftId))
                return shifts.get(s);
        }
//        if(!shifts.containsKey(shiftId)){
//            throw new ObjectNotFoundException("no shift with such id");
//        }
        */
    }


    public Employee addWorker(ShiftId shiftId, Employee worker, List<String> qualifications) throws Exception
    {
        Shift shift = getShift(shiftId);
        if(shift.getWorkers().containsKey(worker.getId())){
            throw new ObjectAlreadyExistsException("that employee is already in");
        }
        if(qualifications.isEmpty()){
            throw new IllegalObjectException("can't add an employee without any role");
        }
        if(!sDao.addWorker(shiftId, worker.getId(), qualifications)){
            throw new DatabaseAccessException("Failed to add a worker into the shift in database");
        }
        shift.addWorker(worker, qualifications);
        return worker;
    }

    public Employee removeWorker(ShiftId shiftId, Employee worker) throws Exception
    {
        Shift shift = getShift(shiftId);
        if(!shift.getWorkers().containsKey(worker.getId())){
            throw new ObjectNotFoundException("that employee isn't on this shift");
        }
        if(!sDao.removeWorker(worker.getId(), shiftId)){
            throw new DatabaseAccessException("Failed to add a worker into the shift in database");
        }
        shift.removeWorker(worker);
        return worker;
    }

    public boolean removeQualification(String name){
       List<Shift> shiftsList = sDao.ReadAll();
       for (Shift shift : shiftsList) {
           if(!sDao.DeleteQualification(name)){
               return false;
           }
       }
       return true;
    }

    public void clearDatabases(){
        sDao.DeleteAll();
    }

}
