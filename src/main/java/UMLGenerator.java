import net.sourceforge.plantuml.SourceStringReader;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.parser.sequence.*;

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
            if (args.length == 2) {
                //Generating the required string
                CodeParser parser = new CodeParser();
                StringBuilder umlBuilder = parser.readCodeTree(args[0]);
                //passing generated string to PlantUMl
                System.out.println("Creating class diagram ...");
                createImage(args[1], umlBuilder.toString());
            } else if (args.length == 3) {
                System.out.println("Generating Sequence Diagram at :- " + args[2]);
                try {
                    String content = new String(Files.readAllBytes(Paths.get("sequence.txt")));
                    createImage(args[2], content);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private static void createImage(String outputPath, String input) {
        SourceStringReader plantUmlReader = new SourceStringReader(input);
        try (FileOutputStream imageOutputStream = new FileOutputStream(outputPath)) {
            plantUmlReader.generateImage(imageOutputStream);
            System.out.println("Diagram generated");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
