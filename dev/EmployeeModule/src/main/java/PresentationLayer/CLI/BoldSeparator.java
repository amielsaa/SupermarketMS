package PresentationLayer.CLI;

public class BoldSeparator implements SeparatorSet
{
    @Override
    public String horizontalClean()
    {
        return "#";
    }

    @Override
    public String horizontal()
    {
        return " # ";
    }

    @Override
    public String vertical()
    {
        return "=";
    }

    @Override
    public String intersection()
    {
        return "=O=";
    }
    public String intersectionClean()
    {
        return "O";
    }
}
