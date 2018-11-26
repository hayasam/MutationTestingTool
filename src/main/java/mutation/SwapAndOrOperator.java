package mutation;

import mutationproject.MutationInfo;
import spoon.reflect.code.BinaryOperatorKind;
import spoon.reflect.code.CtBinaryOperator;

import java.util.function.BiConsumer;

public class SwapAndOrOperator extends MutationOperator<CtBinaryOperator>
{
    private CtBinaryOperator binaryOperator;

    public SwapAndOrOperator(BiConsumer<MutationOperator<CtBinaryOperator>, MutationInfo> mutationConsumer, boolean revertAfterChange)
    {
        super(mutationConsumer, revertAfterChange);
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
    protected MutationInfo applyMutation(CtBinaryOperator element)
    {
        binaryOperator = element;
        swapAndOr(binaryOperator);
        return new MutationInfo(String.format("Swap && and || between \"%s\" and \"%s\"",
                binaryOperator.getLeftHandOperand(), binaryOperator.getRightHandOperand()), getMethod().getSimpleName());
    }

    @Override
    public void revertMutation()
    {
        swapAndOr(binaryOperator);
    }
}
