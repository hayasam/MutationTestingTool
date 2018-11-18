package mutation;

import spoon.reflect.code.BinaryOperatorKind;
import spoon.reflect.code.CtBinaryOperator;
import spoon.reflect.declaration.CtElement;

import java.util.function.BiConsumer;

public class SwapAndOrOperator extends MutationOperator<CtBinaryOperator>
{
    private CtBinaryOperator binaryOperator;

    public SwapAndOrOperator(BiConsumer<MutationOperator<CtBinaryOperator>, CtElement> mutationConsumer)
    {
        super(mutationConsumer);
    }

    @Override
    public void process(CtBinaryOperator ctBinaryOperator) // needed for Spoon to understand that we only want to process binary operators
    {
        super.process(ctBinaryOperator);
    }

    @Override
    public boolean isToBeProcessed(CtBinaryOperator candidate)
    {
        return super.isToBeProcessed(candidate) &&
                (candidate.getKind() == BinaryOperatorKind.AND || candidate.getKind() == BinaryOperatorKind.OR);
    }

    private static void swapAndOr(CtBinaryOperator bo)
    {
        if(bo.getKind() == BinaryOperatorKind.AND)
            bo.setKind(BinaryOperatorKind.OR);
        else if(bo.getKind() == BinaryOperatorKind.OR)
            bo.setKind(BinaryOperatorKind.AND);
    }

    @Override
    protected void applyMutation(CtBinaryOperator element)
    {
        binaryOperator = element;
        swapAndOr(binaryOperator);
    }

    @Override
    public void revertMutation()
    {
        swapAndOr(binaryOperator);
    }

    @Override
    public String getMutationDescription()
    {
        return String.format("Swap && and || between %s and %s in method %s",
                binaryOperator.getLeftHandOperand(), binaryOperator.getRightHandOperand(), getMethod().getSimpleName());
    }
}
