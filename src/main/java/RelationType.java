/**
 * Created by kaushik on 1/3/17.
 */
public class RelationType {
    String startClass;
    String endClass;
    RelationEnum r_enum;
    String label;

    RelationType(String startClass, String endClass, RelationEnum r_enum, String label) {
        this.endClass = endClass;
        this.startClass = startClass;
        this.label = label;
        this.r_enum = r_enum;
    }

    @Override
    public String toString() {
        return endClass + r_enum.symbol + startClass + label;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof RelationType) {
            RelationType t = (RelationType) o;
            if (t.endClass.equalsIgnoreCase(this.endClass) &&
                    t.startClass.equalsIgnoreCase(this.startClass) &&
                    t.getRelation() == this.getRelation())
                return true;
            else
                return false;
        } else
            return false;
    }

    public RelationEnum getRelation() {
        return r_enum;
    }
}

enum RelationEnum {
    EXTENDS(" <|-- "),
    ASSOCIATE(" - "),
    IMPEMENTS(" <|.. "),
    ASSOCIATE_MANY(" \"0...*\" - "),
    DEPENCENCY(" <.. ");

    String symbol;

    RelationEnum(String symbol) {
        this.symbol = symbol;
    }

    String getSymbol() {
        return symbol;
    }
}
