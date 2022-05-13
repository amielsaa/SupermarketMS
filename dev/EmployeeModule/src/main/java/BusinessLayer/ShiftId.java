package BusinessLayer;



import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class ShiftId
{
    private int branchId;
    private LocalDateTime date;
    private ShiftTime time;

    public ShiftId(int _branchId, LocalDateTime _date, ShiftTime _time){
        branchId = _branchId;
        date = LocalDateTime.parse(_date.toLocalDate().toString() + "T06:00:00");
        //date = _date;
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

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShiftId shiftId = (ShiftId) o;
        return branchId == shiftId.branchId && date.getDayOfYear() == shiftId.date.getDayOfYear() && date.getYear() == shiftId.date.getYear() && time == shiftId.time;
    }

    @Override
    public String toString() {
        return "ShiftId{" +
                "branchId=" + branchId +
                ", date=" + date +
                ", time=" + time +
                '}';
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(branchId, date, time);
    }
}
