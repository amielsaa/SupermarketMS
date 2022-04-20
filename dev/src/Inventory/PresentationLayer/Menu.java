package Inventory.PresentationLayer;

public class Menu {

    private CommandLineTable clt;

    public Menu() {
        this.clt = new CommandLineTable();
        this.clt.setShowVerticalLines(true);
    }

    private void printMenu() {
        System.out.println("1-Add Product\n" +
                "2-Add Store Product\n" +
                "3-Add Category\n" +
                "4-Print All Store Products");
    }
}
