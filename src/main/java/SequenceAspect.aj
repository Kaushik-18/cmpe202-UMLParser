
/**
 * Created by kaushik on 20/4/17.
 */
public aspect SequenceAspect {

    pointcut trace(): call(* *(..));

    after() returning : trace() {
        //System.out.println("  Traces !!");
    }

}

