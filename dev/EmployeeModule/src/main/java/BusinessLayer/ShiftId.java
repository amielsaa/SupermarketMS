package BusinessLayer;

import com.sun.istack.internal.NotNull;

import java.time.LocalDateTime;

public class ShiftId
{
    private int branchId;
    private LocalDateTime date;
    private ShiftTime time;

    public ShiftId(@NotNull int _branchId, @NotNull LocalDateTime _date, @NotNull ShiftTime _time){
        branchId = _branchId;
        date = _date;
        time = _time;
    }

    public int getBranchId() {
        return branchId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public ShiftTime getTime() {
        return time;
    }
}
