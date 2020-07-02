package ires.baylor.edu.logerrors.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class ClassStructure {
    List<String> classNames;
    List<String> functionNames;
    List<String> imports;

    public ClassStructure() {
        functionNames = new ArrayList<>();
        imports = new ArrayList<>();
        classNames = new ArrayList<>();
    }

    ClassStructure(ClassStructure copy) {
        this.classNames = copy.getClassNames();
        this.functionNames = copy.getFunctionNames();
        this.imports = copy.getImports();
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
}