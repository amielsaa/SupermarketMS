package PresentationLayer.CLI;

public class LineSeparator implements SeparatorSet
{
    @Override
    public String horizontalClean()
    {
        return "|";
    }

    @Override
    public String horizontal()
    {
        return " | ";
    }

    @Override
    public String vertical()
    {
        return "-";
    }

    @Override
    public String intersection()
    {
        return "-+-";
    }
    public String intersectionClean()
    {
        return "+";
    }
}
