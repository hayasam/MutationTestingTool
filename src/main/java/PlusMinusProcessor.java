import spoon.processing.AbstractProcessor;
import spoon.reflect.code.BinaryOperatorKind;
import spoon.reflect.code.CtBinaryOperator;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class PlusMinusProcessor extends AbstractProcessor<CtMethod> {


    @Override
    public boolean isToBeProcessed(CtMethod candidate) {
        boolean isToBeProcessed = !(candidate.getSimpleName().equals("main") && candidate.isStatic()) // not static main method
                && !candidate.isAbstract() // not an interface method declaration
                && candidate.getAnnotation(org.junit.Test.class) == null; // not a Test
        if(isToBeProcessed) System.out.println("[Valid candidate] PlusMinus method " + candidate.getSimpleName());
        return isToBeProcessed;
    }

    public void process(CtMethod ctMethod) {

        List<CtStatement> backup = new ArrayList<>(ctMethod.getBody().getStatements());


        for(CtStatement st : ctMethod.getBody().getStatements()) {
            List<CtElement> elements = st.getElements(
                    (e) -> e instanceof CtBinaryOperator &&
                                (
                                       ((CtBinaryOperator) e).getKind() == BinaryOperatorKind.PLUS ||
                                       ((CtBinaryOperator) e).getKind() == BinaryOperatorKind.MINUS
                                )
                    );
            // TODO swap + -
        }



        MutationProject.testMutation(ctMethod);
        ctMethod.getBody().getStatements().addAll(backup);
    }
}
