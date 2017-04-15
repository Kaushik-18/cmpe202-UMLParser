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
            if (args.length < 2) {
                System.out.println(" Please enter input and output files path");
                return;
            }
            if (args.length == 4) {


            } else if (args.length == 2) {
                //Generating the required string
                CodeParser parser = new CodeParser();
                StringBuilder umlBuilder = parser.readCodeTree(args[0]);
                //passing generated string to PlantUMl
                createImage(args[1], umlBuilder.toString());
            }
        }
    }

    private static void createImage(String outputPath, String input) {
        SourceStringReader plantUmlReader = new SourceStringReader(input);
        try (FileOutputStream imageOutputStream = new FileOutputStream(outputPath)) {
            plantUmlReader.generateImage(imageOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
