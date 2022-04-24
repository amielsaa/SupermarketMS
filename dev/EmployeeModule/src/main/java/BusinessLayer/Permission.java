package BusinessLayer;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Permission
{
    private String name;

    protected Permission(@NotNull String _name){
        name = _name;
    }


    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Permission that = (Permission) o;
        return getName().equals(that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
