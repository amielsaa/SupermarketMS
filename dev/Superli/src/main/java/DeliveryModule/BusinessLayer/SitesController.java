package DeliveryModule.BusinessLayer;
import DeliveryModule.DataAccessLayer.SiteDAO;

import java.util.*;

public class SitesController {
    private int nextID;
    private SiteDAO siteDAO;

    public SitesController() {
        this.siteDAO=new SiteDAO();
        nextID=getNextID();
    }

    public void load() throws Exception{
        addSupplierWarehouse("Haifa", 0, "054-0000001", "supplier1");
        addSupplierWarehouse("Beer Sheva", 2, "054-0000002", "supplier2");
        addBranch("Tiberias", 0,"054-0000003","branch1");
        addBranch("Tel Aviv", 1,"054-0000004","branch2");
        addBranch("Jerusalem", 1,"054-0000005","branch3");
        addBranch("Dimona", 2,"054-0000006","branch4");
    }

    private int getNextID(){
        return siteDAO.getMaxId()+1;
    }
    public void addSupplierWarehouse(String address, int deliveryZone, String phoneNumber, String contactName) throws Exception {
        if(siteDAO.getSite(address)!=null){
            throw new Exception(String.format("A site with address %s already exists..",address));
        }
        siteDAO.Create(new SupplierWarehouse(nextID, address, deliveryZone, phoneNumber, contactName));
        nextID++;
    }

    public void addBranch(String address, int deliveryZone, String phoneNumber, String contactName) throws Exception {
        if(siteDAO.getSite(address)!=null){
            throw new Exception(String.format("A site with address %s already exists..",address));
        }
        siteDAO.Create(new Branch(nextID, address, deliveryZone, phoneNumber, contactName));
        nextID++;
    }

    public void editSiteAddress(int id, String address) throws Exception {
        Site toBeEdited = getSite(id);
        if (toBeEdited == null)
            throw new Exception("the site id has not been found, so nothing changed");
        toBeEdited.setAddress(address);
        siteDAO.editSiteAddress(id,address);
    }

    public void editSiteDeliveryZone(int id, int zone) throws Exception {
        Site toBeEdited = getSite(id);
        if (toBeEdited == null)
            throw new Exception("the site id has not been found, so nothing changed");
        toBeEdited.setDeliveryZone(zone);
        siteDAO.setDeliveryZone(id,zone);
    }

    public void editSitePhoneNumber(int id, String phoneNumber) throws Exception {
        Site toBeEdited = getSite(id);
        if (toBeEdited == null)
            throw new Exception("the site id has not been found, so nothing changed");
        toBeEdited.setPhoneNumber(phoneNumber);
        siteDAO.setPhoneNumber(id,phoneNumber);
    }

    public void editSiteContactName(int id, String name) throws Exception {
        Site toBeEdited = getSite(id);
        if (toBeEdited == null)
            throw new Exception("The site id has not been found, so nothing changed");
        toBeEdited.setContactName(name);
        siteDAO.setContactName(id,name);
    }

    public Site getSite(int id) throws Exception {
        Site site = siteDAO.getSite(id);
        if (site == null)
            throw new Exception(String.format("The site id %d does not exist",id));
        return site;
    }
    public Site getSite(String address) {
        return siteDAO.getSite(address);
    }

    public ArrayList<Site> getAllSites() {
        return siteDAO.getAllSites();
    }

    public Collection<Site> viewSitesPerZone(int zone) throws Exception {
        Collection<Site> output = new ArrayList<>();
        Collection<Site> site_list = siteDAO.getAllSites();
        for (Site site: site_list)
            if (site.getDeliveryZone() == Site.stringToDeliveryZone(zone))
                output.add(site);
        return output;
    }

    public int getSiteId(String address) throws Exception{
        Site site=getSite(address);
        if(site==null){
            throw new Exception(String.format("A site with address %s does not exist",address));
        }
        return site.getId();
    }


    public void deleteSite(int id) throws Exception {
        Site site=getSite(id);
        if(site==null)
            throw new Exception(String.format("A site with id %d does not exist",id));
        siteDAO.Delete(id);
    }

    public ArrayList<Site> getAllDestinations(){
        ArrayList<Site> siteList=siteDAO.getAllSites();
        ArrayList<Site> destList=new ArrayList<>();
        for(Site site:siteList){
            if(site.canBeADestination()){destList.add(site);}
        }
        return destList;
    }

    public String getDeliveryZoneName(int zone) throws Exception{
        if(zone>=DeliveryZone.values().length){
            throw new Exception(String.format("Delivery zone %d does not exist",zone));
        }
        return DeliveryZone.values()[zone].name();
    }
}
