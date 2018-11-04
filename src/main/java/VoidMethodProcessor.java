import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtMethod;

import java.util.ArrayList;
import java.util.List;

public class VoidMethodProcessor extends AbstractProcessor<CtMethod> {


    @Override
    public boolean isToBeProcessed(CtMethod candidate) {
        return candidate.getType().equals(getFactory().Type().voidPrimitiveType()) // not primitive type
                && !(candidate.getSimpleName().equals("main") && candidate.isStatic()) // not static main method
                && !candidate.isAbstract() // not an interface method declaration
                && candidate.getAnnotation(org.junit.Test.class) == null; // not a Test
    }

    public void process(CtMethod ctMethod) {

        List<CtStatement> backup = new ArrayList<>(ctMethod.getBody().getStatements());
        ctMethod.getBody().getStatements().clear();

        System.out.println("void " + ctMethod.getSimpleName()); // TODO remove

        MutationProject.testMutation(ctMethod);
        ctMethod.getBody().getStatements().addAll(backup);
    }
}
