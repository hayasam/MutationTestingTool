package processor;

import mutationproject.MutationProcessor;
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

public class AndOrProcessor extends MutationProcessor<CtBinaryOperator> {

    private CtBinaryOperator ctBinaryOperator;
    private CtMethod met;

    @Override
    public boolean isToBeProcessed(CtBinaryOperator candidate) {

        CtElement el = candidate;
        do {
            el = el.getParent();
        }
        while(el != null && !(el instanceof CtMethod));

        if(el != null) {
            this.met = (CtMethod) el;
            boolean isToBeProcessed = !(this.met.getSimpleName().equals("main") && this.met.isStatic()) // not static main method
                    && !this.met.isAbstract() // not an interface method declaration
                    && this.met.getAnnotation(org.junit.Test.class) == null // not a Test
                    && (candidate.getKind() == BinaryOperatorKind.AND || candidate.getKind() == BinaryOperatorKind.OR);
            if(isToBeProcessed) System.out.println("[Valid candidate] AndOr method " + this.met.getSimpleName());
            return isToBeProcessed;
        }
        return false;
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
        return String.format("Swap && and || between %s and %s in method %s",
                this.ctBinaryOperator.getLeftHandOperand(),
                this.ctBinaryOperator.getRightHandOperand(),
                this.met.getSimpleName());
    }
}
