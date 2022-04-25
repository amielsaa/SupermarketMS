package BusinessLayer;

import Utilities.LegalTimeException;


import java.time.LocalDateTime;

public class TimeInterval
{
    private LocalDateTime start;
    private LocalDateTime end;

    protected TimeInterval(LocalDateTime start, LocalDateTime end) throws LegalTimeException {
        if(start.isAfter(end)){
            throw new LegalTimeException("The start of the interval can't be after the end");
        }
        this.start = start;
        this.end = end;
    }

    public LocalDateTime getStart() {
        return start;
    }

    protected void setStart(LocalDateTime start) throws LegalTimeException {
        if(start.isAfter(end)){
            throw new LegalTimeException("The start of the interval can't be after the end");
        }
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    protected void setEnd(LocalDateTime end) throws LegalTimeException {
        if(start.isAfter(end)){
            throw new LegalTimeException("The start of the interval can't be after the end");
        }
        this.end = end;
    }

    @Override
    public String toString() {
        return "TimeInterval{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }

    public boolean isOverlapping(TimeInterval other){

        if(this.getEnd().isBefore(other.getStart()) || this.getEnd().isEqual(other.getEnd())){
            return false;
        }
        else return !other.getEnd().isBefore(this.getStart()) && !other.getEnd().isEqual(this.getEnd());
    }

}
