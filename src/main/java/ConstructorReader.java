import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.Parameter;

import java.util.List;

/**
 * Created by kaushik on 28/3/17.
 */
public class ConstructorReader {

    private CodeParser _mainParser;

    public ConstructorReader(CodeParser parser) {
        this._mainParser = parser;
    }

    public void readClassConstructors(ClassOrInterfaceDeclaration unit) {
        NodeList<BodyDeclaration<?>> list = unit.getMembers();

        for (BodyDeclaration node : list) {
            if (node instanceof ConstructorDeclaration) {
                ConstructorDeclaration l = (ConstructorDeclaration) node;
                StringBuilder paramsbuilder = new StringBuilder();
                for (Parameter params : l.getParameters()) {
                    paramsbuilder.append(params.getName()).append(" : ").append(params.getType()).append(" ");
                    String paramType = params.getType().toString();
                    if (_mainParser._getTypeMap().containsKey(paramType) &&
                            (_mainParser._getTypeMap().get(paramType).isInterface())) {
                        _mainParser.getRelationsBuilder().updateRelationsList(new RelationType(unit.getNameAsString(),
                                params.getType().toString(), RelationEnum.DEPENCENCY, " : using "));
                    }
                }
                _mainParser.getUmlBuilder().append("+").append(l.getName())
                        .append('(').append(paramsbuilder).append(')')
                        .append("\n");
            }

        }
    }
}
