package DAL;

import BusinessLayer.Supplier;

import java.util.HashMap;

public class SupplierDAO extends DalController {
    private HashMap<Integer, Supplier> BN_To_Supplier;


    public SupplierDAO(){
        super("Suppliers");
        BN_To_Supplier = new HashMap<Integer, Supplier>();
    }

    public void select(){}
    public void insert(){}
    public void delete(){}
    //public void update(){}


}
