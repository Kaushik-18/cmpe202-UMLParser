import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;

import java.util.HashMap;
import java.util.List;

/**
 * Created by kaushik on 27/3/17.
 */
public class MethodsReader {

    private CodeParser _mParser;

    public MethodsReader(CodeParser parser) {
        this._mParser = parser;
    }

    void readMethodBody(MethodDeclaration method, String className) {
        NodeList<Statement> statements = method.getBody().orElse(new BlockStmt()).getStatements();
        for (Statement statement : statements) {
            String words[] = statement.toString().trim().split(" ");
            for (String word : words) {
                if (_mParser._getTypeMap().containsKey(word) &&
                        (_mParser._getTypeMap().get(word).isInterface())) {
                    _mParser.getRelationsBuilder().updateRelationsList(new RelationType(className,
                            word, RelationEnum.DEPENCENCY, "  "));
                }
            }
        }
    }

    void readAllMethodsInClass(ClassOrInterfaceDeclaration unit, HashMap<String, String> variablesMap) {
        List<MethodDeclaration> methodslist = unit.getMethods();
        if (methodslist != null) {
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
                            paramsbuilder.append(params.getName()).append(" : ").append(params.getType()).append(" ");
                            String paramType = params.getType().toString();
                            if (_mParser._getTypeMap().containsKey(paramType) &&
                                    (_mParser._getTypeMap().get(paramType).isInterface()) &&
                                    !unit.isInterface()) {
                                _mParser.getRelationsBuilder().updateRelationsList(new RelationType(unit.getNameAsString(),
                                        params.getType().toString(), RelationEnum.DEPENCENCY, "  "));
                            }
                        }
                        _mParser.getUmlBuilder().append("+").append(method.getName())
                                .append('(').append(paramsbuilder).append(')')
                                .append(":").append(returnType).append("\n");
                    }
                }
            }
        }
    }

    boolean checkGetterSetter(MethodDeclaration method) {
        String methodName = method.getNameAsString();
        int bodySize = method.getBody().orElse(new BlockStmt()).getStatements().size();
        if (methodName.startsWith("get") && method.getParameters().size() == 0 && bodySize == 1) {
            return true;
        } else if (methodName.startsWith("set") && method.getParameters().size() == 1 && bodySize == 1) {
            return true;
        }
        return false;
    }

}
