package BusinessLayer.Objects;

import java.util.Collections;
import java.util.List;

public class Qualification
{
    // TODO IMPLEMENT
    private String name;
    private List<Permission> permissions;

    protected Qualification(String name, List<Permission> permissions)
    {
        this.name = name;
        this.permissions = permissions;
    }

    public String getName()
    {
        return name;
    }

    public List<Permission> getPermissions()
    {
        return Collections.unmodifiableList(permissions);
    }
}
