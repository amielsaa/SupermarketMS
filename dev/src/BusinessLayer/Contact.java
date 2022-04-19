package BusinessLayer;

public class Contact {
    private String name;
    private int Phone_Num;

    public Contact(String name, int phone_num) {
        this.name = name;
        Phone_Num = phone_num;
    }

    public int getPhone_Num() {
        return Phone_Num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone_Num(int phone_Num) {
        Phone_Num = phone_Num;
    }
}
