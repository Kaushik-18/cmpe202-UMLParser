import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.type.*;
import com.sun.org.apache.xml.internal.dtm.ref.ExtendedType;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by kaushik on 25/2/17.
 */
public class CodeParser {

    private StringBuilder umlBuilder;
    private HashMap<String, ClassOrInterfaceDeclaration> typeMap;
    private ArrayList<RelationType> relationsList;

    public CodeParser() {
        umlBuilder = new StringBuilder();
        relationsList = new ArrayList<>();
    }

    public StringBuilder readCodeTree(String filePath) {
        File sourceFolder = new File(filePath);
        umlBuilder.append("@startuml\n skinparam classAttributeIconSize 0\n");
        typeMap = new HashMap<>();
        if (sourceFolder.exists()) {
            for (File source : sourceFolder.listFiles()) {
                if (source.getName().endsWith(".java")) {
                    try {
                        CompilationUnit unit = JavaParser.parse(source);
                        ClassOrInterfaceDeclaration dec = readClassOrInterfaceName(unit);
                        typeMap.put(dec.getName().toString(), dec);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
            for (ClassOrInterfaceDeclaration dec : typeMap.values()) {
                readClassOrInterfaceDetails(dec);
            }
            printRelations(relationsList);
            umlBuilder.append("\n@enduml");
            System.out.print("uml " + umlBuilder.toString());
        }
        return umlBuilder;
    }

    private ClassOrInterfaceDeclaration readClassOrInterfaceName(CompilationUnit unit) {
        List<Node> nodelist = unit.getChildNodes();
        for (Node node : nodelist) {
            if (node instanceof ClassOrInterfaceDeclaration) {
                return (ClassOrInterfaceDeclaration) node;
            }
        }
        return null;
    }

    private void readClassOrInterfaceDetails(ClassOrInterfaceDeclaration unit) {
        if (!unit.isInterface()) {
            umlBuilder.append("class ").append(unit.getName()).append("{\n");
            for (ClassOrInterfaceType exType : unit.getExtendedTypes()) {
                relationsList.add(new RelationType(unit.getNameAsString(),
                        exType.getNameAsString(),
                        RelationEnum.EXTENDS, " "));
            }
            for (ClassOrInterfaceType impType : unit.getImplementedTypes()) {
                relationsList.add(new RelationType(unit.getNameAsString(),
                        impType.getNameAsString(),
                        RelationEnum.IMPEMENTS, " "));
            }
        } else {
            umlBuilder.append("interface ").append(unit.getName()).append("{\n");
        }
        HashMap<String, String> varMap = readAllAttributes(unit);
        readAllMethodsInClass(unit, varMap);
        printAttributes(varMap);
        umlBuilder.append("}\n");
    }

    private void readAllMethodsInClass(ClassOrInterfaceDeclaration unit, HashMap<String, String> variablesMap) {
        List<MethodDeclaration> methodslist = unit.getMethods();
        if (methodslist != null) {
            for (MethodDeclaration method : methodslist) {
                if (method.getDeclarationAsString().startsWith("public")) {
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
        int bodySize = 0;
        if (method.getBody().isPresent()) {
            bodySize = method.getBody().get().getStatements().size();
        }
        if (methodName.startsWith("get") && method.getParameters().size() == 0 && bodySize == 1) {
            return true;
        } else if (methodName.startsWith("set") && method.getParameters().size() == 1 && bodySize == 1) {
            return true;
        }
        return false;
    }

    //read all attributes declared in class;build a HashMap for later comparison ie finding getters and setters
    private HashMap<String, String> readAllAttributes(ClassOrInterfaceDeclaration unit) {
        HashMap<String, String> variablesMap = new HashMap<>();
        List<FieldDeclaration> fieldslist = unit.getFields();
        if (fieldslist != null) {
            for (FieldDeclaration dec : fieldslist) {
                if (dec.toString().startsWith("public") || dec.toString().startsWith("private")) {
                    for (VariableDeclarator expr : dec.getVariables()) {
                        Type type = expr.getType();
                        obtainType(type,unit);
                        String sign = dec.toString().startsWith("public") ? " + " : " - ";
                        String variableExpression = sign + expr.getName() + ":" + type;
                        variablesMap.put(expr.getName().toString(), variableExpression);
                    }
                }
            }
        }
        return variablesMap;
    }

    private void printAttributes(HashMap<String, String> attributesMap) {
        for (String value : attributesMap.values()) {
            umlBuilder.append(value).append("\n");
        }
    }

    private void printRelations(ArrayList<RelationType> relations) {
        for (RelationType type : relations) {
            umlBuilder.append(type.toString()).append("\n");
        }
    }

    private void obtainType(Type type, ClassOrInterfaceDeclaration dec) {
        if (!(type instanceof PrimitiveType) && !type.toString().equals("String")) {
            if (typeMap.containsKey(type.toString())) {
                relationsList.add(new RelationType(dec.getName().toString(),
                        type.toString(), RelationEnum.ASSOCIATE, "uses"));
            }
        }

    }

}
