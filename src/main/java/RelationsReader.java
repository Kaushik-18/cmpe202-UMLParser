import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.type.ArrayType;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.Type;
import com.sun.org.apache.bcel.internal.classfile.Code;

import java.util.*;

/**
 * Created by kaushik on 27/3/17.
 */
public class RelationsReader {

    private ArrayList<RelationType> _relationsList;
    private CodeParser _mParser;
    private HashMap<String, RelationType> _asscMap;

    public RelationsReader(CodeParser parser) {
        _relationsList = new ArrayList<>();
        this._mParser = parser;
        _asscMap = new HashMap<>();
    }

    public boolean obtainRelationFromType(Type type, ClassOrInterfaceDeclaration dec) {
        if(dec.isInterface()){
            return false ;
        }
        String typeStr = type.toString();
        if (!(type instanceof PrimitiveType) && !typeStr.equals("String")) {
            if (type instanceof ArrayType) {
                Type narrayType = ((ArrayType) type).getComponentType();
                if (!(narrayType instanceof PrimitiveType) &&
                        !narrayType.toString().equalsIgnoreCase("String")) {
                    updateRelationsList(new RelationType(dec.getName().toString(),
                            type.toString(), RelationEnum.ASSOCIATE_MANY, " "));
                }
            } else if (typeStr.contains("Collection") || typeStr.contains("ArrayList")) {
                String collectionType = typeStr.substring(typeStr.indexOf("<") + 1, typeStr.indexOf(">"));
                updateRelationsList(new RelationType(dec.getName().toString(), collectionType,
                        RelationEnum.ASSOCIATE_MANY, " "));
                return true;
            } else {
                updateRelationsList(new RelationType(dec.getName().toString(),
                        type.toString(), RelationEnum.ASSOCIATE, " "));
                return true;
            }
        }
        return false;
    }

    //checking for already added relation between classes
    void updateRelationsList(RelationType relation) {
        if (relation.getRelation() == RelationEnum.ASSOCIATE ||
                relation.getRelation() == RelationEnum.ASSOCIATE_MANY) {

            String key1 = relation.startClass + relation.endClass;
            String key2 = relation.endClass + relation.startClass;

            if (!_asscMap.containsKey(key1) && !_asscMap.containsKey(key2)) {
                _asscMap.put(key1, relation);
            } else {
                RelationType storedRelation = _asscMap.get(_asscMap.containsKey(key1) ? key1 : key2);
                RelationType updatedRelation = null;
                if (storedRelation.getRelation() == RelationEnum.ASSOCIATE && relation.getRelation() == RelationEnum.ASSOCIATE) {
                    ///////
                    updatedRelation = new RelationType(relation.startClass, relation.endClass, RelationEnum.ASSOCIATE_ONE_TO_ONE, "");
                } else if (storedRelation.getRelation() == RelationEnum.ASSOCIATE && relation.getRelation() == RelationEnum.ASSOCIATE_MANY) {
                    /////////
                    updatedRelation = new RelationType(relation.startClass, relation.endClass, RelationEnum.ASSOCIATE_ONE_TO_MANY, "");

                } else if (storedRelation.getRelation() == RelationEnum.ASSOCIATE_MANY && relation.getRelation() == RelationEnum.ASSOCIATE) {
                    ///////
                    updatedRelation = new RelationType(storedRelation.startClass, storedRelation.endClass, RelationEnum.ASSOCIATE_ONE_TO_MANY, "");
                }
                _asscMap.put(_asscMap.containsKey(key1) ? key1 : key2 , updatedRelation);
            }

        } else {
            int pos = _relationsList.indexOf(relation);
            if (pos < 0) {
                _relationsList.add(relation);
            }
        }
    }

    void addRelations(List<RelationType> reList) {
        _relationsList.addAll(reList);
    }

    void printRelations() {
        for (RelationType type : _relationsList) {
            _mParser.getUmlBuilder().append(type.toString()).append("\n");
        }
        Collection<RelationType> relValues = _asscMap.values();
        for (RelationType assType : relValues) {
            _mParser.getUmlBuilder().append(assType.toString()).append("\n");
        }

    }

}


