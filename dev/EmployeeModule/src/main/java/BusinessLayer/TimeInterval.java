package BusinessLayer;

import Utilities.LegalTimeException;
import com.sun.istack.internal.NotNull;

import java.time.LocalDateTime;

public class TimeInterval
{
    private LocalDateTime start;
    private LocalDateTime end;

    //TODO: check null inputs

    public TimeInterval(@NotNull LocalDateTime start, @NotNull LocalDateTime end) throws LegalTimeException {
        if(start.isAfter(end)){
            throw new LegalTimeException("The start of the interval can't be after the end");
        }
        this.start = start;
        this.end = end;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(@NotNull LocalDateTime start) throws LegalTimeException {
        if(start.isAfter(end)){
            throw new LegalTimeException("The start of the interval can't be after the end");
        }
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(@NotNull LocalDateTime end) throws LegalTimeException {
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
