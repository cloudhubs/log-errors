package ires.baylor.edu.logerrors.parser;
import ires.baylor.edu.logerrors.model.ClassStructure;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Scanner;

public class ProjectStructureParser {
    private static ClassStructure cs = null;

    public static ClassStructure getClassStructure(String current) {
        cs = new ClassStructure();

        if(current != null) {
            File folder = new File(current);
            try {
                recursiveFunc(folder);
                cs.removeDuplicates();
            } catch (FileNotFoundException e) {
                System.out.println("AAHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH");
            }
        }
        return cs;
    }

    private static void recursiveFunc(File current) throws FileNotFoundException {

        File[] listOfFiles = current.listFiles();
        for (File f : listOfFiles) {
            if (f.isFile()) {
                if (f.getName().endsWith(".py") && !f.getName().contains("__init__")) {

                    Scanner scan = new Scanner(f);
                    String currentLine = null;
                    while (scan.hasNextLine()) {
                        currentLine = scan.nextLine();

                        if (currentLine.matches("^from .* import .*") || currentLine.matches("^import .*")) {
                            // currentLine = currentLine.replaceFirst("^from ", "").replaceFirst("import ",
                            // "");
                            cs.addImports(currentLine);

                        } else if (currentLine.matches(" *class .*: *")) {
                            currentLine = currentLine.replaceFirst(": *", "").replaceFirst(" *class ", "");
                            cs.addClassName(currentLine);

                        } else if (currentLine.matches(" *def .*(.*): *") && !currentLine.contains("__init__")) {
                            currentLine = currentLine.replaceFirst(": *", "").replaceFirst(" *def ", "");
                            cs.addFuncName(currentLine);

                        } else if (currentLine.matches(".*=.*") && !currentLine.matches(".*#.*|.*(\\+|-|!|=|>|<)=.*")) {
                            if (currentLine.matches(".*self\\..*")){
                                cs.addVarNames(currentLine.replaceAll(" *=.*", "").replaceAll(".*self\\.", "").replaceFirst(" *", ""));
                            } else if (!currentLine.replaceAll("=.*", "").matches(".*\\..*") && !currentLine.matches("[^=]*\\(.*=.*\\).*")) {
                                cs.addVarNames(currentLine.replaceAll(" *=.*", "").replaceFirst(" *", "").replaceAll("\\[.*\\]", ""));
                            }
                        }

                    }
                    scan.close();
                } else if (f.getName().equalsIgnoreCase("requirements.txt")) {
                    Scanner scan = new Scanner(f);
                    while (scan.hasNextLine()) {
                        cs.addExternalPackages(scan.nextLine());
                    }
                    scan.close();
                }
            } else if (f.isDirectory()) {
                recursiveFunc(f);
            }
        }

    }
}
