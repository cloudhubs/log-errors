package ires.baylor.edu.logerrors.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class FileStructure {
    String fileName;
    List<String> classNames;
    List<String> functionNames;
    List<String> imports;
    List<String> varNames;


    public FileStructure() {
        functionNames = new ArrayList<>();
        imports = new ArrayList<>();
        classNames = new ArrayList<>();
        varNames = new ArrayList<>();
    }

    public void addFuncName(String str) {
        this.functionNames.add(str);
    }

    public void addClassName(String str) {
        this.classNames.add(str);
    }

    public void addImports(String str) {
        this.imports.add(str);
    }

    public void addVarNames(String str) {
        this.varNames.add(str);
    }


    public void removeDuplicates() {
        List<String> withoutDuplicates = new ArrayList<>(new LinkedHashSet<>(classNames));
        classNames = withoutDuplicates;
        withoutDuplicates = new ArrayList<>(new LinkedHashSet<>(this.functionNames));
        this.functionNames = withoutDuplicates;
        withoutDuplicates = new ArrayList<>(new LinkedHashSet<>(this.imports));
        this.imports = withoutDuplicates;
        withoutDuplicates = new ArrayList<>(new LinkedHashSet<>(this.varNames));
        this.varNames = withoutDuplicates;
    }
}