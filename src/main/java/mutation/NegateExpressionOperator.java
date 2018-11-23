package mutation;

import mutationproject.MutationInfo;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtUnaryOperator;
import spoon.reflect.code.UnaryOperatorKind;
import spoon.support.reflect.code.CtUnaryOperatorImpl;

import java.util.function.BiConsumer;

public class NegateExpressionOperator extends MutationOperator<CtExpression>
{
    private CtExpression expression;
    private CtUnaryOperator operator;

    public NegateExpressionOperator(BiConsumer<MutationOperator<CtExpression>, MutationInfo> mutationConsumer)
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
    protected MutationInfo applyMutation(CtExpression element)
    {
        expression = element;
        operator = new CtUnaryOperatorImpl();
        expression.replace(operator);
        operator.setKind(UnaryOperatorKind.NOT);
        operator.setOperand(expression);
        return new MutationInfo("Negate boolean expression \"" + expression + "\"", getMethod().getSimpleName());
    }

    @Override
    protected void revertMutation()
    {
        operator.replace(expression);
    }
}