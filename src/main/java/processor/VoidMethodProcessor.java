package processor;

import mutationproject.IMutationProcessor;
import mutationproject.MutationProject;
import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtMethod;

import java.util.ArrayList;
import java.util.List;

public class VoidMethodProcessor extends AbstractProcessor<CtMethod> implements IMutationProcessor {

    private CtMethod method;
    private List<CtStatement> backup;

    @Override
    public boolean isToBeProcessed(CtMethod candidate) {
        return candidate.getType().equals(getFactory().Type().voidPrimitiveType()) // not primitive type
                && !(candidate.getSimpleName().equals("main") && candidate.isStatic()) // not static main method
                && !candidate.isAbstract() // not an interface method declaration
                && candidate.getAnnotation(org.junit.Test.class) == null; // not a Test
    }

    public void process(CtMethod ctMethod) {

        method = ctMethod;
        backup = new ArrayList<>(method.getBody().getStatements());
        method.getBody().getStatements().clear();

        System.out.println("void " + method.getSimpleName()); // TODO remove

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
