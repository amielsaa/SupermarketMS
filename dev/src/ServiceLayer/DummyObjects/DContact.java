package ServiceLayer.DummyObjects;

import BusinessLayer.Contact;

public class DContact {
    private String name;
    private String Phone_Num;

    public DContact(Contact c) {
        this.name = c.getName();
        Phone_Num = c.getPhone_Num();
    }

    public String getName() {
        return name;
    }

    public String getPhone_Num() {
        return Phone_Num;
    }
}
