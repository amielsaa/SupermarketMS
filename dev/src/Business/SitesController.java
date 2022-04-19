package Business;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SitesController {
    private Map<Integer, Site> sites;
    private int nextID;
    public SitesController(){
        sites = new HashMap<>();
        nextID = 1;
    }

    public void addSupplierWarehouse(String address, int deliveryZone, String phoneNumber, String contactName) throws Exception {
        sites.put(nextID, new SupplierWarehouse(nextID, address, deliveryZone, phoneNumber, contactName));
        nextID++;
    }

    public void addBranch(String address, int deliveryZone, String phoneNumber, String contactName) throws Exception {
        sites.put(nextID, new Branch(nextID, address, deliveryZone, phoneNumber, contactName));
        nextID++;
    }

    public void editSiteAddress(int id, String address) throws Exception {
        Site toBeEdited = getSite(id);
        if (toBeEdited == null)
            throw new Exception("the site id has not been found, so nothing changed");
        toBeEdited.setAddress(address);
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

    public Site getSite(int id) {
        return sites.get(id);
    }


    public Collection<Site> getAllSites() {
        return sites.values();
    }

    public Collection<Site> viewSitesPerZone(int zone) throws Exception {
        Site.DeliveryZone deliveryZone = Site.stringToDeliveryZone(zone);
        Collection<Site> output = new ArrayList<>();
        Collection<Site> site_list = sites.values();
        for (Site site: site_list)
            if (site.getDeliveryZone() == Site.stringToDeliveryZone(zone))
                output.add(site);
        return output;
    }


    public void deleteSite(int id)
    {
        sites.remove(id);
    }
}
