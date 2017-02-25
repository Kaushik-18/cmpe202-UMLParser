import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaushik on 25/2/17.
 */
public class CodeParser {

    public CodeParser() {
    }

    public void readCodeTree(String filePath) {
        File sourceFolder = new File(filePath);
        if (sourceFolder.exists()) {
            for (File source : sourceFolder.listFiles()) {
                if (source.getName().endsWith(".java")) {
                    try {
                        CompilationUnit unit = JavaParser.parse(source);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private ArrayList<String> getImportists(CompilationUnit cu) {
        ArrayList<String> imports = new ArrayList<String>();
        List<ImportDeclaration> importDeclarations = cu.getImports();
        for (ImportDeclaration imported : importDeclarations) {
            imports.add(imported.getNameAsString());
        }
        System.out.print(imports);
        return imports;
    }

    private void getTypeDeclartions() {
      
    }

}
