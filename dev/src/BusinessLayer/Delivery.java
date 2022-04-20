package BusinessLayer;
import java.time.LocalDateTime;
import java.util.Collection;

public class Delivery {
    private int id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int weight;
    private Truck truck;
    private Site origin;
    //to be implemented, destinations
    public Delivery(int id, LocalDateTime startTime, int durationInMinutes, int weight, Truck truck, Site origin, Collection<Branch> destinations)
    {
        this.id = id;
        this.startTime = startTime;
        this.endTime = startTime.plusMinutes(durationInMinutes);
        this.weight = weight;
        this.truck = truck;
        this.origin = origin;
    }

    public int getId() {
        return id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    protected void setStartTime(LocalDateTime startTime) throws Exception {
        if (startTime.compareTo(endTime) > 0)
            throw new Exception("start time cant be later than the end time");
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    protected void setEndTime(LocalDateTime endTime) throws Exception {
        if (endTime.compareTo(startTime) < 0)
            throw new Exception("end time cant be earlier than the start time");
        this.endTime = endTime;
    }

    public int getWeight() {
        return weight;
    }

    protected void setWeight(int weight) {
        this.weight = weight;
    }

    public Truck getTruck() {
        return truck;
    }

    protected void setTruck(Truck truck) {
        this.truck = truck;
    }

    public Site getOrigin() {
        return origin;
    }

    protected void setOrigin(Site origin) {
        this.origin = origin;
    }

    protected boolean isDeliveryTime(LocalDateTime time)
    {
        return time.compareTo(endTime) < 0 && time.compareTo(startTime) > 0;
    }
}
