package processor;

import mutationproject.IMutationProcessor;
import mutationproject.MutationProject;
import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.reference.CtTypeReference;

import java.util.ArrayList;
import java.util.List;

public class ObjectMethodProcessor extends AbstractProcessor<CtMethod> implements IMutationProcessor {

    private CtMethod method;
    private List<CtStatement> backup;

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

        method = ctMethod;
        backup = new ArrayList<>(method.getBody().getStatements());
        method.getBody().getStatements().clear();

        MutationProject.testMutation(this, method);
    }

    @Override
    public void revertChanges() {
        method.getBody().getStatements().addAll(backup);
    }

    @Override
    public String getMutationDescription() {
        return null; // TODO
    }
}
