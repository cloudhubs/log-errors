package ires.baylor.edu.logerrors.parser;

import ires.baylor.edu.logerrors.model.FileStructure;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Slf4j
public class ProjectStructureParser {
    private static List<FileStructure> fs = null;
    private static List<String> requirements = null;

    public static List<FileStructure> getClassStructure(String current) {
        fs = new ArrayList<>();

        if (current != null) {
            File folder = new File(current);
            try {
                getClassStructure(folder);
                fs.forEach(t -> t.removeDuplicates());
            } catch (FileNotFoundException e) {
                log.info("Unable to parce file structure given");
            }
        }
        return fs;
    }


    private static void getClassStructure(File current) throws FileNotFoundException {

        File[] listOfFiles = current.listFiles();
        for (File f : listOfFiles) {
            if (f.isFile()) {
                if (f.getName().endsWith(".py") && !f.getName().contains("__init__")) {

                    Scanner scan = new Scanner(f);
                    String currentLine = null;
                    FileStructure temp = new FileStructure();
                    temp.setFileName(f.getName());

                    while (scan.hasNextLine()) {
                        currentLine = scan.nextLine();

                        if (currentLine.matches("^from .* import .*") || currentLine.matches("^import .*")) {
                            temp.addImports(currentLine);

                        } else if (currentLine.matches(" *class .*: *")) {
                            currentLine = currentLine.replaceFirst(": *", "").replaceFirst(" *class ", "");
                            currentLine = currentLine.replaceAll("\\(.*\\)", "");
                            temp.addClassName(currentLine);

                        } else if (currentLine.matches(" *def .*(.*): *") && !currentLine.contains("__init__")) {
                            currentLine = currentLine.replaceAll("\\(.*\\)", "");
                            currentLine = currentLine.replaceFirst(": *", "").replaceFirst(" *def ", "");
                            temp.addFuncName(currentLine);

                        } else if (currentLine.matches(".*=.*") && !currentLine.matches(".*#.*|.*(\\+|-|!|=|>|<)=.*")) {
                            if (currentLine.matches(".*self\\..*")) {
                                temp.addVarNames(currentLine.replaceAll(" *=.*", "").replaceAll(".*self\\.", "").replaceFirst(" *", ""));
                            } else if (!currentLine.replaceAll("=.*", "").matches(".*\\..*") && !currentLine.matches("[^=]*\\(.*=.*\\).*")) {
                                temp.addVarNames(currentLine.replaceAll(" *=.*", "").replaceFirst(" *", "").replaceAll("\\[.*\\]", ""));
                            }
                        }

                    }
                    scan.close();
                    fs.add(temp);
                }
            } else if (f.isDirectory()) {
                getClassStructure(f);
            }
        }

    }


    public static List<String> getRequirements(String current) {
        requirements = new ArrayList<>();
        if (current != null) {
            File folder = new File(current);
            try {
                getRecs(folder);
            } catch (FileNotFoundException e) {
                log.info("Unable to parce file structure given");
            }
        }
        return requirements;
    }


    private static void getRecs(File current) throws FileNotFoundException {
        File[] listOfFiles = current.listFiles();
        for (File f : listOfFiles) {
            if (f.isFile()) {
                if (f.getName().equalsIgnoreCase("requirements.txt")) {
                    Scanner scan = new Scanner(f);
                    while (scan.hasNextLine()) {
                        requirements.add(scan.nextLine());
                    }
                    scan.close();
                    return;
                }
            } else if (f.isDirectory()) {
                getRecs(f);
            }
        }

    }
}
