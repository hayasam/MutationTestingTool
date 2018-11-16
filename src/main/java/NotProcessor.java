import spoon.processing.AbstractProcessor;
import spoon.reflect.code.*;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;
import spoon.support.reflect.code.CtUnaryOperatorImpl;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class NotProcessor extends AbstractProcessor<CtExpression> implements IMutationProcessor {

    private CtExpression ctExpression;

    @Override
    public boolean isToBeProcessed(CtExpression candidate) {
        boolean isToBeProcessed = (candidate.getType().getTypeDeclaration().getSimpleName().equals("boolean"));
        if(isToBeProcessed) {
            System.out.println("[Valid candidate] !Not exp " + candidate.toString());
        }
        return isToBeProcessed;

    }

    public void process(CtExpression ctExpression) {
        this.ctExpression = ctExpression;

        CtUnaryOperator op = new CtUnaryOperatorImpl();
        op.setOperand(this.ctExpression);
        op.setKind(UnaryOperatorKind.NOT);
        ctExpression.replace(op);

        MutationProject.testMutation(ctExpression);
    }

    @Override
    public void revertChanges() {
        this.ctExpression.replace(((CtUnaryOperator) this.ctExpression).getOperand());
    }

    @Override
    public String getMutationDescription() {
        return null;
    }
}