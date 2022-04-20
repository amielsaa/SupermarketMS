package BusinessLayer;

public class Driver {
    int id;
    String name;
    LicenseType licenseType;

    public Driver(int id, String name, LicenseType licenseType) {
        this.id = id;
        this.name = name;
        this.licenseType = licenseType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LicenseType getLicenseType() {
        return licenseType;
    }

    public void setLicenseType(LicenseType licenseType) {
        this.licenseType = licenseType;
    }
}
