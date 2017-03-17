/**
 * Created by kaushik on 1/3/17.
 */
public class RelationType {
    private String startClass;
    private String endClass;
    private RelationEnum r_enum;
    private String label;

    RelationType(String startClass, String endClass, RelationEnum r_enum, String label) {
        this.endClass = endClass;
        this.startClass = startClass;
        this.label = label;
        this.r_enum = r_enum;
    }

    @Override
    public String toString() {
        return endClass + r_enum.symbol + startClass +label;
    }
}

enum RelationEnum {
    EXTENDS("<|--"),
    ASSOCIATE("-"),
    IMPEMENTS("<|.."),
    DEPENCENCY("<..");

    String symbol;

    RelationEnum(String symbol) {
        this.symbol = symbol;
    }

    String getSymbol() {
        return symbol;
    }
}
