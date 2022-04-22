package BusinessLayer;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Shift
{
    private ShiftId id;
    private Map<Employee, List<Qualification>> workers;
    private Employee shiftManager;

    public Shift(@NotNull ShiftId _id, @NotNull Map<Employee, List<Qualification>> _workers, @NotNull Employee _shiftManager){
        id = _id;
        workers = _workers;
        shiftManager = _shiftManager;
    }

    public ShiftId getId() {
        return id;
    }

    public Map<Employee, List<Qualification>> getWorkers() {
        return Collections.unmodifiableMap(workers);
    }

    public Employee getShiftManager() {
        return shiftManager;
    }

    protected void addWorker(@NotNull Employee employee, List<Qualification> qualifications){
        workers.put(employee, qualifications);
    }

    protected void removeWorker(@NotNull Employee employee){
        workers.remove(employee);
    }
}
