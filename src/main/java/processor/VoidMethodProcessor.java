package processor;

import mutationproject.MutationProcessor;
import mutationproject.MutationProject;
import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtMethod;

import java.util.ArrayList;
import java.util.List;

public class VoidMethodProcessor extends MutationProcessor<CtMethod> {

    private CtMethod method;
    private List<CtStatement> backup;

    @Override
    public boolean isToBeProcessed(CtMethod candidate) {
        boolean isToBeProcessed =  candidate.getType().equals(getFactory().Type().voidPrimitiveType()) // not primitive type
                && !(candidate.getSimpleName().equals("main") && candidate.isStatic()) // not static main method
                && !candidate.isAbstract() // not an interface method declaration
                && candidate.getAnnotation(org.junit.Test.class) == null; // not a Test;
        if (isToBeProcessed) System.out.println("[Valid candidate] void method " + candidate.getSimpleName());
        return isToBeProcessed;
    }

    public void process(CtMethod ctMethod) {

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
        return "Remove body in void method " + method.getSimpleName();
    }
}
