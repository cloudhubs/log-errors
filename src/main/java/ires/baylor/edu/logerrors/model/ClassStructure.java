package ires.baylor.edu.logerrors.model;

import lombok.*;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class ClassStructure {
    List<String> classNames;
    List<String> functionNames;
    List<String> imports;
    List<String> varNames;
    List<String> externalPackages; //From requirements.txt file


    ClassStructure(ClassStructure copy) {
        this.classNames = copy.getClassNames();
        this.functionNames = copy.getFunctionNames();
        this.imports = copy.getImports();
        this.varNames = copy.getVarNames();
        this.externalPackages = copy.getExternalPackages();
    }




    public ClassStructure() {
        functionNames = new ArrayList<>();
        imports = new ArrayList<>();
        classNames = new ArrayList<>();
        varNames = new ArrayList<>();
        externalPackages = new ArrayList<>();
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
    public void addExternalPackages(String str) {
        this.externalPackages.add(str);
    }


    public void removeDuplicates() {
        List<String> withoutDuplicates = new ArrayList<>(new LinkedHashSet<>(classNames));
        classNames = withoutDuplicates;
        withoutDuplicates = new ArrayList<>(new LinkedHashSet<>(this.externalPackages));
        this.externalPackages = withoutDuplicates;
        withoutDuplicates = new ArrayList<>(new LinkedHashSet<>(this.functionNames));
        this.functionNames = withoutDuplicates;
        withoutDuplicates = new ArrayList<>(new LinkedHashSet<>(this.imports));
        this.imports = withoutDuplicates;
        withoutDuplicates = new ArrayList<>(new LinkedHashSet<>(this.varNames));
        this.varNames = withoutDuplicates;
    }
}