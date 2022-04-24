package BusinessLayer;

import BusinessLayer.*;
import DataAccessLayer.DALController;
import Utilities.Response;


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

    public Response<List<Shift>> getAllShifts() {
        return Response.makeSuccess(shifts.values().stream().toList());
    }

    public Response<List<Shift>> getShifts(int branchId) {
        List<Shift> sList = getAllShifts().getData();
        Iterator i = sList.iterator();
        while(i.hasNext()) {
            Shift s = (Shift)i.next();
            if(s.getId().getBranchId() != branchId) {
                i.remove();
            }
        }
        return Response.makeSuccess(sList);
    }

    public Response<Shift> addShift(int branchId, LocalDateTime date, Employee shiftManager,
                                    Map<Employee, List<Qualification>> workers, ShiftTime shiftTime){
        // using for each for reasons...
        ShiftId newShift = new ShiftId(branchId, date, shiftTime);
        for(ShiftId s : shifts.keySet()) {
            if(s.equals(newShift))
                return Response.makeFailure("this shift already exists");
        }
//        if(shifts.containsKey(new ShiftId(branchId, date, shiftTime))){
//            return Response.makeFailure("this shift already exists");
//        }
        if(!date.isAfter(LocalDateTime.now().plusDays(1))){
            //Won't be allowed to add a shift a day after
            return Response.makeFailure("illegal starting time");
        }
        ShiftId shiftId = newShift;
        Shift shift = new Shift(shiftId, workers, shiftManager);
        this.shifts.put(shiftId, shift);
        return Response.makeSuccess(shift);
        //TODO: update after implementing DAL, Employee
    }

    public Response<Shift> removeShift(ShiftId shiftId){
        //TODO: check if it should be possible to remove today's shift
        //TODO: update after implementing Response, DAL, Employee
        if(!shiftId.getDate().isAfter(LocalDateTime.now().plusDays(1))){
            //Won't be allowed to remove a shift a day after
            return Response.makeFailure("too late to remove");
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
            return Response.makeFailure("No shift with such id. ");
        }
//        if(!shifts.containsKey(shiftId)){
//            return Response.makeFailure("no shift with such id");
//        }
        Shift toRemove = shifts.remove(toRemoveId);
        return Response.makeSuccess(toRemove);
    }

    public Response<Shift> getShift(ShiftId shiftId){
        //TODO: update after implementing Response, DAL, Employee
        for(ShiftId s : shifts.keySet()) {
            if(s.equals(shiftId))
                return Response.makeSuccess(shifts.get(s));
        }
//        if(!shifts.containsKey(shiftId)){
//            return Response.makeFailure("no shift with such id");
//        }
        return Response.makeFailure("no shift with such id");
    }


    public Response<Employee> addWorker(ShiftId shiftId, Employee worker, List<Qualification> qualifications){
        Response<Shift> res = getShift(shiftId);
        if(!res.isSuccess()){
            return Response.makeFailure("no such shift");
        }
        Shift shift = res.getData();
        if(shift.getWorkers().containsKey(worker)){
            return Response.makeFailure("that employee is already in");
        }
        if(qualifications.isEmpty()){
            return Response.makeFailure("can't add an employee without any role");
        }
        shift.addWorker(worker, qualifications);
        return Response.makeSuccess(worker);
    }

    public Response<Employee> removeWorker(ShiftId shiftId, Employee worker){
        Response<Shift> res = getShift(shiftId);
        if(!res.isSuccess()){
            return Response.makeFailure("no such shift");
        }
        Shift shift = res.getData();
        if(!shift.getWorkers().containsKey(worker)){
            return Response.makeFailure("that employee isn't on this shift");
        }
        shift.removeWorker(worker);
        return Response.makeSuccess(worker);
    }

}
