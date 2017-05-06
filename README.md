## UML parser personal project assigned in CMPE 202 class

Project Requirments :- To convert Java Source code files into UML class diagram and sequence diagram .Final executable will be a jar file which can be run from the command line


The following are the libraries and tools I have used for creating the UML parser :-
Note :- All the code is written in Java and requires Java 1.8 to compile and run
PlantUML​ :- for generating the sequence and class diagram image files from given input string.
Note :- On Ubuntu ,we also require the graphwiz package install using sudo apt-get install graphviz

JavaParser​ :- for parsing the source code files when we are generating class diagram.It generates an
Abstract Syntax Tree which I use to generate the input required by PlantUML.

## For Class Diagram ​ :-
the final jar generated expects two inputs :- the path of input folder and path of destination where image will be
saved. We can use the jar file directly or use the shell script in uml_parser_pack folder.

## Sequence Diagram :-
For sequence diagram ,I need the following additional tools :-
AspectJ Compiler (ajc) from eclipse.org
AspectJ runtime aspectjrt.jar from eclipse.org
I use AspectJ to define pointcuts (in my SequenceAspect.aj file) which allows me to trace the method calls
made at runtime. I then use this trace to generate input for the plantUML library which creates the actual
image.

## To run the sequence diagram portion :-
Go to uml_parser_pack folder contains a bash script for running sequence diagram generation code.
Script requires 3 inputs :- “seq” “input folder path” “output path”
On running the script a temp folder is created in which the source code and SequenceAspect.aj file is copied .
the ajc compiler is used to compile the source coe in the temp folder and weave in the aspectJ portions into
the code.
Run the code as a normal Java program ; the traceis written to a sequence.txt file which is given as input to
umlparser jar file for generating the sequence diagram.
Note :- For now ,the script assumes that the ‘main()’ method is defined in a class called Main
