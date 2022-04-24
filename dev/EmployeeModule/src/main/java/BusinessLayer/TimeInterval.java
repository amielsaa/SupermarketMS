package BusinessLayer;

import Utilities.LegalTimeException;
import org.jetbrains.annotations.NotNull;
import java.time.LocalDateTime;

public class TimeInterval
{
    private LocalDateTime start;
    private LocalDateTime end;

    protected TimeInterval(@NotNull LocalDateTime start, @NotNull LocalDateTime end) throws LegalTimeException {
        if(start.isAfter(end)){
            throw new LegalTimeException("The start of the interval can't be after the end");
        }
        this.start = start;
        this.end = end;
    }

    public LocalDateTime getStart() {
        return start;
    }

    protected void setStart(@NotNull LocalDateTime start) throws LegalTimeException {
        if(start.isAfter(end)){
            throw new LegalTimeException("The start of the interval can't be after the end");
        }
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    protected void setEnd(@NotNull LocalDateTime end) throws LegalTimeException {
        if(start.isAfter(end)){
            throw new LegalTimeException("The start of the interval can't be after the end");
        }
        this.end = end;
    }

    public boolean isOverlapping(@NotNull TimeInterval other){

        if(this.getEnd().isBefore(other.getStart()) || this.getEnd().isEqual(other.getEnd())){
            return false;
        }
        else return !other.getEnd().isBefore(this.getStart()) && !other.getEnd().isEqual(this.getEnd());
    }

}
