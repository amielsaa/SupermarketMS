package BusinessLayer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class Delivery {
    private int id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Driver driver;
    private Truck truck;
    private Site origin;
    private LinkedHashMap<Branch, HashMap<String,Integer>> destinationItems;
    private int weight;
    public Delivery(int id, LocalDateTime startTime, LocalDateTime endTime ,Driver driver,Truck truck, Site origin )
    {
        this.id = id;
        this.startTime = startTime;
        this.driver = driver;
        this.endTime = endTime;
        this.truck = truck;
        this.origin = origin;
        this.destinationItems=new LinkedHashMap<>();
        this.weight=0;
    }

    public int getId() {
        return id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    protected void setStartTime(LocalDateTime startTime) throws Exception {
        if (startTime.isAfter(endTime))
            throw new Exception("start time cant be later than the end time");
        if(startTime.isBefore(LocalDateTime.now()))
            throw new Exception("start time has passed");
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    protected void setEndTime(LocalDateTime endTime) throws Exception {
        if (endTime.isBefore(startTime))
            throw new Exception("end time cant be earlier than the start time");
        if(endTime.isBefore(LocalDateTime.now()))
            throw new Exception("end time has passed");
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

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    protected boolean isDeliveryTime(LocalDateTime time)
    {
        return time.compareTo(endTime) < 0 && time.compareTo(startTime) > 0;
    }

    public void addDestination(Branch branch) throws Exception {
        if(!destinationItems.containsKey(branch)){
            destinationItems.put(branch,new HashMap<>());
        }
        else
            throw new Exception("CHANGE ME");
    }
    public void removeDestination(Branch branch) throws Exception {
        if(destinationItems.containsKey(branch)){
            destinationItems.remove(branch);
        }
        else
            throw new Exception("CHANGE ME");
    }
    public void addItemToDestination(Branch branch, String item, int quantity) throws Exception {
        if(!destinationItems.containsKey(branch)){
            throw new Exception(String.format("'%s' is not a destination of this delivery",branch.getAddress()));
        }
        if(destinationItems.get(branch).containsKey(item)){
            throw new Exception(String.format("'%s' is already to be delivered to %s.",item,branch.getAddress()));
        }
        destinationItems.get(branch).put(item,quantity);
    }
    public void removeItemFromDestination(Branch branch, String item) throws Exception {
        if(!destinationItems.containsKey(branch)){
            throw new Exception(String.format("'%s' is not a destination of this delivery",branch.getAddress()));
        }
        if (destinationItems.get(branch).remove(item) == null)
            throw new Exception();
    }
    public void editItemQuantity(Branch branch, String item, int quantity) throws Exception {
        if(!destinationItems.containsKey(branch)){
            throw new Exception(String.format("'%s' is not a destination of this delivery",branch.getAddress()));
        }
        if(!destinationItems.get(branch).containsKey(item)){
            throw new Exception(String.format("'%s' is not to be delivered to %s.",item,branch.getAddress()));
        }
        destinationItems.get(branch).replace(item,quantity);
    }
    public String toStringItemsOfDest(Branch site){
        String output= String.format("\t\t** Destination address: %s\n\t\t\t*** Items:\n",site.getAddress());
        HashMap<String,Integer> itemMap=destinationItems.get(site);
        for(Map.Entry pair:itemMap.entrySet()){
            output=output.concat(String.format("\t\t\t\t**** item name: %s,  quantity: %d\n",pair.getKey(),pair.getValue()));
        }
        return output;
    }

    public Set<Branch> getDestinations(){
        return destinationItems.keySet();
    }

    @Override
    public String toString(){
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        String output=String.format("Delivery id: %d\n\t* Driver name: %s\n\t* Driver id: %d\n\t* Start time: %s\n\t* End time: %s\n\t* Origin: %s\n\t* Weight: %d\n\t* Destinations:\n",
                id,driver.getName(),driver.getId(),startTime.format(formatter),endTime.format(formatter),origin.getAddress(),weight);
        for (Branch site:destinationItems.keySet()){ output=output.concat(toStringItemsOfDest(site));}
        return output;
    }
}
