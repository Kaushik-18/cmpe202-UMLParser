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
public class AttributesParser {

    CodeParser _mainParser;

    public AttributesParser(CodeParser mainParser) {
        _mainParser = mainParser;
    }

    public void parseClassAttributes(ClassOrInterfaceDeclaration unit) {
        HashMap<String, String> variablesMap = new HashMap<>();
        List<FieldDeclaration> fieldslist = unit.getFields();
        if (fieldslist != null) {
            fieldslist.stream().filter(dec -> dec.toString().startsWith("public") || dec.toString().startsWith("private")).forEach(dec -> {
                for (VariableDeclarator expr : dec.getVariables()) {
                    Type type = expr.getType();
                    RelationType relation = obtainRelationFromType(type, unit);
                    if (relation != null) {
                        _mainParser.getRelationsList().add(relation);
                    } else {
                        String sign = dec.toString().startsWith("public") ? " + " : " - ";
                        String variableExpression = sign + expr.getName() + ":" + type;
                        variablesMap.put(expr.getName().toString(), variableExpression);
                    }
                }
            });
        }
    }

    private RelationType obtainRelationFromType(Type type, ClassOrInterfaceDeclaration dec) {
        if (!(type instanceof PrimitiveType) && !type.toString().equals("String")) {
            if (type instanceof ArrayType) {
                Type narrayType = ((ArrayType) type).getComponentType();
                if (!(narrayType instanceof PrimitiveType) &&
                        !narrayType.toString().equalsIgnoreCase("String")) {
                    return new RelationType(dec.getName().toString(),
                            type.toString(), RelationEnum.ASSOCIATE, " : *");
                }
            } else {
                return new RelationType(dec.getName().toString(),
                        type.toString(), RelationEnum.ASSOCIATE, " ");
            }
        }
        return null;
    }


}
