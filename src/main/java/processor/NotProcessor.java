package processor;

import mutationproject.MutationProcessor;
import mutationproject.MutationProject;
import spoon.processing.AbstractProcessor;
import spoon.reflect.code.*;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;
import spoon.support.reflect.code.CtUnaryOperatorImpl;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class NotProcessor extends MutationProcessor<CtExpression> {

    private CtExpression ctExpression;
    private CtMethod met;

    @Override
    public boolean isToBeProcessed(CtExpression candidate) {

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
                    && (candidate.getType().getTypeDeclaration().getSimpleName().equals("boolean")); // bool expr

            if(isToBeProcessed) System.out.println("[Valid candidate] !Not exp " + candidate.toString());
            return isToBeProcessed;
        }
        return false;

    }

    public void process(CtExpression ctExpression) {
        this.ctExpression = ctExpression;

        CtUnaryOperator op = new CtUnaryOperatorImpl();
        op.setOperand(this.ctExpression);
        op.setKind(UnaryOperatorKind.NOT);
        ctExpression.replace(op);

        MutationProject.testMutation(this, ctExpression);
    }

    @Override
    public void revertChanges() {
        this.ctExpression.replace(((CtUnaryOperator) this.ctExpression).getOperand());
    }

    @Override
    public String getMutationDescription() {
        return String.format("Invert boolean expression %s in method %s",
                this.ctExpression,
                this.met.getSimpleName());
    }
}