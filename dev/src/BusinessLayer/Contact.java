package BusinessLayer;

public class Contact {
    private String name;
    private String Phone_Num;

    public Contact(String name, String phone_num) {
        this.name = name;
        Phone_Num = phone_num;
    }

    public String getPhone_Num() {
        return Phone_Num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone_Num(String phone_Num) {
        Phone_Num = phone_Num;
    }
}
