import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;

import java.util.Iterator;

public class VoidMethodProcessor extends AbstractProcessor<CtMethod> {


    @Override
    public boolean isToBeProcessed(CtMethod candidate) {
        return candidate.getType().equals(getFactory().Type().voidPrimitiveType());
    }

    public void process(CtMethod ctMethod) {
        ctMethod.getBody().getStatements().clear();
        System.out.println("void " + ctMethod.getSimpleName());
        // TODO -> ignore main and @Test

        CtElement el = ctMethod;
        while(!(el instanceof CtType) || !((CtType)el).isTopLevel())
            el = el.getParent();

        System.out.println(((CtType)el).getShortRepresentation());
        MutationProject.testMutation(((CtType)el).getQualifiedName());
    }
}
