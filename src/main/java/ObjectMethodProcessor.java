import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.reference.CtTypeReference;

import java.util.ArrayList;
import java.util.List;

public class ObjectMethodProcessor extends AbstractProcessor<CtMethod> {

    @Override
    public boolean isToBeProcessed(CtMethod candidate){
        boolean isToBeProcessed = (candidate.getType().equals(getFactory().Type().objectType().getTopLevelType()) // Object type
                || candidate.getType().getSuperclass() != null) // extends Object
                &&!(candidate.getSimpleName().equals("main")&&candidate.isStatic()) // not main
                &&!candidate.isAbstract() // not an interface method declaration
                &&candidate.getAnnotation(org.junit.Test.class)==null; // not a Test
        if(isToBeProcessed) System.out.println("[Valid candidate] Object return type " + candidate.getSimpleName());
        return isToBeProcessed;
    }

    public void process(CtMethod ctMethod){

        List<CtStatement> backup=new ArrayList<>(ctMethod.getBody().getStatements());
        ctMethod.getBody().getStatements().clear();
        

        MutationProject.testMutation(ctMethod);
//        ctMethod.getBody().getStatements().addAll(backup);
    }

}
