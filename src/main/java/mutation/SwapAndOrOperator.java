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

    private static String getOperatorString(CtBinaryOperator bo)
    {
        if(bo.getKind() == BinaryOperatorKind.AND) return "&&";
        return "||";
    }

    @Override
    protected MutationInfo applyMutation(CtBinaryOperator element)
    {
        binaryOperator = element;
        String prevOperator = getOperatorString(binaryOperator);
        swapAndOr(binaryOperator);
        return new MutationInfo(String.format("Replaced %s with %s between \"%s\" and \"%s\"",
                prevOperator, getOperatorString(binaryOperator), binaryOperator.getLeftHandOperand(),
                binaryOperator.getRightHandOperand()), getMethod().getSimpleName());
    }

    @Override
    public void revertMutation()
    {
        swapAndOr(binaryOperator);
    }
}
