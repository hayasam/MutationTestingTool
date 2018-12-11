package mutation;

import mutationproject.MutationInfo;
import spoon.reflect.code.BinaryOperatorKind;
import spoon.reflect.code.CtBinaryOperator;

import java.util.function.BiConsumer;

public class SwapPlusMinusOperator extends MutationOperator<CtBinaryOperator>
{
    private CtBinaryOperator binaryOperator;

    public SwapPlusMinusOperator(BiConsumer<MutationOperator<CtBinaryOperator>, MutationInfo> mutationConsumer, boolean revertAfterChange)
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
                candidate.getLeftHandOperand().getType().isPrimitive() &&
                candidate.getRightHandOperand().getType().isPrimitive() &&
                (candidate.getKind() == BinaryOperatorKind.PLUS || candidate.getKind() == BinaryOperatorKind.MINUS);
    }

    private static void swapPlusMinus(CtBinaryOperator bo)
    {
        if(bo.getKind() == BinaryOperatorKind.PLUS)
            bo.setKind(BinaryOperatorKind.MINUS);
        else if(bo.getKind() == BinaryOperatorKind.MINUS)
            bo.setKind(BinaryOperatorKind.PLUS);
    }

    private static String getOperatorString(CtBinaryOperator bo)
    {
        if(bo.getKind() == BinaryOperatorKind.PLUS) return "+";
        return "-";
    }

    @Override
    protected MutationInfo applyMutation(CtBinaryOperator element)
    {
        binaryOperator = element;
        String prevOperator = getOperatorString(binaryOperator);
        swapPlusMinus(binaryOperator);
        return new MutationInfo(String.format("Replaced %s with %s between \"%s\" and \"%s\"",
                prevOperator, getOperatorString(binaryOperator), binaryOperator.getLeftHandOperand(),
                binaryOperator.getRightHandOperand()), getMethod().getSimpleName());
    }

    @Override
    public void revertMutation()
    {
        swapPlusMinus(binaryOperator);
    }
}
