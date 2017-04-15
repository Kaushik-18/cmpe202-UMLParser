import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class SequenceGenerator {


    public void execute(String fileName, String className, String methodName) {
        File sourceFolder = new File(fileName);
        if (sourceFolder.exists()) {
            for (File source : sourceFolder.listFiles()) {
                if (source.getName().endsWith(".java")) {
                    try {
                        CompilationUnit unit = JavaParser.parse(source);
                        List<Node> nodelist = unit.getChildNodes();
                        for (Node node : nodelist) {
                            if (node instanceof ClassOrInterfaceDeclaration) {
                                ClassOrInterfaceDeclaration dec = (ClassOrInterfaceDeclaration) node;
                                dec.getMethods();

                            }

                        }

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }


    private void readStartMethodBody(String fileName, String methodName) {
        File sourceFile = new File(fileName);
        if (sourceFile.exists() && sourceFile.getName().endsWith(".java")) {
            try {
                CompilationUnit unit = JavaParser.parse(sourceFile);



            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }


}
