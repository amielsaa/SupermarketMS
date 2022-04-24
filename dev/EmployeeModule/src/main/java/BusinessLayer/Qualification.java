package BusinessLayer;



import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Qualification
{
    private String name;
    private List<Permission> permissions;
    //the name would be unique for every qualification object
    protected Qualification(String name, List<Permission> permissions) {
        this.name = name;
        this.permissions = permissions;
    }

    public String getName() {
        return name;
    }

    protected void setName(String name) {
        this.name = name;
    }

    public List<Permission> getPermissions() {
        return Collections.unmodifiableList(permissions);
    }

    protected void addPermission(Permission permission){
        permissions.add(permission);
    }

    protected void removePermission(Permission permission){
        permissions.remove(permission);
    }

    protected boolean hasPermission(Permission permission) { return this.permissions.contains(permission); }

    @Override
    public String toString() {
        return "Qualification{" +
                "name='" + name + '\'' +
                ", permissions=" + permissions +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Qualification that = (Qualification) o;
        return getName().equals(that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getPermissions());
    }
}
