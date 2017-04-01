import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by kaushik on 25/2/17.
 */
class CodeParser {

    private StringBuilder _umlBuilder;
    private HashMap<String, ClassOrInterfaceDeclaration> _typeMap;
    private HashMap<String, List<MethodDeclaration>> _interfaceMap;
    private RelationsReader _relBuilder;
    private MethodsReader _methodReader;
    private AttributesReader _attrsReader;
    private ConstructorReader _consReader;

    CodeParser() {
        _umlBuilder = new StringBuilder();
        _relBuilder = new RelationsReader(this);
        _methodReader = new MethodsReader(this);
        _attrsReader = new AttributesReader(this);
        _consReader = new ConstructorReader(this);
        _interfaceMap = new HashMap<>();
        _typeMap = new HashMap<>();
    }

    public HashMap getInterfaceMap() {
        return _interfaceMap;
    }

    public HashMap<String, ClassOrInterfaceDeclaration> _getTypeMap() {
        return _typeMap;
    }

    public StringBuilder getUmlBuilder() {
        return _umlBuilder;
    }

    public RelationsReader getRelationsBuilder() {
        return _relBuilder;
    }


    public StringBuilder readCodeTree(String filePath) {
        File sourceFolder = new File(filePath);
        _umlBuilder.append("@startuml\n skinparam classAttributeIconSize 0\n");

        if (sourceFolder.exists()) {
            for (File source : sourceFolder.listFiles()) {
                if (source.getName().endsWith(".java")) {
                    try {
                        CompilationUnit unit = JavaParser.parse(source);
                        ClassOrInterfaceDeclaration dec = readClassOrInterfaceName(unit);
                        if (dec != null) {
                            _typeMap.put(dec.getName().toString(), dec);
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
            for (ClassOrInterfaceDeclaration dec : _typeMap.values()) {
                parseClassInterfaceTypes(dec);
            }
            _relBuilder.printRelations();
            _umlBuilder.append("\n@enduml");
            System.out.print("uml " + _umlBuilder.toString());
        }
        return _umlBuilder;
    }

    //reads all classes in file..
    private ClassOrInterfaceDeclaration readClassOrInterfaceName(CompilationUnit unit) {
        List<Node> nodelist = unit.getChildNodes();
        for (Node node : nodelist) {
            if (node instanceof ClassOrInterfaceDeclaration) {
                return (ClassOrInterfaceDeclaration) node;
            }
        }
        return null;
    }

    private void parseClassInterfaceTypes(ClassOrInterfaceDeclaration unit) {
        if (!unit.isInterface()) {
            _umlBuilder.append("class ").append(unit.getName()).append("{\n");
            _relBuilder.addRelations(unit.getExtendedTypes().stream().map(exType -> new RelationType(unit.getNameAsString(),
                    exType.getNameAsString(),
                    RelationEnum.EXTENDS, " ")).collect(Collectors.toList()));
            _relBuilder.addRelations(unit.getImplementedTypes().stream().map(impType -> new RelationType(unit.getNameAsString(),
                    impType.getNameAsString(),
                    RelationEnum.IMPEMENTS, " ")).collect(Collectors.toList()));
        } else {
            _umlBuilder.append("interface ").append(unit.getName()).append("{\n");
        }
        HashMap<String, String> varMap = _attrsReader.parseClassAttributes(unit);
        _consReader.readClassConstructors(unit);
        _methodReader.readAllMethodsInClass(unit, varMap);
        _attrsReader.printAttributes(varMap);
        _umlBuilder.append("}\n");
    }
}
