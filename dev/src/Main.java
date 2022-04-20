import PresentationLayer.UserTerminal;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args)
    {
        System.out.println(LocalDateTime.now().compareTo(LocalDateTime.now().plusDays(1)));
        //UserTerminal terminal = new UserTerminal();
        //terminal.run();
    }
}
