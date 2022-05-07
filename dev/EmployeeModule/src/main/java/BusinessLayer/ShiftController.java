package BusinessLayer;

import BusinessLayer.*;
import DataAccessLayer.DALController;
import Utilities.*;


import java.time.LocalDateTime;
import java.util.*;

public class ShiftController
{

    public Map<ShiftId, Shift> shifts;

    //TODO: decide when it shouldn't be possible to change the shift
    public ShiftController(DALController dalController) {
        dalController.execute();
        shifts = new HashMap<>();
        // TODO: implement getting data from DAL
    }

    public List<Shift> getAllShifts() {
        List l = new ArrayList(shifts.values());
        return l;
    }

    public List<Shift> getShifts(int branchId) {
        List<Shift> sList = getAllShifts();
        Iterator i = sList.iterator();
        while(i.hasNext()) {
            Shift s = (Shift)i.next();
            if(s.getId().getBranchId() != branchId) {
                i.remove();
            }
        }
        return sList;
    }

    public Shift addShift(int branchId, LocalDateTime date, Employee shiftManager,
                                    Map<Employee, List<Qualification>> workers, ShiftTime shiftTime) throws Exception
    {
        // using for each for reasons...
        ShiftId newShift = new ShiftId(branchId, date, shiftTime);
        for(ShiftId s : shifts.keySet()) {
            if(s.equals(newShift))
                throw new ObjectAlreadyExistsException("this shift already exists");
        }
//        if(shifts.containsKey(new ShiftId(branchId, date, shiftTime))){
//            return Response.makeFailure("this shift already exists");
//        }
        if(!date.isAfter(LocalDateTime.now().plusDays(1))){
            //Won't be allowed to add a shift a day after
            throw new LegalTimeException("illegal starting time");
        }
        ShiftId shiftId = newShift;
        Shift shift = new Shift(shiftId, workers, shiftManager);
        this.shifts.put(shiftId, shift);
        return shift;
        //TODO: update after implementing DAL, Employee
    }

    public Shift removeShift(ShiftId shiftId) throws Exception
    {
        //TODO: check if it should be possible to remove today's shift
        //TODO: update after implementing Response, DAL, Employee
        if(!shiftId.getDate().isAfter(LocalDateTime.now().plusDays(1))){
            //Won't be allowed to remove a shift a day after
            throw new LegalTimeException("too late to remove");
        }
        ShiftId toRemoveId = null;
        for(ShiftId s : shifts.keySet()) {
            if(s.equals(shiftId))
            {
                toRemoveId = s;
                break;
            }
        }
        if(toRemoveId == null) {
            throw new ObjectNotFoundException("No shift with such id. ");
        }
//        if(!shifts.containsKey(shiftId)){
//            throew new ObjectNotFoundException("no shift with such id");
//        }
        Shift toRemove = shifts.remove(toRemoveId);
        return toRemove;
    }

    public Shift getShift(ShiftId shiftId) throws Exception
    {
        //TODO: update after implementing Response, DAL, Employee
        for(ShiftId s : shifts.keySet()) {
            if(s.equals(shiftId))
                return shifts.get(s);
        }
//        if(!shifts.containsKey(shiftId)){
//            throw new ObjectNotFoundException("no shift with such id");
//        }
        throw new ObjectNotFoundException("no shift with such id");
    }


    public Employee addWorker(ShiftId shiftId, Employee worker, List<Qualification> qualifications) throws Exception
    {
        Shift shift = getShift(shiftId);
        if(shift.getWorkers().containsKey(worker)){
            throw new ObjectAlreadyExistsException("that employee is already in");
        }
        if(qualifications.isEmpty()){
            throw new IllegalObjectException("can't add an employee without any role");
        }
        shift.addWorker(worker, qualifications);
        return worker;
    }

    public Employee removeWorker(ShiftId shiftId, Employee worker) throws Exception
    {
        Shift shift = getShift(shiftId);
        if(!shift.getWorkers().containsKey(worker)){
            throw new ObjectNotFoundException("that employee isn't on this shift");
        }
        shift.removeWorker(worker);
        return worker;
    }

}
