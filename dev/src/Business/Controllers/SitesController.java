package Business.Controllers;
import Business.SimpleObjects.*;
import java.util.ArrayList;
import java.util.Collection;

public class SitesController {
    private Collection<Site> sites;
    private int nextID;
    public SitesController(){
        sites = new ArrayList<>();
        nextID = 1;
    }

    public void addSupplier(String address, String deliveryZone, String phoneNumber, String contactName){
        sites.add(new Supplier(nextID, address, deliveryZone, phoneNumber, contactName));
        nextID++;
    }

    public void addStore(String address, String deliveryZone, String phoneNumber, String contactName){
        sites.add(new Store(nextID, address, deliveryZone, phoneNumber, contactName));
        nextID++;
    }

    public void editSiteAddress(int id, String address)
    {
        Site toBeEdited = getSite(id);
        if (toBeEdited != null)
            toBeEdited.setAddress(address);
    }

    public void editSiteDeliveryZone(int id, String zone)
    {
        Site toBeEdited = getSite(id);
        if (toBeEdited != null)
            toBeEdited.setDeliveryZone(zone);
    }

    public void editSitePhoneNumber(int id, String phoneNumber)
    {
        Site toBeEdited = getSite(id);
        if (toBeEdited != null)
            toBeEdited.setPhoneNumber(phoneNumber);
    }

    public void editSiteContactName(int id, String name)
    {
        Site toBeEdited = getSite(id);
        if (toBeEdited != null)
            toBeEdited.setContactName(name);
    }

    public Site getSite(int id) {
        for (Site site: sites)
            if (site.getId() == id)
                return site;
        return null;
    }


    public Collection<Site> getAllSites() {
        return sites;
    }

    public Collection<Site> viewSitesPerZone(String zone){
        Collection<Site> output = new ArrayList<>();
        for (Site site: sites)
            if (site.getDeliveryZone().equals(zone))
                output.add(site);
        return output;
    }


    public void deleteSite(int id)
    {

    }
}
