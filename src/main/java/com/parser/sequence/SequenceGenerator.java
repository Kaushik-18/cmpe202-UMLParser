package com.parser.sequence;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SequenceGenerator {

    public SequenceGenerator(String codePath) {
        /*File codeFolder = new File(codePath);
        String mainFilePath = getMainMethodPath(codeFolder);
        try {
            //runProcess("sudo ajc -1.8 /home/kaushik/Downloads/202/personalproject/sequence*//*.java  /home/kaushik/Downloads/202/personalproject/sequence*//*.aj");
            runProcess("java -cp /home/kaushik/Downloads/202/personalproject/sequence:/home/kaushik/aspectj1.8/lib/aspectjrt.jar Main");

        } catch (Exception e) {
            e.printStackTrace();
        }

        // ajc -cp aspectjrt
*/

    }


    /*private String getMainMethodPath(File folderPath) {
        return null;
    }


    private static void runProcess(String command) throws Exception {
        Process pro = Runtime.getRuntime().exec(command);
        printLines(command + " stdout:", pro.getInputStream());
        printLines(command + " stderr:", pro.getErrorStream());
        pro.waitFor();
    }

    private static void printLines(String name, InputStream ins) throws Exception {
        String line = null;
        BufferedReader in = new BufferedReader(
                new InputStreamReader(ins));
        while ((line = in.readLine()) != null) {
            System.out.println(name + " " + line);
        }
    }
*/

}
