/**
 * Created by kaushik on 24/2/17.
 */
public class UMLGenerator {
    public static void main(String args[]) {
        if (args != null) {
            //TODO read output and input path from command line
            CodeParser parser = new CodeParser();
            parser.readCodeTree("/home/kaushik/Downloads/Git Projects/testjava");
        }
    }
}
