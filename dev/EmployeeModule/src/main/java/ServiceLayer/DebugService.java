package ServiceLayer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class DebugService
{
    public static void main(String[] args) {
        /*
        String date = "2022-12-15T00:00:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDateTime ldt = LocalDateTime.parse(date);
        System.out.println(ldt.format(formatter));
        */
        Scanner sc = new Scanner(System.in);
        System.out.println("Please provide the date in the following format: 'yyyy-MM-dd' for example - 2022-03-29");
        String in = sc.nextLine() + "T00:00:00";
        System.out.println(LocalDateTime.parse(in));

    }
}
