package ires.baylor.edu.logerrors.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class PeekableScanner {
    private Scanner scan;
    private String next;

    public PeekableScanner(File filename) throws FileNotFoundException {
        scan = new Scanner(filename);
        next = (scan.hasNext() ? scan.nextLine() : null);
    }

    public boolean hasNextLine() {
        return (next != null);
    }

    public String nextLine() {
        String current = next;
        next = (scan.hasNextLine() ? scan.nextLine() : null);
        return current;
    }

    public String peekLine() {
        return next;
    }
}