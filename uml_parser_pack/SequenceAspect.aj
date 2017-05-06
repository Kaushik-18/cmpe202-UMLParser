import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import java.util.LinkedHashSet;
import java.util.ArrayList;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.charset.Charset;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.charset.Charset;
import java.io.IOException;

public aspect SequenceAspect{
  String mainClass ;
  StringBuilder seqBuilder ;
  LinkedHashSet<String> targetSet;

 pointcut mainCall() : !within(SequenceAspect) && 
                        execution(public static void main(..));

pointcut traced() : !within(SequenceAspect) && 
                      call(* *.*(..)) && 
                      !call(* java..*.*(..)) && 
                      !cflow(execution(*.new(..)));

  before() : mainCall() {
    seqBuilder = new StringBuilder();
    seqBuilder.append("@startuml\n skinparam classAttributeIconSize 0\n");
    targetSet = new LinkedHashSet();
    mainClass = thisJoinPoint.getSourceLocation().getWithinType().getName();
  }

  before() : traced() {
    String source  = null;
    if(thisJoinPoint.getThis() != null){
      source = thisJoinPoint.getThis().getClass().getName();
    }
    else{
      source = mainClass;
    }

    String target = null; 
    if (thisJoinPoint.getTarget() != null) {
      target = thisJoinPoint.getTarget().getClass().getName();
    } 

   
    Signature mSignature = thisJoinPoint.getSignature()  ;
    String[] signatureArray = mSignature.toString().split(" ");
    String[] points = signatureArray[1].split("\\.");
    seqBuilder.append(source + "->"+target).append(":")
    .append(points[1])
    .append(" : ")
    .append(signatureArray[0]);
     if(!targetSet.contains(target)){
       targetSet.add(target);
       seqBuilder.append("\n activate ").append(target);
    }
    seqBuilder.append("\n") ;
  }

  after() : traced() {
     
  }

  after() : mainCall() {
     seqBuilder.append("\n@enduml");
     Path file = Paths.get("sequence.txt");
    try {
        Files.write(file, seqBuilder.toString().getBytes());
    } catch(IOException ie) {
        ie.printStackTrace();
    }
  }
}
