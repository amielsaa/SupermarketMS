package EmployeeModule.BusinessLayer;



import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Shift
{
    private ShiftId id;
    private Map<Integer, List<String>> workers;
    private Integer shiftManager;

    public Shift(ShiftId _id, Map<Integer, List<String>> _workers, Integer _shiftManager){
        id = _id;
        workers = _workers;
        shiftManager = _shiftManager;
    }

    public ShiftId getId() {
        return id;
    }

    public Map<Integer, List<String>> getWorkers() {
        return Collections.unmodifiableMap(workers);
    }

    public Integer getShiftManager() {
        return shiftManager;
    }

    @Override
    public String toString() {
        return "Shift{" +
                "id=" + id +
                ", workers=" + workers +
                ", shiftManager=" + shiftManager +
                '}';
    }

    protected void addWorker(Employee employee, List<String> qualifications){
        workers.put(employee.getId(), qualifications);
    }

    protected void removeWorker(Employee employee){
        workers.remove(employee.getId());
    }
}
