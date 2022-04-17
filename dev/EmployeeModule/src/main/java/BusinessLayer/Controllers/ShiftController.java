package BusinessLayer.Controllers;

import BusinessLayer.Objects.*;
import DataAccessLayer.DALController;
import Utilities.Response;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShiftController
{
    //TODO: check if Map from shiftId to Shift is needed
    public Map<ShiftId, Shift> shifts;

    //TODO: decide if it is needed to check if the inputs are null
    //TODO: decide when it shouldn't be possible to change the shift
    public ShiftController(@NotNull DALController dalController) {
        dalController.execute();
        shifts = new HashMap<>();
        // TODO: implement getting data from DAL
    }

    public Response<Shift> addShift(int branchId, @NotNull LocalDateTime date, @NotNull Employee shiftManager,
                                    @NotNull Map<Employee, List<Qualification>> workers, @NotNull ShiftTime shiftTime){
        if(shifts.containsKey(new ShiftId(branchId, date, shiftTime))){
            return Response.makeFailure("this shift already exists");
        }
        if(date.isAfter(LocalDateTime.now().plusDays(1))){
            //Won't be allowed to add a shift a day after
            return Response.makeFailure("illegal starting time");
        }
        ShiftId shiftId = new ShiftId(branchId, date, shiftTime);
        Shift shift = new Shift(shiftId, workers, shiftManager);
        return Response.makeSuccess(shift);
        //TODO: update after implementing DAL, Employee
    }

    public Response<Shift> removeShift(@NotNull ShiftId shiftId){
        //TODO: check if it should be possible to remove today's shift
        //TODO: update after implementing Response, DAL, Employee
        if(shiftId.getDate().isAfter(LocalDateTime.now().plusDays(1))){
            //Won't be allowed to remove a shift a day after
            return Response.makeFailure("too late to remove");
        }
        if(!shifts.containsKey(shiftId)){
            return Response.makeFailure("no shift with such id");
        }
        Shift toRemove = shifts.remove(shiftId);
        return Response.makeSuccess(toRemove);
    }

    public Response<Shift> getShift(@NotNull ShiftId shiftId){
        //TODO: update after implementing Response, DAL, Employee
        if(!shifts.containsKey(shiftId)){
            return Response.makeFailure("no shift with such id");
        }
        return Response.makeSuccess(shifts.get(shiftId));
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
