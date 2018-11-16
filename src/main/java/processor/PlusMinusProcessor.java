package processor;

import mutationproject.IMutationProcessor;
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

public class PlusMinusProcessor extends AbstractProcessor<CtBinaryOperator> implements IMutationProcessor {

    private CtBinaryOperator ctBinaryOperator;

    @Override
    public boolean isToBeProcessed(CtBinaryOperator candidate) {

        CtElement el = candidate;
        do {
            el = el.getParent();
        }
        while(el != null && !(el instanceof CtMethod));

        if(el != null) {
            CtMethod met = (CtMethod) el;
            boolean isToBeProcessed = !(met.getSimpleName().equals("main") && met.isStatic()) // not static main method
                    && !met.isAbstract() // not an interface method declaration
                    && met.getAnnotation(org.junit.Test.class) == null; // not a Test
            if(isToBeProcessed) System.out.println("[Valid candidate] PlusMinus method " + met.getSimpleName());
            return isToBeProcessed;
        }
        else return false;
    }

    private static void swapPlusMinus(CtBinaryOperator bo) {
        if(bo.getKind() == BinaryOperatorKind.PLUS) bo.setKind(BinaryOperatorKind.MINUS);
        else if(bo.getKind() == BinaryOperatorKind.MINUS) bo.setKind(BinaryOperatorKind.PLUS);
    }

    public void process(CtBinaryOperator ctBinaryOperator) {
        this.ctBinaryOperator = ctBinaryOperator;
        swapPlusMinus(this.ctBinaryOperator);
        MutationProject.testMutation(this, this.ctBinaryOperator);
    }

    @Override
    public void revertChanges() {
        swapPlusMinus(this.ctBinaryOperator);
    }

    @Override
    public String getMutationDescription() {
        return null; // TODO
    }
}
