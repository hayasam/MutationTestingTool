package processor;

import mutationproject.MutationProject;
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

    private static void swapPlusMinus(CtBinaryOperator bo) {
        if(bo.getKind() == BinaryOperatorKind.PLUS) bo.setKind(BinaryOperatorKind.MINUS);
        else if(bo.getKind() == BinaryOperatorKind.MINUS) bo.setKind(BinaryOperatorKind.PLUS);
    }

    public void process(CtMethod ctMethod) {

        // List<CtStatement> backup = new ArrayList<>(ctMethod.getBody().getStatements());

        for(CtStatement st : ctMethod.getBody().getStatements()) {
            st.getElements(e->true).forEach((e) -> {
                if(e instanceof CtBinaryOperator) {
                    CtBinaryOperator bo = (CtBinaryOperator) e;
                    swapPlusMinus(bo);
                    // MutationProject.testMutation(ctMethod);
                    swapPlusMinus(bo);
                }
            });
        }

        // ctMethod.getBody().getStatements().addAll(backup);
    }
}
