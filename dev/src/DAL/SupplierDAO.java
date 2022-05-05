package DAL;

import BusinessLayer.Supplier;

import java.util.HashMap;

public class SupplierDAO extends DalController {
    private SupplierMapper sMapper;


    public SupplierDAO(){
        super("Suppliers");
        sMapper = new SupplierMapper();
    }

    public void select(){};
    public void insert(){}
    public void delete(){}
    public void update(){}


}
