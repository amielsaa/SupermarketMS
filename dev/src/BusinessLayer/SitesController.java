package BusinessLayer;
import java.util.*;

public class SitesController {
    private Map<Integer, Site> sites;
    private HashMap<String,Integer> siteAddressMapper;
    private int nextID;
    public SitesController(){
        sites = new HashMap<>();
        siteAddressMapper=new HashMap<>();
        nextID = 1;
    }

    public void load() throws Exception{
        addSupplierWarehouse("Haifa", 0, "054-0000001", "supplier1");
        addSupplierWarehouse("Beer Sheva", 2, "054-0000002", "supplier2");
        addBranch("Tiberias", 0,"054-0000003","branch1");
        addBranch("Tel Aviv", 1,"054-0000004","branch2");
        addBranch("Jerusalem", 1,"054-0000005","branch3");
        addBranch("Dimona", 2,"054-0000006","branch4");
    }

    public void addSupplierWarehouse(String address, int deliveryZone, String phoneNumber, String contactName) throws Exception {
        if(siteAddressMapper.containsKey(address)){
            throw new Exception(String.format("A site with address %s already exists..",address));
        }
        siteAddressMapper.put(address,nextID);
        sites.put(nextID, new SupplierWarehouse(nextID, address, deliveryZone, phoneNumber, contactName));
        nextID++;
    }

    public void addBranch(String address, int deliveryZone, String phoneNumber, String contactName) throws Exception {
        if(siteAddressMapper.containsKey(address)){
            throw new Exception(String.format("A site with address %s already exists..",address));
        }
        siteAddressMapper.put(address,nextID);
        sites.put(nextID, new Branch(nextID, address, deliveryZone, phoneNumber, contactName));
        nextID++;
    }

    public void editSiteAddress(int id, String address) throws Exception {
        Site toBeEdited = getSite(id);
        if (toBeEdited == null)
            throw new Exception("the site id has not been found, so nothing changed");
        siteAddressMapper.remove(toBeEdited.getAddress());
        toBeEdited.setAddress(address);
        siteAddressMapper.put(address,id);
    }

    public void editSiteDeliveryZone(int id, int zone) throws Exception {
        Site toBeEdited = getSite(id);
        if (toBeEdited == null)
            throw new Exception("the site id has not been found, so nothing changed");
        toBeEdited.setDeliveryZone(zone);
    }

    public void editSitePhoneNumber(int id, String phoneNumber) throws Exception {
        Site toBeEdited = getSite(id);
        if (toBeEdited == null)
            throw new Exception("the site id has not been found, so nothing changed");
        toBeEdited.setPhoneNumber(phoneNumber);
    }

    public void editSiteContactName(int id, String name) throws Exception {
        Site toBeEdited = getSite(id);
        if (toBeEdited == null)
            throw new Exception("the site id has not been found, so nothing changed");
        toBeEdited.setContactName(name);
    }

    public Site getSite(int id) throws Exception {
        Site site = sites.get(id);
        if (site == null)
            throw new Exception("CHANGE ME");
        return site;
    }
    public Site getSite(String address) {
        return sites.get(siteAddressMapper.get(address));
    }


    public Collection<Site> getAllSites() {
        return sites.values();
    }

    public Collection<Site> viewSitesPerZone(int zone) throws Exception {
        //DeliveryZone deliveryZone = Site.stringToDeliveryZone(zone);
        Collection<Site> output = new ArrayList<>();
        Collection<Site> site_list = sites.values();
        for (Site site: site_list)
            if (site.getDeliveryZone() == Site.stringToDeliveryZone(zone))
                output.add(site);
        return output;
    }

    public int getSiteId(String address) throws Exception{
        if(!siteAddressMapper.containsKey(address)){
            throw new Exception(String.format("A site with address %s does not exist",address));
        }
        return siteAddressMapper.get(address);
    }


    public void deleteSite(int id) throws Exception {
        Site site=getSite(id);
        if(site==null)
            throw new Exception(String.format("A site with id %d does not exist",id));
        siteAddressMapper.remove(site.getAddress());
        sites.remove(id);
    }

    public ArrayList<Site> getAllDestinations(){
        Collection<Site> siteList=sites.values();
        ArrayList<Site> destList=new ArrayList<>();
        for(Site site:siteList){
            if(site.canBeADestination()){destList.add(site);}
        }
        return destList;
    }
}
