package DeliveryModule.BusinessLayer;

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
    private int driverId;
    private int truckId;
    private int originSiteId;
    private LinkedHashMap<Integer, HashMap<String,Integer>> destinationItems;
    private int weight;

    public Delivery(int id, LocalDateTime startTime, LocalDateTime endTime, int driverId, int truckId, int originSiteId,int weight) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.driverId = driverId;
        this.truckId = truckId;
        this.originSiteId = originSiteId;
        this.destinationItems =new LinkedHashMap<>();
        this.weight = weight;
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

    public void setDestinationItems(LinkedHashMap<Integer, HashMap<String, Integer>> destinationItems) {
        this.destinationItems = destinationItems;
    }

    public LinkedHashMap<Integer, HashMap<String, Integer>> getDestinationItems() {
        return destinationItems;
    }

    public int getDriverId() {
      return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public int getTruckId() {
        return truckId;
    }

    public void setTruckId(int truckId) {
        this.truckId = truckId;
    }

    public int getOriginSiteId() {
        return originSiteId;
    }

    public void setOriginSiteId(int originSiteId) {
        this.originSiteId = originSiteId;
    }

    public void addDestination(Branch branch) throws Exception {
        if(!destinationItems.containsKey(branch.getId())){
            destinationItems.put(branch.getId(),new HashMap<>());
        }
        else
            throw new Exception(String.format("%s is already a destination of delivery number %d",branch.getAddress(),id));
    }

    public void removeDestination(Branch branch) throws Exception {
        if(destinationItems.containsKey(branch.getId())){
            if(destinationItems.size()>1)
                destinationItems.remove(branch.getId());
            else throw new Exception(String.format("Delivery %d contains a single destination...",id));
        }
        else
            throw new Exception(String.format("%s is not a destination of delivery number %d",branch.getAddress(),id));
    }

    public void addItemToDestination(Branch branch, String item, int quantity) throws Exception {
        if(!destinationItems.containsKey(branch.getId())){
            throw new Exception(String.format("'%s' is not a destination of this delivery",branch.getAddress()));
        }
        if(destinationItems.get(branch.getId()).containsKey(item)){
            throw new Exception(String.format("'%s' is already to be delivered to %s.",item,branch.getAddress()));
        }
        destinationItems.get(branch.getId()).put(item,quantity);
    }

    public void removeItemFromDestination(Branch branch, String item) throws Exception {
        if(!destinationItems.containsKey(branch.getId())){
            throw new Exception(String.format("'%s' is not a destination of this delivery",branch.getAddress()));
        }
        if (destinationItems.get(branch.getId()).remove(item) == null)
            throw new Exception(String.format("The item %s was not found at destination %s of delivery %d...",item,branch.getAddress(),id));
    }
    public void editItemQuantity(Branch branch, String item, int quantity) throws Exception {
        if(!destinationItems.containsKey(branch.getId())){
            throw new Exception(String.format("'%s' is not a destination of this delivery",branch.getAddress()));
        }
        if(!destinationItems.get(branch.getId()).containsKey(item)){
            throw new Exception(String.format("'%s' is not to be delivered to %s.",item,branch.getAddress()));
        }
        destinationItems.get(branch.getId()).replace(item,quantity);
    }
    public String toStringItemsOfDest(Integer site){
        String output= String.format("\t\t** Destination id: %d\n\t\t\t*** Items:\n",site);
        HashMap<String,Integer> itemMap=destinationItems.get(site);
        for(Map.Entry pair:itemMap.entrySet()){
            output=output.concat(String.format("\t\t\t\t**** Item name: %s,  Quantity: %d\n",pair.getKey(), pair.getValue()));
        }
        return output;
    }

    public Set<Integer> getDestinations(){
        return destinationItems.keySet();
    }

    @Override
    public String toString(){
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        String output=String.format("Delivery id: %d\n\t* Driver id: %d\n\t* Truck plate number: %d\n\t* Start time: %s\n\t* End time: %s\n\t* Origin Site Id: %d\n\t* Weight: %d\n\t* Destinations:\n",
                id,driverId,truckId,startTime.format(formatter),endTime.format(formatter),originSiteId,weight);
        for (Integer site:destinationItems.keySet()){ output=output.concat(toStringItemsOfDest(site));}
        return output;
    }
/*
    public HashMap<String,Integer> getProductsPerDestination(Branch destination){
        return destinationItems.get(destination);
    }

 */
}
