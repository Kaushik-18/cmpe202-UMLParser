import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaushik on 25/2/17.
 */
public class CodeParser {

    private StringBuilder umlBuilder;

    public CodeParser() {
        umlBuilder = new StringBuilder();
    }

    public StringBuilder readCodeTree(String filePath) {
        File sourceFolder = new File(filePath);
        umlBuilder.append("@startuml\n");
        ArrayList<ClassOrInterfaceDeclaration> typelist = new ArrayList<ClassOrInterfaceDeclaration>();
        if (sourceFolder.exists()) {
            for (File source : sourceFolder.listFiles()) {
                if (source.getName().endsWith(".java")) {
                    try {
                        CompilationUnit unit = JavaParser.parse(source);
                        typelist.add(readClassOrInterfaceName(unit));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
            for (ClassOrInterfaceDeclaration dec : typelist) {
                readClassorInterfaceDetails(dec);
            }
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

    private void readClassorInterfaceDetails(ClassOrInterfaceDeclaration unit) {
        if (!unit.isInterface()) {
            umlBuilder.append("class ").append(unit.getName()).append("{\n");
        } else {
            umlBuilder.append("interface ").append(unit.getName()).append("{\n");
        }
        readAllAttributes(unit);
        readAllMethodsInClass(unit);
        umlBuilder.append("}\n");
    }

    private void parseExtendsImplementsRelation(ClassOrInterfaceDeclaration unit) {

    }

    private void readAllMethodsInClass(ClassOrInterfaceDeclaration unit) {
        List<MethodDeclaration> methodslist = unit.getMethods();
        if (methodslist != null) {
            for (MethodDeclaration methods : methodslist) {
                if (methods.getDeclarationAsString().startsWith("public"))
                    umlBuilder.append(" + ");
                else if (methods.getDeclarationAsString().startsWith("private"))
                    umlBuilder.append(" - ");
                umlBuilder.append(methods.getName()).append("()\n");
            }
        }
    }

    private void readAllAttributes(ClassOrInterfaceDeclaration unit) {
        List<FieldDeclaration> fieldslist = unit.getFields();
        if (fieldslist != null) {
            for (FieldDeclaration dec : fieldslist) {
                for (VariableDeclarator expr : dec.getVariables()) {
                    umlBuilder.append(expr.getName()).append(":").append(expr.getType()).append("\n");
                }
            }
        }
    }


}
