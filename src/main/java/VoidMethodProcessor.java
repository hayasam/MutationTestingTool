import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;

import java.util.ArrayList;
import java.util.List;

public class VoidMethodProcessor extends AbstractProcessor<CtMethod> {


    @Override
    public boolean isToBeProcessed(CtMethod candidate) {
        return candidate.getType().equals(getFactory().Type().voidPrimitiveType()) // not primitive type
                && !(candidate.getSimpleName().equals("main") && candidate.isStatic()) // not static main method
                && !candidate.isAbstract(); // not an interface method declaration


        // Hint to ignore @Test (requires importing JUnit)
        //candidate.getAnnotation(junit.org.Test.class).getAnnotationType().equals(...);
    }

    public void process(CtMethod ctMethod) {

        List<CtStatement> backup = new ArrayList<>(ctMethod.getBody().getStatements());
        ctMethod.getBody().getStatements().clear();

        System.out.println("void " + ctMethod.getSimpleName()); // TODO remove

        // TODO -> ignore @Test

        MutationProject.testMutation(ctMethod);
        ctMethod.getBody().getStatements().addAll(backup);
    }
}
