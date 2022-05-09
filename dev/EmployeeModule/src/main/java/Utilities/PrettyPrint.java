package Utilities;

import PresentationLayer.CLI.BoldSeparator;
import PresentationLayer.CLI.LineSeparator;
import PresentationLayer.CLI.SeparatorSet;

import java.util.Arrays;
import java.util.Scanner;

import static Utilities.Pair.getKeys;
import static Utilities.PrettyInput.printAndWaitForLegalInt;

public class PrettyPrint
{
    public static final int TAB_SIZE = 4;

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
        int tabCount = calcMaxTabSize(getKeys(entries).toArray(new String[entries.length]));

        // Print entries
        for(Pair<String, String> pair : entries) {
            String k = pair.getKey();
            String v = pair.getValue();
            s += k + mulString("\t", (tabCount - calcTabSize(k) + 1)) + separators.horizontal() + v + "\n";
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
        int tabCount = calcMaxTabSize(getKeys(entries).toArray(new String[entries.length]));
        int intersectionPosition = (tabCount + 1) * TAB_SIZE;
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
        return s.length() / TAB_SIZE - ((s.length() % TAB_SIZE) == (TAB_SIZE - 1) ? 0 : 1);
    }

    public static String mulString(String s, int times) {
        String res = "";
        for(int i = 0; i < times; i++) {
            res += s;
        }
        return res;
    }
}
