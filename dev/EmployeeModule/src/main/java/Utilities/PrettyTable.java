package Utilities;

import PresentationLayer.CLI.BoldSeparator;
import PresentationLayer.CLI.LineSeparator;
import PresentationLayer.CLI.SeparatorSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static Utilities.Pair.getKeys;
import static Utilities.PrettyPrint.*;

public class PrettyTable
{
    private int columnSize;
    private PrettyTableEntry header;
    private List<PrettyTableEntry> entries;
    private boolean hasHeader;

    public PrettyTable(int _columnSize) {
        this.columnSize = _columnSize;
        this.header = new PrettyTableEntry(columnSize);
        this.entries = new LinkedList<PrettyTableEntry>();
        this.hasHeader = false;
    }
    public  PrettyTable(String... header) {
        this.columnSize = header.length;
        this.header = new PrettyTableEntry(header);
        this.entries = new LinkedList<PrettyTableEntry>();
        this.hasHeader = true;
    }

    public boolean insert(String... fields) {
        if(fields.length != columnSize) {
            return false;
        }
        PrettyTableEntry entry = new PrettyTableEntry(fields);
        this.entries.add(entry);
        return true;
    }

    public boolean insertAt(int rowIndex, String... fields) {
        PrettyTableEntry entry = new PrettyTableEntry(fields);
        entries.add(rowIndex, entry);
        return true;
    }


    @Override
    public String toString()
    {
        String s = "";
        SeparatorSet boldSeparators = new BoldSeparator();
        if(hasHeader) {
            List<PrettyTableEntry> newEntries = new LinkedList<>();
            newEntries.add(header);
            for(PrettyTableEntry e : this.entries) newEntries.add(e);

            int[] tabCounts = calcTabCounts(newEntries);

            s += toStringEntry(header, boldSeparators, tabCounts) + "\n";

            return s + bodyToString(tabCounts);
        } else {
            return bodyToString();
        }
    }

    public String bodyToString() {
        int[] tabCounts = calcTabCounts(this.entries);
        return bodyToString();
    }

    public int[] calcTabCounts(List<PrettyTableEntry> entries) {
        if(entries.size() < 1) {
            return null;
        }

        // Calculate max tab count for each column
        int[] tabCounts = new int[columnSize];
        for(int i = 0; i < columnSize; i++)
        {
            String[] column = new String[entries.size()];
            for(int j = 0; j < column.length; j++) {
                column[j] = entries.get(j).get(i);
            }
            tabCounts[i] = calcMaxTabSize(column);

        }
        return tabCounts;
    }

    public String bodyToString(int[] tabCounts)
    {
        SeparatorSet separators = new LineSeparator();
        String s = "";


        // Print entries
        for(PrettyTableEntry entry : entries) {
            s += toStringEntry(entry, separators, tabCounts) + "\n";
        }
        return s;
    }

    private String toStringEntry(PrettyTableEntry entry, SeparatorSet separators, int[] tabCounts) {
        String row = separators.horizontalClean() + " ";
        String[] rowFields = entry.getFields();
        for(int i = 0; i < rowFields.length; i++)
        {
            row += rowFields[i] + mulString("\t", (tabCounts[i] - calcTabSize(rowFields[i]) + 1));
            if(i < rowFields.length - 1) {
                row += separators.horizontal();
            }
        }
        row += " " + separators.horizontalClean() + "\n";

        // Make lines and separators
        row += separators.intersectionClean() + separators.vertical();
        for(int i = 0; i < rowFields.length; i++)
        {
            row += mulString(separators.vertical(), ((tabCounts[i] + 1) * TAB_SIZE));
            if(i < rowFields.length - 1) {
                row += separators.intersection();
            }
        }
        row += separators.vertical() + separators.intersectionClean();

        return row;
    }

    private class PrettyTableEntry
    {
        private String[] fields;
        private int nextIndex = 0;

        public PrettyTableEntry(int size) {
            this.fields = new String[size];
            for(int i = 0; i < fields.length; i++) {
                fields[i] = "NULL";
            }
        }
        public PrettyTableEntry(String... _fields) {
            this.fields = new String[_fields.length];
            for(int i = 0; i < this.fields.length; i++) {
                this.fields[i] = _fields[i];
            }
        }

        public boolean insert(String s) {
            boolean b = insert(s, nextIndex);
            if(b) {
                nextIndex++;
            }
            return b;
        }

        public boolean insert(String s, int i) {
            if(i < 0 || i >= fields.length) {
                return false;
            }
            fields[i] = s;
            return true;
        }

        public String get(int index) {
            return fields[index];
        }

        public String[] getFields() {
            return fields;
        }
    }
}
