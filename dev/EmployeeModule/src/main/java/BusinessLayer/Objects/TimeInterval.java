package BusinessLayer.Objects;

import java.time.LocalDateTime;

public class TimeInterval
{
    // TODO IMPLEMENT

    private LocalDateTime start;
    private LocalDateTime end;

    protected TimeInterval(LocalDateTime start, LocalDateTime end) {
        if(start.isAfter(end)) {
            throw new IllegalArgumentException("Start argument cannot be later than end argument. ");
        }
        this.start = start;
        this.end = end;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public boolean isOverlapping(TimeInterval other) {
        // TODO IMPLEMENT
        return true;
    }

    public boolean isOverlapping(LocalDateTime time) {
        // TODO IMPLEMENT
        return true;
    }

    @Override
    public String toString()
    {
        return "TimeInterval{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }
}
