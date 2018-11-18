package mutation;

import mutation.MutationOperator;
import mutationproject.MutationProject;
import spoon.reflect.code.*;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;
import spoon.support.reflect.code.CtUnaryOperatorImpl;

import java.util.function.BiConsumer;

public class NegateExpressionOperator extends MutationOperator<CtExpression>
{
    private CtExpression expression;
    private CtUnaryOperator operator;

    public NegateExpressionOperator(BiConsumer<MutationOperator<CtExpression>, CtElement> mutationConsumer)
    {
        super(mutationConsumer);
    }

    @Override
    public void process(CtExpression ctExpression) // needed for Spoon to understand that we only want to process expressions
    {
        super.process(ctExpression);
    }

    @Override
    public boolean isToBeProcessed(CtExpression candidate)
    {
        return super.isToBeProcessed(candidate) && candidate.getType().getTypeDeclaration().getSimpleName().equals("boolean");
    }

    @Override
    protected void applyMutation(CtExpression element)
    {
        expression = element;
        operator = new CtUnaryOperatorImpl();
        expression.replace(operator);
        operator.setKind(UnaryOperatorKind.NOT);
        operator.setOperand(expression);
    }

    @Override
    protected void revertMutation()
    {
        operator.replace(expression);
    }

    @Override
    public String getMutationDescription()
    {
        return String.format("Negate boolean expression %s in method %s", expression, getMethod().getSimpleName());
    }
}