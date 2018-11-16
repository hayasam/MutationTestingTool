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

public class AndOrProcessor extends AbstractProcessor<CtBinaryOperator> implements IMutationProcessor {

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
            if(isToBeProcessed) System.out.println("[Valid candidate] AndOr method " + met.getSimpleName());
            return isToBeProcessed;
        }
        else return false;
    }

    private static void swapAndOr(CtBinaryOperator bo) {
        if(bo.getKind() == BinaryOperatorKind.AND) bo.setKind(BinaryOperatorKind.OR);
        else if(bo.getKind() == BinaryOperatorKind.OR) bo.setKind(BinaryOperatorKind.AND);
    }

    public void process(CtBinaryOperator ctBinaryOperator) {
        this.ctBinaryOperator = ctBinaryOperator;
        swapAndOr(this.ctBinaryOperator);
        MutationProject.testMutation(this, this.ctBinaryOperator);
    }

    @Override
    public void revertChanges() {
        swapAndOr(this.ctBinaryOperator);
    }

    @Override
    public String getMutationDescription() {
        return null; // TODO
    }
}
