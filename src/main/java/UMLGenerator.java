import net.sourceforge.plantuml.SourceStringReader;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by kaushik on 24/2/17.
 */
public class UMLGenerator {
    public static void main(String args[]) {
        if (args != null) {
            //TODO read output and input path from command line
            //Generating the required string
            CodeParser parser = new CodeParser();
            StringBuilder umlBuilder = parser.readCodeTree("/home/kaushik/Downloads/202/cmpe202/umlparser/uml-parser-test-1");
            //passing generated string to PlantUMl
            SourceStringReader plantUmlReader = new SourceStringReader(umlBuilder.toString());
            try (FileOutputStream imageOutputStream = new FileOutputStream("/home/kaushik/Downloads/Git Projects/uml.png")) {
                plantUmlReader.generateImage(imageOutputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
