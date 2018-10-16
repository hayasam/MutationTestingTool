import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtReturn;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.reference.CtTypeReference;
import spoon.support.reflect.code.CtReturnImpl;

import java.util.Iterator;

public class PrimitiveTypeProcessor extends AbstractProcessor<CtMethod> {


    @Override
    public boolean isToBeProcessed(CtMethod candidate) {
        return candidate.getType().isPrimitive();

    }

    public void process(CtMethod ctMethod) {
        ctMethod.getBody().getStatements().clear();
        CtTypeReference type = ctMethod.getType();
        ctMethod.getBody().addStatement(new CtReturnImpl<>()); // TODO
    }
}
