package BusinessLayer;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Qualification
{
    private String name;
    private List<Permission> permissions;
    //the name would be unique for every qualification object
    public Qualification(@NotNull String name, @NotNull List<Permission> permissions) {
        this.name = name;
        this.permissions = permissions;
    }

    public String getName() {
        return name;
    }

    protected void setName(@NotNull String name) {
        this.name = name;
    }

    public List<Permission> getPermissions() {
        //TODO check if is needed to return unmodifiable list
        return Collections.unmodifiableList(permissions);
    }

    protected void addPermission(@NotNull Permission permission){
        permissions.add(permission);
    }

    protected void removePermission(@NotNull Permission permission){
        permissions.remove(permission);
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
