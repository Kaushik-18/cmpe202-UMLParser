import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.type.ArrayType;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.Type;

import java.util.HashMap;
import java.util.List;

/**
 * Created by kaushik on 12/3/17.
 */
public class AttributesReader {

    private CodeParser _mainParser;

    AttributesReader(CodeParser mainParser) {
        _mainParser = mainParser;
    }

    HashMap<String, String> parseClassAttributes(ClassOrInterfaceDeclaration unit) {
        HashMap<String, String> variablesMap = new HashMap<>();
        List<FieldDeclaration> fieldslist = unit.getFields();
        if (fieldslist != null) {
            fieldslist.stream().filter(dec -> dec.toString().startsWith("public")
                    || dec.toString().startsWith("private"))
                    .forEach(dec -> {
                        for (VariableDeclarator expr : dec.getVariables()) {
                            Type type = expr.getType();
                             if(!_mainParser.getRelationsBuilder().obtainRelationFromType(type, unit))
                            {
                                String sign = dec.toString().startsWith("public") ? " + " : " - ";
                                String variableExpression = sign + expr.getName() + ":" + type;
                                variablesMap.put(expr.getName().toString(), variableExpression);
                            }
                        }
                    });
        }
        return variablesMap;
    }

    public void printAttributes(HashMap<String, String> attributesMap) {
        for (String value : attributesMap.values()) {
            _mainParser.getUmlBuilder().append(value).append("\n");
        }
    }

}


