package Utilities.CLIUtil;

import EmployeeModule.BusinessLayer.*;
import EmployeeModule.PresentationLayer.CLI.BoldSeparator;
import EmployeeModule.PresentationLayer.CLI.LineSeparator;
import EmployeeModule.PresentationLayer.CLI.SeparatorSet;
import Utilities.Pair;

import java.util.Arrays;
import java.util.Scanner;

import static Utilities.CLIUtil.PrettyInput.printAndWaitForLegalInt;

public class PrettyPrint
{
    public static final int TAB_SIZE = 4;
    public static final int MARGIN_TABS = 1;

    public static String makeErrorMessage(String msg) {
        return "[ERROR] " + msg;
    }

    public static String makeSuccessMessage() {
        return "[SUCCESS]";
    }

    public static String makeTitle(String title, SeparatorSet separators) {
        String filler = mulString(separators.vertical(), 3);
        return filler + " " + title + " " + filler;
    }
    public static String makeTitle(String title) {
        return makeTitle(title, new LineSeparator());
    }

    public static String makeBigTitle(String title, SeparatorSet separators) {
        String filler = separators.horizontalClean();
        String titleLine = filler + " " + title + " " + filler;
        String border = mulString(separators.vertical(), titleLine.length());
        return border + "\n" + titleLine + "\n" + border;
    }
    public static String makeBigTitle(String title) {
        return makeBigTitle(title, new BoldSeparator());
    }

    public static String interactWithOptions(Scanner s, String title, String[] options) {
        final int START_INDEX = 1;
        int size = options.length + START_INDEX;

        Pair[] optionPairs = makeIndexedEntries(options, START_INDEX);
        System.out.println(makeListWithTitle(title, optionPairs));
        return options[printAndWaitForLegalInt(s, "Please choose an option: ", (x) -> (x >= START_INDEX && x < size), "This option is illegal. ") - START_INDEX];
    }

    public static String makeList(Pair<String, String>[] entries, SeparatorSet separators) {
        String s = "";
        if(entries.length < 1) {
            return s;
        }

        // Calculate max tab count

        // very messy function call due to the java version used :/
        int tabCount = calcMaxTabSize(Pair.getKeys(entries).toArray(new String[entries.length]));

        // Print entries
        for(Pair<String, String> pair : entries) {
            String k = pair.getKey();
            String v = pair.getValue();
            s += k + mulString(" ", (calcLeftSize(k) + tabCount - calcTabSize(k))) + separators.horizontal() + v + "\n";
        }
        return s;
    }

    public static String makeList(Pair<String, String>[] entries) {
        return makeList(entries, new LineSeparator());
    }

    public static String makeListWithTitle(String title, Pair<String, String>[] entries, SeparatorSet separators)
    {
        String body;
        if(entries.length == 0) {
            body = "<Empty>\n";
        } else {
            body = makeList(entries, separators);
        }
        String formattedTitle = makeTitle(title);
        int tabCount = calcMaxTabSize(Pair.getKeys(entries).toArray(new String[entries.length]));
        int intersectionPosition = (tabCount);
        int maxLineLength = Arrays.stream(body.split("\n")).max((s1, s2) -> (s1.length() - s2.length())).get().length();
        int headerLength = Math.max(formattedTitle.length(), maxLineLength);

        String separationLine = mulString(separators.vertical(), intersectionPosition) + separators.intersection() + mulString(separators.vertical(), headerLength - intersectionPosition);

        return formattedTitle + "\n" + separationLine + "\n" + body;
    }

    public static String makeListWithTitle(String title, Pair<String, String>[] entries) {
        return makeListWithTitle(title, entries, new LineSeparator());
    }


    public static Pair<String, String>[] makeIndexedEntries(String[] strings, int startIndex){
        Pair<String, String>[] entries = new Pair[strings.length];

        for(int i = 0; i < strings.length; i++) {
            entries[i] = new Pair("" + (i + startIndex), strings[i]);
        }

        return entries;
    }

    public static int calcMaxTabSize(String[] strings) {
        int tabCount = 0;
        for (String s : strings)
        {
            int c = calcTabSize(s);
            if(tabCount < c)
            {
                tabCount = c;
            }
        }
        return tabCount;
    }

    public static int calcTabSize(String s) {
        int len = s.length();
        if(len % TAB_SIZE == 0)
        {
            return len;
        }
        return len + TAB_SIZE - (len) % TAB_SIZE;
    }

    public static int calcLeftSize(String s) {
        int len = s.length();
        if(len % TAB_SIZE == 0)
        {
            return 0;
        }
        return TAB_SIZE - (len) % TAB_SIZE;
    }

    public static String mulString(String s, int times) {
        String res = "";
        for(int i = 0; i < times; i++) {
            res += s;
        }
        return res;
    }

    public static void printEmployee(Employee e) {
        // Employee data
        PrettyTable employeeTable = new PrettyTable("ID", "Name", "Salary", "Work Starting Date");
        employeeTable.insert(Integer.toString(e.getId()), e.getName(), Double.toString(e.getSalary()), e.getWorkStartingDate().toLocalDate().toString());
        System.out.println(employeeTable.toString());

        // Working conditions data
        WorkingConditions wc = e.getWorkingConditions();
        String[] qualificationNames = wc.getQualifications().toArray(new String[wc.getQualifications().size()]);

        if(qualificationNames.length > 0)
        {
            Pair[] optionPairs = makeIndexedEntries(qualificationNames, 1);
            System.out.println(makeListWithTitle("Qualifications", optionPairs));
        } else {
            System.out.println(makeTitle("no qualifications"));
        }

        System.out.println(makeTitle("Working Conditions"));
        String wcDescription = wc.getDescription();
        System.out.println(wcDescription.length() == 0 ? "[Empty]" : wcDescription);

        if(wc.getWorkingHours().size() > 0)
        {
            System.out.println(makeBigTitle("Working Hours"));
            PrettyTable workingHoursTable = new PrettyTable("Starting Date and Time", "Ending Date and Time");
            for (TimeInterval ti : wc.getWorkingHours())
            {
                workingHoursTable.insert(ti.getStart().toString(), ti.getEnd().toString());
            }
            System.out.println(workingHoursTable.toString());
        } else {
            System.out.println(makeTitle("no working hours"));
        }
    }

    public static void printBankAccountDetails(BankAccountDetails bad) {
        // Bank account details
        System.out.println(makeBigTitle("Bank Account Details"));
        PrettyTable badTable = new PrettyTable("Bank Id", "Branch Id", "Account Id", "Bank Name", "Branch Name", "Account Name");
        badTable.insert(
                Integer.toString(bad.bankId()),
                Integer.toString(bad.branchId()),
                Integer.toString(bad.accountId()),
                bad.bankName(),
                bad.branchName(),
                bad.accountOwner()
        );
        System.out.println(badTable.toString());
    }
}
