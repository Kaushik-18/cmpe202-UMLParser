import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.type.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by kaushik on 25/2/17.
 */
class CodeParser {

    private StringBuilder umlBuilder;
    private HashMap<String, ClassOrInterfaceDeclaration> _typeMap;
    private HashMap<String, List<MethodDeclaration>> _interfaceMap;
    private ArrayList<RelationType> _relationsList;

    CodeParser() {
        umlBuilder = new StringBuilder();
        _relationsList = new ArrayList<>();
        _interfaceMap = new HashMap<>();
        _typeMap = new HashMap<>();
    }

    public HashMap getInterfaceMap() {
        return _interfaceMap;
    }

    public HashMap _getTypeMap() {
        return _typeMap;
    }

    public StringBuilder getUmlBuilder() {
        return umlBuilder;
    }

    public List<RelationType> getRelationsList() {
        return _relationsList;
    }


    public StringBuilder readCodeTree(String filePath) {
        File sourceFolder = new File(filePath);
        umlBuilder.append("@startuml\n skinparam classAttributeIconSize 0\n");

        if (sourceFolder.exists()) {
            for (File source : sourceFolder.listFiles()) {
                if (source.getName().endsWith(".java")) {
                    try {
                        CompilationUnit unit = JavaParser.parse(source);
                        ClassOrInterfaceDeclaration dec = readClassOrInterfaceName(unit);
                        if (dec != null) {
                            _typeMap.put(dec.getName().toString(), dec);
                        }
                        parseClassInterfaceTypes(dec);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
            //printRelations(relationsList);
            umlBuilder.append("\n@enduml");
            System.out.print("uml " + umlBuilder.toString());
        }
        return umlBuilder;
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
            umlBuilder.append("class ").append(unit.getName()).append("{\n");
            _relationsList.addAll(unit.getExtendedTypes().stream().map(exType -> new RelationType(unit.getNameAsString(),
                    exType.getNameAsString(),
                    RelationEnum.EXTENDS, " ")).collect(Collectors.toList()));
            _relationsList.addAll(unit.getImplementedTypes().stream().map(impType -> new RelationType(unit.getNameAsString(),
                    impType.getNameAsString(),
                    RelationEnum.IMPEMENTS, " ")).collect(Collectors.toList()));
        } else {
            umlBuilder.append("interface ").append(unit.getName()).append("{\n");
        }
        umlBuilder.append("}\n");
    }



   /* private void readAllMethodsInClass(ClassOrInterfaceDeclaration unit, HashMap<String, String> variablesMap) {
        List<MethodDeclaration> methodslist = unit.getMethods();
        if (methodslist != null) {
            //methodslist.stream().filter(method -> method.getNameAsString().);
            for (MethodDeclaration method : methodslist) {
                if (method.getDeclarationAsString().startsWith("public")) {
                    //obtain usages of interface type inside methods
                    readMethodBody(method, unit.getNameAsString());
                    //check for getter ,setter
                    if (checkGetterSetter(method)) {
                        String vName = method.getNameAsString().substring(3).toLowerCase();
                        if (variablesMap.containsKey(vName)) {
                            String mapValue = variablesMap.get(vName);
                            mapValue = mapValue.replace(" - ", " + ");
                            variablesMap.put(vName, mapValue);
                        }
                    } else {
                        String returnType = method.getType().toString();
                        StringBuilder paramsbuilder = new StringBuilder();
                        for (Parameter params : method.getParameters()) {
                            paramsbuilder.append(params.getName()).append(" : ")
                                    .append(params.getType())
                                    .append(" ");
                        }
                        umlBuilder.append("+").append(method.getName())
                                .append('(').append(paramsbuilder).append(')')
                                .append(":").append(returnType).append("\n");
                    }
                }
            }
        }
    }

    private boolean checkGetterSetter(MethodDeclaration method) {
        String methodName = method.getNameAsString();
        int bodySize = method.getBody().orElse(new BlockStmt()).getStatements().size();
        if (methodName.startsWith("get") && method.getParameters().size() == 0 && bodySize == 1) {
            return true;
        } else if (methodName.startsWith("set") && method.getParameters().size() == 1 && bodySize == 1) {
            return true;
        }
        return false;
    }

    private void buildAttributes(HashMap<String, String> attributesMap) {
        for (String value : attributesMap.values()) {
            umlBuilder.append(value).append("\n");
        }
    }

    private void printRelations(ArrayList<RelationType> relations) {
        for (RelationType type : relations) {
            umlBuilder.append(type.toString()).append("\n");
        }
    }

    private RelationType obtainRelationFromType(Type type, ClassOrInterfaceDeclaration dec) {
        //Checking if
        if (!(type instanceof PrimitiveType) && !type.toString().equals("String")) {
            if (type instanceof ArrayType) {
                Type narrayType = ((ArrayType) type).getComponentType();
                if (!(narrayType instanceof PrimitiveType) &&
                        !narrayType.toString().equalsIgnoreCase("String")) {
                    return new RelationType(dec.getName().toString(),
                            type.toString(), RelationEnum.ASSOCIATE, " : *");
                }
            } else {

                if (typeMap.containsKey(type.toString())) {
                    if (typeMap.get(type.toString()).isInterface()) {
                        return new RelationType(dec.getName().toString(),
                                type.toString(), RelationEnum.INTERFACE_USES, " : uses");
                    }
                }
            }
        }
        return null;
    }

    private void readMethodBody(MethodDeclaration method, String className) {
        NodeList<Statement> statements = method.getBody().orElse(new BlockStmt()).getStatements();
        for (Statement statement : statements) {
            String words[] = statement.toString().trim().split(" ");
            for (String word : words) {
                if (typeMap.containsKey(word) && typeMap.get(word).isInterface()) {
                    relationsList.add(new RelationType(className,
                            word, RelationEnum.INTERFACE_USES, " : uses"));
                }
            }

        }
    }*/

}
