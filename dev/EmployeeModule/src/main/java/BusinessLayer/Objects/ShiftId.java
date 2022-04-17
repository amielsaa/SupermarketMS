package BusinessLayer.Objects;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

public class ShiftId
{
    private int branchId;
    private LocalDateTime date;
    private ShiftTime time;

    public ShiftId(int _branchId, @NotNull LocalDateTime _date, @NotNull ShiftTime _time){
        branchId = _branchId;
        date = _date;
        time = _time;
    }

    public int getBranchId() {
        return branchId;
    }

    /**
     *
     * @return date of the shift
     */
    public LocalDateTime getDate() {
        return date;
    }

    /**
     *
     * @return time of day (DAY, NIGHT)
     */
    public ShiftTime getShiftTime() {
        return time;
    }
}
